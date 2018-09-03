package com.fuyou.play.bean;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class BaseBean {

    private int status;
    private ErrorBean error;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

}
