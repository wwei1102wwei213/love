package com.yyspbfq.filmplay.bean;

import java.util.List;

public class InfoMessageBean {

    private List<InfoMessageEntity> data;
    private int size;

    public List<InfoMessageEntity> getData() {
        return data;
    }

    public void setData(List<InfoMessageEntity> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
