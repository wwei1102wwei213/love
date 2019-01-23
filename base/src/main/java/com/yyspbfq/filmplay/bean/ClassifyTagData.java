package com.yyspbfq.filmplay.bean;

import java.util.List;

public class ClassifyTagData {

    private List<VideoShortBean> data;
    private int size;

    public List<VideoShortBean> getData() {
        return data;
    }

    public void setData(List<VideoShortBean> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
