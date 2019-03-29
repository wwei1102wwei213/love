package com.yyspbfq.filmplay.bean;

import java.util.List;

public class HostInfoBean {

    /*"api":[
        {
            "id":"9",
            "url":"http://47.107.94.24:88",
            "request_url":"http://47.107.94.24:88/test.json"
        }
    ],
   */

    private List<HostItem> api, image, official, update, issue, status;

    public List<HostItem> getApi() {
        return api;
    }

    public List<HostItem> getImage() {
        return image;
    }

    public List<HostItem> getOfficial() {
        return official;
    }

    public List<HostItem> getUpdate() {
        return update;
    }

    public List<HostItem> getIssue() {
        return issue;
    }

    public List<HostItem> getStatus() {
        return status;
    }
}
