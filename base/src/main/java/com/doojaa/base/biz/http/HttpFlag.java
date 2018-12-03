package com.doojaa.base.biz.http;


import com.doojaa.base.BuildConfig;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class HttpFlag {

    public static final int RESULT_OK = 1;

    public static final int HTTP_ERROR_NETWORK = 1;     //网络异常
    public static final int HTTP_ERROR_DISCONNECT = 2;  //连接异常
    public static final int HTTP_ERROR_RESULT_EMPTY = 3;  //result数据为空
    public static final int HTTP_ERROR_FORMAT = 4;      //数据解析异常
    public static final int HTTP_ERROR_CODE = 5;      //数据code异常
    public static final int HTTP_ERROR_DATA_EMPTY = 6;  //data数据为空
    public static final int HTTP_ERROR_OTHER = 7;       //其他异常

    public static final String HOST = BuildConfig.DEBUG?"192.168.20.10:80":"192.168.20.10:80";
    private final static String HTTP = "http://";
//    private final static String HTTPS = "https://";
    private final static String URL_SPLITTER = "/";

    public static String BASE_URL = HTTP + HOST + URL_SPLITTER;


    //------------------------------- web地址  start ----------------------------------------------
    //首单支付失败跳转页面
    public static final String URL_PAY_FAILED = BASE_URL + "ios/first_pay_failed";
    //升级地址
    public static String URL_UPDATE = "http://down.dj.com/android/update.json";
    //福利中心
    public static final String URL_FULI = BASE_URL + "my/fuli";
    //评论协议
    public static final String URL_USER_COMMENT_PROTOCOL = BASE_URL + "ios/xieyi";
    //------------------------------- web地址    end ----------------------------------------------

    //------------------------------- Post接口 start ----------------------------------------------
    //领取连续签到奖励
    public static final int FLAG_SIGN_RECEIVE_REWARD = 2;
    public static final String URL_SIGN_RECEIVE_REWARD = BASE_URL + "/user/getSignReward";
    //上传头像
    public static final int FLAG_UPLOAD_IMG = 3;
    public static final String URL_UPLOAD_IMG = "http://image.dj.com/upload.php";
    //-----------支付请求接口-------------
    //---------- 支付请求接口 end--------------
    //手机登陆
    public static final int FLAG_LOGIN_MOBILE = 12;
    public static final String URL_LOGIN_MOBILE = BASE_URL + "login/phone";
    //小说更新，重要接口
    public static final int FLAG_BOOK_UPDATE = 13;
    public static final String URL_BOOK_UPDATE = BASE_URL + "book/getUpdateBook";
    //小说阅读记录，重要接口
    public static final int FLAG_BOOK_RECORD = 14;
    public static final String URL_BOOK_RECORD = BASE_URL + "user/readDetail";
    //提交本地小说状态
    public static final int FLAG_BOOK_EDIT_STATUS = 15;
    public static final String URL_BOOK_EDIT_STATUS = BASE_URL + "user/editBooksStatus";
    //------------------------------- Post接口 end   ----------------------------------------------

    //------------------------------- GET接口 start ----------------------------------------------
    //获取系统配置
    public static final int FLAG_SYSTEM_CONFIG = 22;
    public static final String URL_SYSTEM_CONFIG = BASE_URL + "system/config";
    //活动配置
    public static final int FLAG_ACTIVITY_CONFIG = 23;
    public static final String URL_ACTIVITY_CONFIG = BASE_URL + "system/activity";
    //底部按钮 - 推荐
    public static final int FLAG_BOOK_RECOMMEND = 24;
    public static final String URL_BOOK_RECOMMEND = BASE_URL + "books/recommendBooks";
    //底部按钮 - 书城
    public static final int FLAG_BOOK_CITY = 25;
    public static final String URL_BOOK_CITY = BASE_URL + "books/index";
    //分类
    public static final int FLAG_BOOK_TYPE = 26;
    public static final String URL_BOOK_TYPE = BASE_URL + "books/type";
    //排行榜
    public static final int FLAG_BOOK_RANK = 27;
    public static final String URL_BOOK_RANK = BASE_URL + "books/rankList";
    //排行榜详情页
    public static final int FLAG_BOOK_RANK_DETAIL = 28;
    public static final String URL_BOOK_RANK_DETAIL = BASE_URL + "books/rankcate";
    //书架Banner
    public static final int FLAG_BOOK_SHELF_BANNER = 29;
    public static final String URL_BOOK_SHELF_BANNER= BASE_URL + "system/bookShelfSlide";
    //书城-更多
    public static final int FLAG_BOOK_CITY_MORE = 30;
    public static final String URL_BOOK_CITY_MORE = BASE_URL + "books/more";
    //作者-更多
    public static final int FLAG_BOOK_AUTHOR_MORE = 31;
    public static final String URL_BOOK_AUTHOR_MORE = BASE_URL + "books/moreBooks";
    //搜索
    public static final int FLAG_BOOK_SEARCH = 32;
    public static final String URL_BOOK_SEARCH = BASE_URL + "books/search";
    //本周热搜榜
    public static final int FLAG_BOOK_WEEKSEARCHHOT = 33;
    public static final String URL_BOOK_WEEKSEARCHHOT = BASE_URL + "books/weekSearchHot";
    //搜索-热词
    public static final int FLAG_BOOK_SEARCH_HOT = 34;
    public static final String URL_BOOK_SEARCH_HOT = BASE_URL + "books/search_hot";
    //搜索-自动补全
    public static final int FLAG_BOOK_SEARCH_AUTO = 35;
    public static final String URL_BOOK_SEARCH_AUTO = BASE_URL + "books/auto_search";
    //小说详情页
    public static final int FLAG_BOOK_PROFILE = 36;
    public static final String URL_BOOK_PROFILE = BASE_URL + "book/profile";
    //换一换
    public static final int FLAG_BOOK_GUESSLIKE = 37;
    public static final String URL_BOOK_GUESSLIKE = BASE_URL + "book/guessLike";
    //猜你喜欢
    public static final int FLAG_BOOK_CITY_GUESS_YOUR_LOVE = 38;
    public static final String URL_BOOK_CITY_GUESS_YOUR_LOVE = BASE_URL + "books/like_change";
    //小说标签页
    public static final int FLAG_BOOK_LABEL = 39;
    public static final String URL_BOOK_LABEL = BASE_URL + "books/label";
    //用户信息
    public static final int FLAG_USER_INFO = 40;
    public static final String URL_USER_INFO = BASE_URL + "user/info";
    //添加小说
    public static final int FLAG_BOOK_ADD = 41;
    public static final String URL_BOOK_ADD = BASE_URL + "user/addBook";
    //移除小说
    public static final int FLAG_BOOK_DEL = 42;
    public static final String URL_BOOK_DEL = BASE_URL + "user/delBook";
    //同步书架
    public static final int FLAG_USER_BOOKS = 43;
    public static final String URL_USER_BOOKS = BASE_URL + "user/books?all=1";
    //分类
    public static final int FLAG_BOOK_TYPE_BOOKS = 44;
    public static final String URL_BOOK_TYPE_BOOKS = BASE_URL + "books/typeBooks";
    //小说目录
    public static final int FLAG_BOOK_CATALOG = 45;
    public static final String URL_BOOK_CATALOG = BASE_URL + "book/category";
    //小说章节内容
    public static final int FLAG_BOOK_CHAPTER = 46;
    public static final String URL_BOOK_CHAPTER = BASE_URL + "book/charpter";
    //小说结束页
    public static final int FLAG_BOOK_READ_END = 47;
    public static final String URL_BOOK_READ_END = BASE_URL + "book/readEnd";
    //阅读记录保存
    public static final int FLAG_BOOK_READLOG_USER = 48;
    public static final String URL_BOOK_READLOG_USER = BASE_URL + "user/readLog";
    public static final int FLAG_BOOK_READLOG_VISITOR = 49;
    public static final String URL_BOOK_READLOG_VISITOR = BASE_URL + "visitor/readLog";
    //阅读页分享
    public static final int FLAG_BOOK_SHARE_READING = 50;
    public static final String URL_BOOK_SHARE_READING = BASE_URL + "book/readingShare";
    //获取关注微信公众号接口
    public static final int FLAG_SYSTEM_WEIXIN = 51;
    public static final String URL_SYSTEM_WEIXIN = BASE_URL + "system/mpWeixin";
    //小说免费专区
    public static final int FLAG_FREE = 52;
    public static final String URL_FREE = BASE_URL + "books/free";
    //小说短篇专区
    public static final int FLAG_SHORT = 54;
    public static final String URL_SHORT = BASE_URL + "books/short";
    //修改用户信息
    public static final int FLAG_USER_UPDATE_INFO = 55;
    public static final String URL_USER_UPDATE_INFO = BASE_URL + "user/updateInfo";
    //累计阅读得书券
    public static final int FLAG_COUPON_FOR_READ = 57;
    public static final String URL_COUPON_FOR_READ = BASE_URL + "activity/reportReadTime";
    //我的消息
    public static final int FLAG_USER_MESSAGE = 63;
    public static final String URL_USER_MESSAGE = BASE_URL + "system/message";
    //我的好友
    public static final int FLAG_USER_INVITE = 64;
    public static final String URL_USER_INVITE = BASE_URL + "user/inviteRecords";

    //追书补偿(bid : 小说id;act : 可选参数，传入1时 领取并返回领取后的状态)
    public static final int FLAG_COMPENSATE = 66;
    public static final String URL_COMPENSATE = BASE_URL + "user/getCompensate";
    //------------------------------- 解锁相关接口 start ----------------------------------------------
    //当前小说已解锁章节
    public static final int FLAG_BUY_BOOKCOIN = 67;
    public static final String URL_BUY_BOOKCOIN = BASE_URL + "user/buyBookCoin";
    //解锁一章面板
    public static final int FLAG_UNLOCK_CHARPTER_PANEL = 68;
    public static final String URL_UNLOCK_CHARPTER_PANEL = BASE_URL + "visitor/showUnlockCharpterPanel";
    //解锁正本面板-短篇小说使用
    public static final int FLAG_UNLOCK_BOOK_PANEL = 69;
    public static final String URL_UNLOCK_BOOK_PANEL = BASE_URL + "visitor/showUnlockBookPanel";
    //解锁单章
    public static final int FLAG_UNLOCK_CHARPTER = 70;
    public static final String URL_UNLOCK_CHARPTER = BASE_URL + "user/unlockCharpter";
    //解锁正本-短篇小说使用
    public static final int FLAG_UNLOCK_BOOK = 71;
    public static final String URL_UNLOCK_BOOK = BASE_URL + "user/unlockBook";
    //小说反馈
    public static final int FLAG_FEED_BACK = 72;
    public static final String URL_FEED_BACK = BASE_URL + "books/record_feedback";
    //兑换书券
    public static final int FLAG_CONVERT_CODE = 73;
    public static final String URL_CONVERT_CODE = BASE_URL + "user/exchangeBookvoucher";
    //------------------------------- 解锁相关接口 end ----------------------------------------------
    //手机验证码
    public static final int FLAG_LOGIN_MOBILE_CODE = 74;
    public static final String URL_LOGIN_MOBILE_CODE = BASE_URL + "login/verifyCode";
    //------------------------------- 评论请求接口 start --------------------------------------------
    //小说所有评论
    public static final int FLAG_COMMENT_BOOKS = 75;
    public static final String URL_COMMENT_BOOKS = BASE_URL + "book/bookComments";
    //发表小说评论，回复评论
    public static final int FLAG_COMMENT_BOOKS_SEND = 76;
    public static final String URL_COMMENT_BOOKS_SEND = BASE_URL + "book/commentBook";
    //章节所有评论
    public static final int FLAG_COMMENT_CHARPTERS = 77;
    public static final String URL_COMMENT_CHARPTERS = BASE_URL + "book/chapterComments";
    //发表章节评论
    public static final int FLAG_COMMENT_CHARPTERS_SEND = 78;
    public static final String URL_COMMENT_CHARPTERS_SEND = BASE_URL + "book/commentCharpter";
    //评论详情
    public static final int FLAG_COMMENT_DETAIL = 79;
    public static final String URL_COMMENT_DETAIL = BASE_URL + "book/commentDetail";
    //评论点赞和取消赞
    public static final int FLAG_COMMENT_VOTE = 80;
    public static final String URL_COMMENT_VOTE = BASE_URL + "book/vote_comment";

    //------------------------------- 评论请求接口 end ----------------------------------------------
    //推广升级
    public static final int FLAG_INVITE_TASK = 81;
    public static final String URL_INVITE_TASK = BASE_URL + "user/task";
    //保存二维码
    public static final int FLAG_INVITE_SAVE_CODE = 82;
    public static final String URL_INVITE_SAVE_CODE = BASE_URL + "user/isCopy";
    //点击广告
    public static final int FLAG_INVITE_CLICK_ADS = 83;
    public static final String URL_INVITE_CLICK_ADS = BASE_URL + "user/clickAdv";

    //------------------------------- GET接口     end ----------------------------------------------

    //-------------------------------      已废弃     ----------------------------------------------
    //书架推荐小说
    public static final int FLAG_BOOK_SHELF_RECOMMEND = -1;
    public static final String URL_BOOK_SHELF_RECOMMEND = BASE_URL + "books/bookStore";
    //点赞  参数:bid 小说bid   / like  1 点赞 0 取消点赞
    public static final int FLAG_BOOK_LIKE = -1;
    public static final String URL_BOOK_LIKE = BASE_URL + "book/like";
    //解锁多章面板
    public static final int FLAG_UNLOCK_CHARPTERS_PANEL = -1;
    public static final String URL_UNLOCK_CHARPTERS_PANEL = BASE_URL + "user/showUnlockCharptersPanel";
    //解锁多章
    public static final int FLAG_UNLOCK_CHARPTERS = -1;
    public static final String URL_UNLOCK_CHARPTERS = BASE_URL + "user/unlockCharpters";
    //流量红包是否已拆
    public static final int FLAG_SIGN_FLUX_EXIST = -1;
    public static final String URL_SIGN_FLUX_EXIST = BASE_URL + "user/giftExist";
    //打开流量红包
    public static final int FLAG_SIGN_FLUX = -1;
    public static final String URL_SIGN_FLUX = BASE_URL + "user/gift";
    //流量红包明细
    public static final int FLAG_SIGN_FLUX_DETAIL = -1;
    public static final String URL_SIGN_FLUX_DETAIL = BASE_URL + "my/flowGiftDetail";
    //流量红包提现接口
    public static final int FLAG_SIGN_FLUX_WITHDRAW = -1;
    public static final String URL_SIGN_FLUX_WITHDRAW = BASE_URL + "user/flow_withdraw";
    //签到面板
    public static final int FLAG_SIGN_PANEL = -1;
    public static final String URL_SIGN_PANEL = BASE_URL + "user/signPanel";
    //countly统计
    public static final int FLAG_CONTLY = -1;
    public static final String URL_CONTLY = "http://bdata.xxxx.com/index/submit_log";
}
