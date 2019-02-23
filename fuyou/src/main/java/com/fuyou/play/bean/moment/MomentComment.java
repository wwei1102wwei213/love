package com.fuyou.play.bean.moment;

/**
 * Created by Administrator on 2018-08-17.
 */

public class MomentComment {

    private String content;
    private MomentSender sender;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MomentSender getSender() {
        return sender;
    }

    public void setSender(MomentSender sender) {
        this.sender = sender;
    }
}
