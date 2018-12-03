package com.doojaa.base.biz.http;


public class CookieManger {

    private CookieManger(){}

    private String cookies;

    private static CookieManger instance;

    public static CookieManger getInstance() {
        if (instance==null) {
            synchronized (CookieManger.class) {
                if (instance==null) {
                    instance = new CookieManger();
                }
            }
        }
        return instance;
    }

    /*public String getCookies() {
        if (cookies==null) {
            cookies = cookieHeader(BaseApplication.getInstance().cookieJar.getCookies());
        }
        return cookies;
    }

    public void updateCookies() {
        cookies = cookieHeader(BaseApplication.getInstance().cookieJar.getCookies());
    }

    private String cookieHeader(List<Cookie> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString();
    }*/
}
