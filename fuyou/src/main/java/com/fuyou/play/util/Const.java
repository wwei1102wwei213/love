package com.fuyou.play.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018/3/30 0030.
 */
public class Const {

    public static final String TAG_LOG = "PLAY";
    public static final String LOG_TAG_HTTP = "PLAY_HTTP";
    public static final String LOG_TAG_ERROR = "PLAY_ERROR";
    public static final String LOG_TAG = "PLAY";

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
    public final static String SL_DISK_IMG_LOVE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MLove/temp/";
    public final static String SL_DIRECTORY_PICTURE_FOLD_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
            + File.separator + "ML";                   // 图片存储到相册目录下

    public final static String FY_DIRECTORY_PICTURE_FOLD_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
            + File.separator + "fuyou";                   // 图片存储到相册目录下

    public static final String CHAT_ACCOUNT_PASSWORD = "123456";

    /**Intent消息串**/
    //聊天页面
    public static final String INTENT_CHAT_TYPE = "intent_chat_type";
    public static final String INTENT_CHAT_USER = "intent_chat_user";

    public static final String INTENT_MAX_NUM = "intent_max_num";
    public static final String INTENT_SELECTED_PICTURE = "intent_selected_picture";
}
