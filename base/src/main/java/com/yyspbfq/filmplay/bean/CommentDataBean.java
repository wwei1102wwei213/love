package com.yyspbfq.filmplay.bean;

import android.text.TextUtils;

import java.util.List;

public class CommentDataBean {

    private List<CommentEntity> data;
    private int size;
    private String count;

    public List<CommentEntity> getData() {
        return data;
    }

    public void setData(List<CommentEntity> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCount() {
        if (TextUtils.isEmpty(count)) return "0";
        return count;
    }
}
