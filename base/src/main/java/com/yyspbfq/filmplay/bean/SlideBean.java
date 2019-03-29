package com.yyspbfq.filmplay.bean;

import com.yyspbfq.filmplay.utils.glide.ImageHostUtils;

public class SlideBean {

    /*"title": "这还是一个幻灯片",
            "vid": "3",
            "openType": "3", //操作类型，1:打开url，2:应用下载，3:打开视频 4:活动页面
            "url": "",
            "pic": "http://p.xxxxxxxx.com/uploads/default/201901/17/default201901171157567846.png"*/

    private String title, vid, openType, url, pic, id;

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

    public String getPic() {
        return ImageHostUtils.contact(pic);
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
