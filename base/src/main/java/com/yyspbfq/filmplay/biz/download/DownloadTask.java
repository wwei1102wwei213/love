package com.yyspbfq.filmplay.biz.download;

import android.util.Log;

import com.wei.wlib.downloader.JFileDownloadListener;
import com.wei.wlib.util.WLibLog;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.bean.MessageEvent;
import com.yyspbfq.filmplay.db.DBHelper;
import com.yyspbfq.filmplay.db.VideoDownloadBean;
import com.yyspbfq.filmplay.utils.tools.FileUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask implements Runnable, JFileDownloadListener{

    public static final int TASK_STATUS_INIT = 0;
    public static final int TASK_STATUS_WORKING = 1;
    public static final int TASK_STATUS_PAUSE = 2;
    public static final int TASK_STATUS_CANCEL = 3;
    public static final int TASK_STATUS_COMPLETED = 4;


    public static final int DOWNLOAD_HANDLE_PROGRESS = 1;
    public static final int DOWNLOAD_HANDLE_CANCEL = 2;
    public static final int DOWNLOAD_HANDLE_PAUSE = 3;
    public static final int DOWNLOAD_HANDLE_COMPLETED = 4;
    public static final int DOWNLOAD_HANDLE_CHANGE_COUNT = 5;


    private static final String TEMP_END_STR = ".cld";
    private boolean isCanceled=false;
    private boolean isPaused=false;
    private boolean isWork = false;
    private int status;
    private VideoDownloadBean tag;
    private String url;
    private String fileName;

    public DownloadTask(String url, String fileName, VideoDownloadBean tag) {
        this.url = url;
        this.fileName = fileName + url.substring(url.lastIndexOf("."), url.length());
        this.tag = tag;
        status = TASK_STATUS_INIT;
    }

    @Override
    public void run() {
        status = TASK_STATUS_WORKING;
        isWork = true;
        DownloadTaskManager.getInstance().changeTaskNum(true);
        try {
            InputStream is = null;
            RandomAccessFile savedFile = null;
            File file = null;
            long downloadLength = 0;   //记录已经下载的文件长度
            //文件下载地址
            String downloadUrl = url;
            //下载文件存放的目录
            String directory = FileUtils.getVideoFileAbsolutePath(fileName);
            //创建一个文件
            file = new File(directory);

            if(file.exists()){
                //如果文件存在的话，得到文件的大小
//                downloadLength=file.length();
                downloadCompleted(file, 0);
            } else {
                File tempFile = null;
                tempFile = new File(directory + TEMP_END_STR);
                if(tempFile.exists()){
                    //如果文件存在的话，得到文件的大小
                    downloadLength=tempFile.length();
                }
                //得到下载内容的大小
                long contentLength=getContentLength(downloadUrl);
                if(contentLength==0){
                    onFail();
                }else if(contentLength==downloadLength){
                    //已下载字节和文件总字节相等，说明已经下载完成了
                    tempFile.renameTo(file);
                    downloadCompleted(file, 0);
                } else {
                    long startTime = System.currentTimeMillis();
                    if (tag.getVideo_size()!=contentLength) {
                        tag.setVideo_size(contentLength);
                        DBHelper.getInstance().updateDownloadDataSize(BaseApplication.getInstance(), tag);
                    }
                    OkHttpClient client=new OkHttpClient();
                    /**
                     * HTTP请求是有一个Header的，里面有个Range属性是定义下载区域的，它接收的值是一个区间范围，
                     * 比如：Range:bytes=0-10000。这样我们就可以按照一定的规则，将一个大文件拆分为若干很小的部分，
                     * 然后分批次的下载，每个小块下载完成之后，再合并到文件中；这样即使下载中断了，重新下载时，
                     * 也可以通过文件的字节长度来判断下载的起始点，然后重启断点续传的过程，直到最后完成下载过程。
                     */
                    Request request=new Request.Builder()
                            .addHeader("RANGE","bytes="+downloadLength+"-")  //断点续传要用到的，指示下载的区间
                            .url(downloadUrl)
                            .build();
                    try {
                        Response response=client.newCall(request).execute();
                        if(response!=null){
                            is=response.body().byteStream();
                            savedFile=new RandomAccessFile(tempFile,"rw");
                            savedFile.seek(downloadLength);//跳过已经下载的字节
                            byte[] b=new byte[8*1024];
                            int total=0;
                            int len;
                            while((len=is.read(b))!=-1){
                                if(isCanceled){
                                    break;
                                }else if(isPaused){
                                    break;
                                }else {
                                    total+=len;
                                    savedFile.write(b,0,len);
                                    //计算已经下载的百分比
                                    int progress=(int)((total+downloadLength)*100/contentLength);
                                    //注意：在doInBackground()中是不可以进行UI操作的，如果需要更新UI,比如说反馈当前任务的执行进度，
                                    //可以调用publishProgress()方法完成。
                                    if (progress>=100) {
                                        tempFile.renameTo(file);
                                        downloadCompleted(file, System.currentTimeMillis() - startTime);
                                    } else {
                                        tag.setCurrent_size(total+downloadLength);
                                        downloadProgress(progress, 0, 0);
                                    }

                                }
                            }
                            response.body().close();
                        }
                    } catch (IOException e) {
                        onFail();
                        e.printStackTrace();
                    }finally {
                        try{
                            if(is!=null){
                                is.close();
                            }
                            if(savedFile!=null){
                                savedFile.close();
                            }
                            if(isCanceled&&file!=null){
                                file.delete();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e){
            onFail();
            WLibLog.e(e);
        }
        /*if (isCanceled) {
            MessageEvent event = new MessageEvent();
            event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
            event.setFlag(DOWNLOAD_HANDLE_CANCEL);
            EventBus.getDefault().post(event);
        }*/
        if (isPaused) {
            MessageEvent event = new MessageEvent();
            event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
            event.setFlag(DOWNLOAD_HANDLE_PAUSE);
            EventBus.getDefault().post(event);
        }
        isWork = false;
        DownloadTaskManager.getInstance().changeTaskNum(false);
    }



    public synchronized void  pauseDownload(){
        if (isWork) {
            isWork = false;
            status = TASK_STATUS_PAUSE;
            isPaused=true;
        }
    }

    public synchronized void cancelDownload(){
        if (isWork) {
            isWork = false;
            status = TASK_STATUS_CANCEL;
            isCanceled=true;
        }
    }

    public boolean isWork() {
        return isWork;
    }

    public int getStatus() {
        return status;
    }

    public VideoDownloadBean getTag() {
        return tag;
    }

    /**
     * 得到下载内容的大小
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(downloadUrl).build();
        try {
            Response response=client.newCall(request).execute();
            if(response!=null&&response.isSuccessful()){
                long contentLength=response.body().contentLength();
                response.body().close();
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  0;
    }

    private String TAG = "downloadtest";

    private int oldProgress = 0;
    @Override
    public void downloadProgress(int progress, double speed, long remainTime) {
        if (progress!=oldProgress) {
//            Log.e(TAG,"downloadProgress====> progress:"+progress+", speed:"+speed+", remainTime:"+remainTime);
            oldProgress = progress;
            MessageEvent event = new MessageEvent();
            event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
            event.setFlag(DOWNLOAD_HANDLE_PROGRESS);
            EventBus.getDefault().post(event);
        }
    }

    @Override
    public void downloadCompleted(File file, long downloadTime) {
        isWork = false;
        Log.e(TAG,"downloadCompleted:");
        status = TASK_STATUS_COMPLETED;
        if (downloadTime!=0) {
            tag.setState(99);
            tag.setFinish_time(System.currentTimeMillis());
            tag.setPatch(file.getAbsolutePath());
            DBHelper.getInstance().updateDownloadState(BaseApplication.getInstance(), tag);
        } else {
            tag.setVideo_size(file.length());
            tag.setState(99);
            tag.setFinish_time(System.currentTimeMillis());
            tag.setPatch(file.getAbsolutePath());
            DBHelper.getInstance().updateDownloadState(BaseApplication.getInstance(), tag);
        }
        MessageEvent event = new MessageEvent();
        event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
        event.setFlag(DOWNLOAD_HANDLE_COMPLETED);
        EventBus.getDefault().post(event);
//        Log.e(TAG,"downloadCompleted====> downloadTime:"+downloadTime);
    }

    @Override
    public void onFail() {
        Log.e(TAG,"onFail====> onFail");
    }

}
