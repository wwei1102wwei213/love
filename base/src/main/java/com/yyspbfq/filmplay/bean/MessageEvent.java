package com.yyspbfq.filmplay.bean;

public class MessageEvent {

    /**
     * 用户登录事件
     */
    public static final int MSG_GET_USER_INFO = 0x10008;
    /**
     * 下载更新
     */
    public static final int MSG_DOWNLOAD_VIDEO = 0x10009;

    /**
     * 事件类型，参考MSG_前缀常量
     */
    private int message;
    /**
     * 消息
     */
    private String msg;

    private int flag;

    private int getUserInfoStatus;

    private int status;


    public MessageEvent() {
    }

    public MessageEvent(int message) {
        this.message = message;
    }

    public MessageEvent(int message, String json) {
        this.message = message;
        this.msg = json;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
