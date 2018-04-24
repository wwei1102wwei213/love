package com.slove.play.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018/3/30 0030.
 */
public class Const {

    public static final String TAG_LOG = "LOVE_PLAY";
    public static final String LOG_TAG_HTTP = "LOVE_HTTP";
    public static final String LOG_TAG = "LOVE_PLAY";

    public final static String SL_DISK_IMG_BIG = Environment.getExternalStorageDirectory().getPath() + "/slove/image/big/"; //大图片的本地存储*/
    public final static String SL_DISK_IMG_SMALL = Environment.getExternalStorageDirectory().getPath() + "/slove/image/small/"; //小图片的本地存储*/
    public final static String SL_DISK_IMG_TEMP = Environment.getExternalStorageDirectory().getPath() + "/slove/temp/";
    public final static String SL_DIRECTORY_PICTURE_FOLD_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
            + File.separator + "slove";                   // 图片存储到相册目录下

}
