package com.yyspbfq.filmplay.bean;

import com.yyspbfq.filmplay.db.VideoEntity;

import java.util.List;

public class DiscoverDataBean {

    private List<VideoEntity> data;
    private int size;

    public List<VideoEntity> getData() {
        return data;
    }

    public void setData(List<VideoEntity> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
