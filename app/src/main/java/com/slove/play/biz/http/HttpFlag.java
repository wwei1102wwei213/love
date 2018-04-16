package com.slove.play.biz.http;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class HttpFlag {

    private static final String IP_PORT = "http://119.23.232.112:8001/slove/";

    public static final int TEST = 1;
    public static final String TEST_URL = IP_PORT+"testApi.jsp";

    public static final int LOGIN = 2;
    public static final String LOGIN_URL = IP_PORT+"login.jsp";

    public static final int REGISTER = 3;
    public static final String REGISTER_URL = IP_PORT+"reg.jsp";

    public static final int USER_DETAIL = 4;
    public static final String USER_DETAIL_URL = IP_PORT+"userDetail.jsp";

}
