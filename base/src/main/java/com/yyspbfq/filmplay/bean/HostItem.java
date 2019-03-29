package com.yyspbfq.filmplay.bean;

import android.text.TextUtils;

public class HostItem {

    /*{
        "id":"12",
            "url":"http://47.107.94.24:95",
            "request_url":"http://47.107.94.24:95/test.json"
    }*/
    private String id, url, request_url;

    public String getId() {
        return id;
    }

    public String getUrl() {
        if (!TextUtils.isEmpty(url)&&!url.endsWith("/")) {
            return url+"/";
        }
        return url;
    }

    public String getWebsite() {
        return url;
    }

    public String getRequest_url() {
        return request_url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRequest_url(String request_url) {
        this.request_url = request_url;
    }
}
