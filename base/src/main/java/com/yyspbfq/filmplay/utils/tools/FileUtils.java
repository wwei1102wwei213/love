package com.yyspbfq.filmplay.utils.tools;

import android.os.Environment;
import android.text.TextUtils;

import com.wei.wlib.util.WLibLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理录音文件的类
 */
public class FileUtils {

    private  static String rootPath="MeVideo";
    //pcm文件夹
    private final static String AUDIO_MP4_BASEPATH = "/"+rootPath+"/video/";

    private static void setRootPath(String rootPath){
        FileUtils.rootPath=rootPath;
    }

    public static String getVideoFileAbsolutePath(String fileName){
        if(TextUtils.isEmpty(fileName)){
            throw new NullPointerException("fileName isEmpty");
        }
        if(!isSdcardExit()){
            throw new IllegalStateException("sd card no found");
        }
        String mAudioRawPath = "";
        if (isSdcardExit()) {
            /*if (!fileName.endsWith(".mp4")) {
                fileName = fileName + ".mp4";
            }*/
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_MP4_BASEPATH;
            File file = new File(fileBasePath);
            //创建目录
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioRawPath = fileBasePath + fileName;
        }
        return mAudioRawPath;
    }

    //判断是否有外部存储设备sdcard
    public static boolean isSdcardExit() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    //删除所有pcm文件
    public static void deleteFiles() {
        try {
            List<File> list = new ArrayList<>();
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_MP4_BASEPATH;
            File rootFile = new File(fileBasePath);
            if (!rootFile.exists()) {
            } else {
                File[] files = rootFile.listFiles();
                for (File file : files) {
                    try {
                        file.delete();
                    } catch (Exception e){

                    }
                }
            }
        } catch (Exception e){
            WLibLog.e(e);
        }
    }
}
