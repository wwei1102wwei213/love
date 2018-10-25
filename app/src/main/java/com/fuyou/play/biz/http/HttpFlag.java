package com.fuyou.play.biz.http;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class HttpFlag {

    public static final int ERROR_NORMAL = 0;
    public static final int ERROR_TIME_OUT = 1;

    private static final String IP_PORT = "http://www.fuyou.site/";
    public static final String HOST = "www.fuyou.site";

    public static final int CHECK = 1;
    public static final String CHECK_URL = IP_PORT+"check.jsp";

    public static final int LOGIN = 2;
    public static final String LOGIN_URL = IP_PORT+"login.jsp";

    public static final int REGISTER = 3;
    public static final String REGISTER_URL = IP_PORT+"reg.jsp";

    public static final int USER_DETAIL = 4;
    public static final String USER_DETAIL_URL = IP_PORT+"userDetail.jsp";

    public static final int PHONE_CODE = 5;
    public static final String PHONE_CODE_URL = IP_PORT+"smsCode.jsp";

    public static final int LOGIN_OUT = 6;
    public static final String LOGIN_OUT_URL = IP_PORT+"loginOut.jsp";

    public static final int DISCUSS_ADD = 7;
    public static final String DISCUSS_ADD_URL = IP_PORT+"addDiscuss.jsp";

    public static final int DISCUSS_QUERY = 8;
    public static final String DISCUSS_QUERY_URL = IP_PORT+"queryDiscuss.jsp";

    public static final int MOMENT_USER = 9;
    public static final String MOMENT_USER_URL = "http://thoughtworks-ios.herokuapp.com/user/jsmith";

    public static final int MOMENT_LIST = 10;
    public static final String MOMENT_LIST_URL = "http://thoughtworks-ios.herokuapp.com/user/jsmith/tweets";

}
