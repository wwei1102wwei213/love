package com.fuyou.play.bean;

/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class QNTokenData {

    /*{"token":"ciWY4hKMhdnvMR4eBnBm-K3pYBQyVz5mWOG6cKcu:zWBuR0wwuID8H_9FD_ccCyrVXAA=:eyJzY29wZSI6Im1hY2hhdCIsImRlYWRsaW5lIjoxNTI2NDY0ODg3fQ==","host":"http://machat.mcinno.com/"}}*/
    private String token, host;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
