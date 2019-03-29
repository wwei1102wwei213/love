package com.yyspbfq.filmplay.bean;

import com.yyspbfq.filmplay.utils.glide.ImageHostUtils;

public class ThumbEntity {

    private String url;
    private int width, height;

    public String getUrl() {
        return ImageHostUtils.contact(url);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
