package com.yyspbfq.filmplay.bean;

import com.yyspbfq.filmplay.utils.glide.ImageHostUtils;

public class CommentEntity {

    /*"id": "1",
                "vid": "2",
                "fig": "http://127.0.0.1:9224/default_header.png",
                "nick": "138****4679",
                "mess": "这是一个评论",
                "zan": "1",
                "cretime": "1547780752",
                "createDate": "2小时前"*/

    private String id, vid, fig, nick, mess, zan, cretime, createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getFig() {
        return ImageHostUtils.contact(fig);
    }

    public void setFig(String fig) {
        this.fig = fig;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public String getCretime() {
        return cretime;
    }

    public void setCretime(String cretime) {
        this.cretime = cretime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
