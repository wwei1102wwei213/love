package com.yyspbfq.filmplay.bean;

import com.yyspbfq.filmplay.utils.glide.ImageHostUtils;

public class AdvertBean {

/*"thumb": "http://127.0.0.1:9233/uploads/default/201901/18/default201901181558534635.png",
        "url": "http://www.baiud.com",   // 相应跳转/下载URL
        "space": 3, // 首屏的多久显示一久（秒数）（某些广告可以突略）
        "type": 1, // 动作 1:跳转 2:下载
        "showtime": 3, // 显示几秒 （秒数）
        "isclose": 1 // 是否可关闭 1:可以 0:不可以*/
    private Object thumb;
    private String url, title, id;
    private int space, type, showtime, isclose;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getThumb() {
        return thumb;
    }

    public String getThumbWithHost() {
        if (thumb==null) return null;
        return ImageHostUtils.contact(thumb+"");

    }

    public void setThumb(Object thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getShowtime() {
        return showtime;
    }

    public void setShowtime(int showtime) {
        this.showtime = showtime;
    }

    public int getIsclose() {
        return isclose;
    }

    public void setIsclose(int isclose) {
        this.isclose = isclose;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
