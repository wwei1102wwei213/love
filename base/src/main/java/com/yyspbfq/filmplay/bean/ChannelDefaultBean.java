package com.yyspbfq.filmplay.bean;

import com.yyspbfq.filmplay.utils.glide.ImageHostUtils;

import java.util.List;

public class ChannelDefaultBean {

    private String id, title, thumb, pic, remark;
    private List<VideoShortBean> videos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return ImageHostUtils.contact(thumb);
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public List<VideoShortBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoShortBean> videos) {
        this.videos = videos;
    }

    public String getPic() {
        return ImageHostUtils.contact(pic);
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
