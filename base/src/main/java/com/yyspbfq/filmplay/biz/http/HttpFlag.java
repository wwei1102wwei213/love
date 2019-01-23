package com.yyspbfq.filmplay.biz.http;

import com.yyspbfq.filmplay.BuildConfig;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class HttpFlag {

    public static final String HOST = BuildConfig.DEBUG?"47.107.94.24:88":"47.107.94.24:88";
    private final static String HTTP = "http://";
//    private final static String HTTPS = "https://";
    private final static String URL_SPLITTER = "/";

    public static String BASE_URL = HTTP + HOST + URL_SPLITTER;


    //------------------------------- web地址  start ----------------------------------------------
    //首单支付失败跳转页面
    public static final String URL_PAY_FAILED = BASE_URL + "ios/first_pay_failed";
    //升级地址
    public static String URL_UPDATE = "http://47.107.94.24:86/android/update.json";
    //福利中心
    public static final String URL_FULI = BASE_URL + "my/fuli";
    //评论协议
    public static final String URL_USER_COMMENT_PROTOCOL = BASE_URL + "ios/xieyi";
    //领取连续签到奖励
    public static final int FLAG_SIGN_RECEIVE_REWARD = 2;
    public static final String URL_SIGN_RECEIVE_REWARD = BASE_URL + "/user/getSignReward";
    //上传头像
    public static final int FLAG_UPLOAD_IMG = 3;
    public static final String URL_UPLOAD_IMG = "http://47.107.94.24:80/upload.php";

    //登陆
    public static final int FLAG_LOGIN_MOBILE = 11;
    public static final String URL_LOGIN_MOBILE = BASE_URL + "login/phoneLogin";
    //视频详情
    public static final int FLAG_VIDEO_DETAIL = 12;
    public static final String URL_VIDEO_DETAIL = BASE_URL + "video/detail";
    //视频详情猜你喜欢
    public static final int FLAG_VIDEO_DETAIL_LIKE = 13;
    public static final String URL_VIDEO_DETAIL_LIKE = BASE_URL + "video/getDetailLike";
    //视频收藏
    public static final int FLAG_VIDEO_COLLECTION = 14;
    public static final String URL_VIDEO_COLLECTION = BASE_URL + "user/collection";
    //视频点赞
    public static final int FLAG_VIDEO_SET_LIKE = 15;
    public static final String URL_VIDEO_SET_LIKE = BASE_URL + "video/setLikeAndHate";
    //首页 - 推荐栏目
    public static final int FLAG_HOME_MENU_COLUMN = 16;
    public static final String URL_HOME_MENU_COLUMN = BASE_URL + "index/getMenu";
    //首页 - 猜你喜欢
    public static final int FLAG_HOME_LIKE_VIDEO = 17;
    public static final String URL_HOME_LIKE_VIDEO = BASE_URL + "index/getLikeVideo";
    //首页 - 最新
    public static final int FLAG_HOME_NEWEST_VIDEO = 18;
    public static final String URL_HOME_NEWEST_VIDEO = BASE_URL + "index/getNewestVideo";
    //首页 - 最热
    public static final int FLAG_HOME_HOT_VIDEO = 19;
    public static final String URL_HOME_HOT_VIDEO = BASE_URL + "index/getHotVideo";
    //首页 - 分类列表
    public static final int FLAG_HOME_CLASSIFY_LIST = 20;
    public static final String URL_HOME_CLASSIFY_LIST = BASE_URL + "type/getRecType";
    //首页 - 幻灯片
    public static final int FLAG_HOME_SLIDE = 21;
    public static final String URL_HOME_SLIDE = BASE_URL + "slide/show";
    //发现
    public static final int FLAG_MAIN_DISCOVER = 22;
    public static final String URL_MAIN_DISCOVER = BASE_URL + "find/show";
    //频道 - 推荐专栏
    public static final int FLAG_CHANNEL_RECOMMEND = 23;
    public static final String URL_CHANNEL_RECOMMEND = BASE_URL + "topic/getRecommend";
    //频道 - 热门专栏
    public static final int FLAG_CHANNEL_HOT = 24;
    public static final String URL_CHANNEL_HOT = BASE_URL + "topic/getHotTopic";
    //频道 - 人气明星
    public static final int FLAG_CHANNEL_HUMAN = 25;
    public static final String URL_CHANNEL_HUMAN = BASE_URL + "topic/getPopularStar";
    //专栏列表
    public static final int FLAG_CHANNEL_LIST = 26;
    public static final String URL_CHANNEL_LIST = BASE_URL + "topic/getList";
    //专栏详情相似列表
    public static final int FLAG_CHANNEL_DETAIL_LIST = 27;
    public static final String URL_CHANNEL_DETAIL_LIST = BASE_URL + "topic/getVideoListByTopicId";
    //专栏详情
    public static final int FLAG_CHANNEL_DETAIL = 28;
    public static final String URL_CHANNEL_DETAIL = BASE_URL + "topic/getInfoById";
    //获取全部分类
    public static final int FLAG_ALL_CLASSIFY = 29;
    public static final String URL_ALL_CLASSIFY = BASE_URL + "type/getAllType";
    //获取分类列表
    public static final int FLAG_CLASSIFY_LIST = 30;
    public static final String URL_CLASSIFY_LIST = BASE_URL + "video/getList";
    //大家都在搜
    public static final int FLAG_SEARCH_HOT = 31;
    public static final String URL_SEARCH_HOT = BASE_URL + "Search/getHotSearchWord";
    //搜索
    public static final int FLAG_SEARCH_KEYWORD = 32;
    public static final String URL_SEARCH_KEYWORD = BASE_URL + "search/getByKeyword";
    //用户信息
    public static final int FLAG_USER_INFO = 33;
    public static final String URL_USER_INFO = BASE_URL + "user/info";
    //推广升级
    public static final int FLAG_INVITE_TASK = 34;
    public static final String URL_INVITE_TASK = BASE_URL + "user/task";
    //推广列表
    public static final int FLAG_INVITE_LIST = 35;
    public static final String URL_INVITE_LIST = BASE_URL + "user/getInviteList";
    //点击广告
    public static final int FLAG_INVITE_CLICK_ADS = 36;
    public static final String URL_INVITE_CLICK_ADS = BASE_URL + "user/clickAdv";
    //保存二维码
    public static final int FLAG_INVITE_SAVE_CODE = 37;
    public static final String URL_INVITE_SAVE_CODE = BASE_URL + "user/copyQrCode";
    //获取二维码信息
    public static final int FLAG_INVITE_CODE_MSG = 38;
    public static final String URL_INVITE_CODE_MSG = BASE_URL + "user/getExtension";






    //获取系统配置
    public static final int FLAG_SYSTEM_CONFIG = 22;
    public static final String URL_SYSTEM_CONFIG = BASE_URL + "system/config";
    //修改用户信息
    public static final int FLAG_USER_UPDATE_INFO = 55;
    public static final String URL_USER_UPDATE_INFO = BASE_URL + "user/updateInfo";
    //手机验证码
    public static final int FLAG_LOGIN_MOBILE_CODE = 74;
    public static final String URL_LOGIN_MOBILE_CODE = BASE_URL + "login/verifyCode";


    /**推广模块**/





}
