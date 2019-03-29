package com.yyspbfq.filmplay.bean;

import com.yyspbfq.filmplay.utils.glide.ImageHostUtils;

public class VideoShortBean {

    /*"id":"4",
                    "name":"test video",
                    "video_thump":"http://p.xxxxxxxx.com/20181228/DzsBvBMv/1.jpg"*/

    private String id, name, video_thump, quality, subheading;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideo_thump() {
        return ImageHostUtils.contact(video_thump);
    }

    public void setVideo_thump(String video_thump) {
        this.video_thump = video_thump;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }
}
