package com.yyspbfq.filmplay.biz.download;

import android.util.Log;

import com.wei.wlib.thread.WLibThreadPoolManager;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.bean.MessageEvent;
import com.yyspbfq.filmplay.db.DBHelper;
import com.yyspbfq.filmplay.db.VideoDownloadBean;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DownloadTaskManager {

    private static final String TAG = "DownloadTaskManager";

    private HashMap<String, DownloadTask> map;
    private LinkedList<String> waitList;

    private static int MAX_TASK = 3;

    private int count = 0;

    private static DownloadTaskManager instance;

    public static DownloadTaskManager getInstance() {
        if (instance==null) {
            synchronized (DownloadTaskManager.class) {
                if (instance==null) {
                    instance = new DownloadTaskManager();
                    MAX_TASK = SPLongUtils.getInt(BaseApplication.getInstance(), "film_download_count", 3);
                }
            }
        }
        return instance;
    }

    public void setMaxTask(int maxTask) {
        MAX_TASK = maxTask;
        if (count>MAX_TASK) {
            try {
                int del = count - MAX_TASK;
                for (String temp : map.keySet()) {
                    pauseTask(temp);
                    del--;
                    if (del==0) break;
                }
                MessageEvent event = new MessageEvent();
                event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
                event.setFlag(DownloadTask.DOWNLOAD_HANDLE_CHANGE_COUNT);
                EventBus.getDefault().post(event);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (count<MAX_TASK) {
            int add = MAX_TASK - count;
            while (add>0) {
                try {
                    if (waitList!=null && waitList.size()>0) {
                        WLibThreadPoolManager.execute(map.get(waitList.removeFirst()));
                    }
                    add--;
                } catch (Exception e){
                    break;
                }
            }

            MessageEvent event = new MessageEvent();
            event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
            event.setFlag(DownloadTask.DOWNLOAD_HANDLE_CHANGE_COUNT);
            EventBus.getDefault().post(event);
        }
    }

    public int getCurrentCount() {
        return count;
    }

    /**
     * 添加下载任务
     * @param key 唯一标识
     * @param url 下载地址
     * @param fileName 唯一文件名（和key相同）
     * @param entity 视频数据
     */
    public void addTask(String key, String url, String fileName, VideoEntity entity) {
        if ( key == null || url == null ) return;
        if ( map == null ){
            map = new HashMap<>();
        }
        try {
            VideoDownloadBean temp = DBHelper.getInstance().getDownloadRecordByKey(BaseApplication.getInstance(), key);
            if (temp!=null&&temp.getState()==99) {
                ToastUtils.showToast("已缓存过该视频");
                return;
            }
            if ( map.get(key) != null ) {
                if ( map.get(key).isWork() ) {
                    ToastUtils.showToast("下载任务已经存在");
                    return;
                } else {
                    Log.e(TAG,"DownloadTaskManager====> map.remove(key):"+key);
                    map.remove(key);
                }
            }
            VideoDownloadBean bean = new VideoDownloadBean();
            bean.setVid(entity.getId());
            bean.setCreate_time(System.currentTimeMillis());
            bean.setVideo_time(entity.getVideo_time());
            bean.setVideo_thumb(entity.getVideo_thump());
            bean.setName(entity.getName());
            bean.setDownload_url(url);
            DBHelper.getInstance().insertDownloadRecord(BaseApplication.getInstance(), bean);
            DownloadTask task = new DownloadTask(url, fileName, bean);
            Log.e(TAG,"DownloadTaskManager====> map.put( key, task ):"+key);
            map.put( key, task );
            if ( count >= MAX_TASK ) {
                if ( waitList == null ) {
                    waitList = new LinkedList<>();
                }
                Log.e(TAG,"DownloadTaskManager====> waitList.add(key):"+key);
                ToastUtils.showToast("下载任务数量过多,进入等待列表");
                waitList.add(key);
            } else {
                Log.e(TAG,"DownloadTaskManager====> WLibThreadPoolManager.execute(task):"+key);
                ToastUtils.showToast("下载任务添加成功");
                WLibThreadPoolManager.execute(task);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    /**
     * 添加下载任务
     * @param key 唯一标识
     * @param url 下载地址
     * @param fileName 唯一文件名（和key相同）
     * @param entity 下载数据
     */
    public synchronized void addTask(String key, String url, String fileName, VideoDownloadBean entity) {
        if ( key == null || url == null ) return;
        if ( map == null ){
            map = new HashMap<>();
        }
        try {
            /*VideoDownloadBean temp = DBHelper.getInstance().getDownloadRecordByKey(BaseApplication.getInstance(), key);
            if (temp!=null&&temp.getState()==99) {
                ToastUtils.showToast("已缓存过该视频");
                return;
            }*/
            if ( map.get(key) != null ) {
                if ( map.get(key).isWork() ) {
                    ToastUtils.showToast("下载任务已经存在");
                    return;
                } else {
                    Log.e(TAG,"DownloadTaskManager====> map.remove(key):"+key);
                    map.remove(key);
                }
            }
//            DBHelper.getInstance().insertDownloadRecord(BaseApplication.getInstance(), entity);
            DownloadTask task = new DownloadTask(url, fileName, entity);
            Log.e(TAG,"DownloadTaskManager====> map.put( key, task ):"+key);
            map.put( key, task );
            if ( count >= MAX_TASK ) {
                if ( waitList == null ) {
                    waitList = new LinkedList<>();
                }
                Log.e(TAG,"DownloadTaskManager====> waitList.add(key):"+key);
                ToastUtils.showToast("下载任务数量过多,进入等待列表");
                waitList.add(key);
            } else {
                Log.e(TAG,"DownloadTaskManager====> WLibThreadPoolManager.execute(task):"+key);
                WLibThreadPoolManager.execute(task);
            }
        } catch (Exception e){
            BLog.e(e);
        }

    }

    public synchronized void pauseTask(String key) {
        if (map==null) return;
        if (map.get(key)!=null&&map.get(key).isWork()) {
            Log.e(TAG,"DownloadTaskManager====> pauseTask:"+key);
            map.get(key).pauseDownload();
        } else {
            Log.e(TAG,"DownloadTaskManager====> pauseTask:任务已结束");
        }
    }

    public void cancelTask(String key) {
        if (map==null) return;
        if (map.get(key)!=null) {
            Log.e(TAG,"DownloadTaskManager====> cancelTask:"+key);
            DBHelper.getInstance().deleteDownloadRecord(BaseApplication.getInstance(), key);
            MessageEvent event = new MessageEvent();
            event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
            event.setFlag(DownloadTask.DOWNLOAD_HANDLE_CANCEL);
            EventBus.getDefault().post(event);
            if (map.get(key).isWork()) {
                map.get(key).cancelDownload();
            }
            map.remove(key);

        }
    }

    public void cancelTask(List<String> keys) {
        DBHelper.getInstance().deleteDownloadRecord(BaseApplication.getInstance(), keys);
        MessageEvent event = new MessageEvent();
        event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
        event.setFlag(DownloadTask.DOWNLOAD_HANDLE_CANCEL);
        EventBus.getDefault().post(event);
        if (map==null) return;
        for (String key:keys) {
            if (map.get(key)!=null) {
                if (map.get(key).isWork()) {
                    map.get(key).cancelDownload();
                }
                WLibThreadPoolManager.cancel(map.remove(key));
            }
        }
    }


    public int checkState(String key) {
        if (map==null||map.get(key)==null) {
            return -1;//等待开始
        } else {
            return map.get(key).getStatus();
        }
    }

    public void changeTaskNum(String key, boolean isAdd) {
        if (isAdd) {
            count++;
        } else {
            if (count>0&&map.get(key)!=null) count--;
            if (count<MAX_TASK && waitList!=null && waitList.size()>0) {
                WLibThreadPoolManager.execute(
                        map.get(waitList.removeFirst()));
            }
        }
        MessageEvent event = new MessageEvent();
        event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
        event.setFlag(DownloadTask.DOWNLOAD_HANDLE_CHANGE_COUNT);
        EventBus.getDefault().post(event);
    }

    public VideoDownloadBean getTagByKey(String key) {
        if (key==null || map == null || map.get(key) == null) return null;
        return map.get(key).getTag();
    }

    public void removeTask(String key) {
        if ( map==null || !map.containsKey(key)) return;
        WLibThreadPoolManager.cancel(map.remove(key));
    }

    public void clearAll() {
        try {
            WLibThreadPoolManager.stop();
            if (map!=null) {
                map.clear();
            }
            instance = null;
        } catch (Exception e){
            BLog.e(e);
        }

    }



}
