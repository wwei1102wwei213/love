package com.yyspbfq.filmplay.bean;

public class InfoMessageEntity {

    /*"title": "这是一个系统消息",
                "vid": "0",
                "openType": "1", //操作类型，1:打开url，2:应用下载，3:打开视频 4:活动页面
                "url": "http://www.baidu.com"*/

    private String id, title, vid, openType, url, ctime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
}
