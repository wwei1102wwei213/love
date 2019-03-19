package com.yyspbfq.filmplay.utils.tools;

import android.os.Environment;
import android.text.TextUtils;

import com.wei.wlib.util.WLibLog;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理录音文件的类
 */
public class FileUtils {

    private  static String rootPath="MeVideo";
    //视频文件夹
    private final static String AUDIO_MP4_BASE_PATH = "/"+rootPath+"/video/";
    //apk文件夹
    public final static String UPDATE_APK_BASE_PATH = "/"+rootPath+"/apk/";

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
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_MP4_BASE_PATH;
            File file = new File(fileBasePath);
            //创建目录
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioRawPath = fileBasePath + fileName;
        }
        return mAudioRawPath;
    }

    public static String getVideoFileAbsolutePathWithMp4(String fileName){
        if(TextUtils.isEmpty(fileName)){
            throw new NullPointerException("fileName isEmpty");
        }
        if(!isSdcardExit()){
            throw new IllegalStateException("sd card no found");
        }
        String mAudioRawPath = "";
        if (isSdcardExit()) {
            if (!fileName.endsWith(".mp6")) {
                fileName = fileName + ".mp6";
            }
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_MP4_BASE_PATH;
            File file = new File(fileBasePath);
            //创建目录
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioRawPath = fileBasePath + fileName;
        }
        return mAudioRawPath;
    }

    public static String getVideoFileAbsolutePathWithMp4CLD(String fileName){
        if(TextUtils.isEmpty(fileName)){
            throw new NullPointerException("fileName isEmpty");
        }
        if(!isSdcardExit()){
            throw new IllegalStateException("sd card no found");
        }
        String mAudioRawPath = "";
        if (isSdcardExit()) {
            if (!fileName.endsWith(".mp6")) {
                fileName = fileName + ".mp6";
            }
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_MP4_BASE_PATH;
            File file = new File(fileBasePath);
            //创建目录
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioRawPath = fileBasePath + fileName + ".cld";
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

    //删除所有文件
    public static void deleteFiles() {
        try {
            List<File> list = new ArrayList<>();
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_MP4_BASE_PATH;
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

    /**
     * 获取视频文件夹总大小
     */
    public static String getVideoFileSize() {
        try {
            if (isSdcardExit()) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_MP4_BASE_PATH);
                //创建目录
                if (file.exists()) {
                    double d = getFolderSize(file);
                    if (d>1024) {
                        double gb = d / 1024;
                        DecimalFormat df = new DecimalFormat("#.00");
                        String result = df.format(gb) + "GB";
                        if (result.contains(".00")) {
                            result = result.replace(".00", "");
                        } else {
                            if (result.endsWith(".0GB")) {
                                result = result.replace(".0GB", "GB");
                            }
                        }
                        return result;
                    } else {
                        return String.valueOf(d) + "M";
                    }


                }
            }
        } catch (Exception e){

        }
        return "0M";
    }

    public static long getFolderSize(File file){
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            }else {
                size = size + fileList[i].length();
            }
        }
        return size/1024/1024;
    }

    /**
     * 根据ID删除视频下载数据
     * @param id 视频id
     */
    public static void deleteFileById(String id) {
        if (TextUtils.isEmpty(id)) return;
        try {
            File file = new File(getVideoFileAbsolutePathWithMp4(id));
            if (file.exists()) {
                file.delete();
            }
            File file2 = new File(getVideoFileAbsolutePathWithMp4CLD(id));
            if (file2.exists()) {
                file2.delete();
            }
        } catch (Exception e){
            WLibLog.e(e);
        }
    }

    /**
     * 判断视频是否下载过
     * @param id 视频id
     * @return
     */
    public static boolean isVideoExist(String id) {
        if (TextUtils.isEmpty(id)) return false;
        try {
            File file = new File(getVideoFileAbsolutePathWithMp4(id));
            if (file.exists()) {
                return true;
            }
        } catch (Exception e){
            WLibLog.e(e);
        }
        return false;
    }
}
