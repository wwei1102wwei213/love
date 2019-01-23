package com.yyspbfq.filmplay.bean;

import java.util.List;

public class HomeColumnBean {

    /*"id":"7",
            "sort":"7",
            "name":"动作",
            "type":1,
            "videos":[

            ],
            "top":1*/

    private String id, sort, name;
    private int type, top;
    private List<VideoShortBean> videos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public List<VideoShortBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoShortBean> videos) {
        this.videos = videos;
    }
}
