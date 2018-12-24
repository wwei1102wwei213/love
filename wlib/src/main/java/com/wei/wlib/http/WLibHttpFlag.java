package com.wei.wlib.http;


/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class WLibHttpFlag {

    public static final int RESULT_OK = 1;

    public static final int HTTP_ERROR_NETWORK = 1;     //网络异常
    public static final int HTTP_ERROR_DISCONNECT = 2;  //连接异常
    public static final int HTTP_ERROR_RESULT_EMPTY = 3;  //result数据为空
    public static final int HTTP_ERROR_FORMAT = 4;      //数据解析异常
    public static final int HTTP_ERROR_CODE = 5;      //数据code异常
    public static final int HTTP_ERROR_DATA_EMPTY = 6;  //data数据为空
    public static final int HTTP_ERROR_OTHER = 7;       //其他异常

    private final static String HTTP = "http://";
    private final static String HTTPS = "https://";
    private final static String URL_SPLITTER = "/";

    private static boolean DEBUG_MODEL = true;

    private static String HOST = DEBUG_MODEL?"192.168.20.10:80":"192.168.20.10:80";


    public static String BASE_URL = HTTP + HOST + URL_SPLITTER;


    //------------------------------- web地址  start ----------------------------------------------
    //首单支付失败跳转页面
    public static final String URL_PAY_FAILED = BASE_URL + "ios/first_pay_failed";
    //升级地址
    public static String URL_UPDATE = "http://down.dj.com/android/update.json";
    //福利中心
    public static final String URL_FULI = BASE_URL + "my/fuli";


    //------------------------------- Post接口 start ----------------------------------------------

    //获取GET接口地址
    public static String getUrl(int flag) {
        String result = null;

        return result;
    }

    //获取POST接口地址
    public static String postUrl(int flag) {
        String result = null;

        return result;
    }
}
