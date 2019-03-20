package com.yyspbfq.filmplay.bean;

import java.util.List;

public class FeedbackMessageBean {

    private List<FeedbackMessageEntity> data;
    private int size;

    public List<FeedbackMessageEntity> getData() {
        return data;
    }

    public void setData(List<FeedbackMessageEntity> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
