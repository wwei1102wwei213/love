package com.fuyou.play.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018/3/30 0030.
 */
public class Const {

    public static final String TAG_LOG = "ML_PLAY";
    public static final String LOG_TAG_HTTP = "ML_HTTP";
    public static final String LOG_TAG_ERROR = "ML_ERROR";
    public static final String LOG_TAG = "ML_PLAY";

    public static final String CONTACTS_WX_APPID = "wx9c58b3025ddbc6a4";
    public static final String RECEIVER_ACTION_WXLOGIN = "receiver_action_wx_login";
    public static final String RECEIVER_ACTION_WXSHARE = "receiver_action_wx_share";
    public static final String RECEIVER_ACTION_SHARE_1 = "receiver_action_wx_share_1";  //选择问题页面
    public static final String RECEIVER_ACTION_SHARE_2 = "receiver_action_wx_share_2";  //卡牌详情
    public static final String RECEIVER_ACTION_SHARE_3 = "receiver_action_wx_share_3";  //注册季卡
    public static final String RECEIVER_ACTION_SHARE_4 = "receiver_action_wx_share_4";  //冷却结果

    public static final String SP_VERSION_CONFIG = "sp_version_config_ml";
    public static final String SP_COOLING_RESULT = "sp_cooling_result";

    public final static String SL_DISK_IMG_BIG = Environment.getExternalStorageDirectory().getPath() + "/ML/image/big/"; //大图片的本地存储*/
    public final static String SL_DISK_IMG_SMALL = Environment.getExternalStorageDirectory().getPath() + "/ML/image/small/"; //小图片的本地存储*/
    public final static String SL_DISK_IMG_TEMP = Environment.getExternalStorageDirectory().getPath() + "/ML/temp/";
    public final static String SL_DIRECTORY_PICTURE_FOLD_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
            + File.separator + "ML";                   // 图片存储到相册目录下

}
