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

    //升级地址
    public static String URL_UPDATE = "http://47.107.94.24:86/android/update.json";
    //评论协议
    public static final String URL_USER_COMMENT_PROTOCOL = BASE_URL + "ios/xieyi";


    //上传头像
    public static final int FLAG_UPLOAD_IMG = 3;
    public static final String URL_UPLOAD_IMG = "http://47.107.94.24:90/upload.php";

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
    //VIP等级兑换
    public static final int FLAG_LEVEL_EXCHANGE = 39;
    public static final String URL_LEVEL_EXCHANGE = BASE_URL + "user/exchangeLevel";
    //意见反馈
    public static final int FLAG_FEEDBACK_SEND = 40;
    public static final String URL_FEEDBACK_SEND = BASE_URL + "Feedback/sent";
    //用户退出
    public static final int FLAG_USER_EXIT = 41;
    public static final String URL_USER_EXIT = BASE_URL + "login/loginOut";
    //广告 - 闪屏页
    public static final int FLAG_ADVERT_FIRST_SCREEN = 42;
    public static final String URL_ADVERT_FIRST_SCREEN = BASE_URL + "Advert/getFirstScreen";
    //广告 - 首页
    public static final int FLAG_ADVERT_INDEX = 43;
    public static final String URL_ADVERT_INDEX = BASE_URL + "Advert/getIndex";
    //广告 - 用户中心
    public static final int FLAG_ADVERT_USER_CENTER = 44;
    public static final String URL_ADVERT_USER_CENTER = BASE_URL + "Advert/getUser";
    //广告 - 视频上
    public static final int FLAG_ADVERT_VIDEO_ABOVE = 45;
    public static final String URL_ADVERT_VIDEO_ABOVE = BASE_URL + "Advert/getVideo";
    //广告 - 视频详情
    public static final int FLAG_ADVERT_VIDEO_DETAIL = 46;
    public static final String URL_ADVERT_VIDEO_DETAIL = BASE_URL + "Advert/getDetail";
    //修改用户信息
    public static final int FLAG_UPDATE_INFO = 47;
    public static final String URL_UPDATE_INFO = BASE_URL + "user/updateInfo";
    //系统消息
    public static final int FLAG_MESSAGE_SHOW = 48;
    public static final String URL_MESSAGE_SHOW = BASE_URL + "message/show";
    //收藏列表
    public static final int FLAG_INFO_COLLATION = 49;
    public static final String URL_INFO_COLLATION = BASE_URL + "user/getCollectionList";
    //删除收藏
    public static final int FLAG_DELETE_COLLATION = 50;
    public static final String URL_DELETE_COLLATION = BASE_URL + "user/delCollection";
    //删除播放记录
    public static final int FLAG_DELETE_VIDEO_RECORD = 51;
    public static final String URL_DELETE_VIDEO_RECORD = BASE_URL + "user/delViewRecord";
    //设置播放记录
    public static final int FLAG_SET_VIDEO_RECORD = 52;
    public static final String URL_SET_VIDEO_RECORD = BASE_URL + "user/setViewRecord";
    //同步播放记录
    public static final int FLAG_SYNC_VIDEO_RECORD = 53;
    public static final String URL_SYNC_VIDEO_RECORD = BASE_URL + "user/syncViewRecord";
    //扣除播放次数
    public static final int FLAG_DEDUCTION_COIN = 54;
    public static final String URL_DEDUCTION_COIN = BASE_URL + "video/deductionVideoNum";
    //评论列表
    public static final int FLAG_COMMENT_LIST = 55;
    public static final String URL_COMMENT_LIST = BASE_URL + "Comment/show";
    //发表评论
    public static final int FLAG_COMMENT_SENT = 56;
    public static final String URL_COMMENT_SENT = BASE_URL + "Comment/sent";
    //发表评论
    public static final int FLAG_COMMENT_LIKE = 57;
    public static final String URL_COMMENT_LIKE = BASE_URL + "Comment/zan";
    //帮组与反馈
    public static final int FLAG_INFO_HELP = 58;
    public static final String URL_INFO_HELP = BASE_URL + "help/show";



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
