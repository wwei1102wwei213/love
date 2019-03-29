package com.yyspbfq.filmplay.db;

import com.yyspbfq.filmplay.utils.glide.ImageHostUtils;

import java.text.DecimalFormat;

public class VideoDownloadBean {

    private String vid, name, video_time, video_thumb, download_url;
    private long video_size;
    private long current_size;
    private long create_time;
    private int state;
    private long finish_time;
    private String patch;

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public String getVideo_thumb() {
        return ImageHostUtils.contact(video_thumb);
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    public long getVideo_size() {
        return video_size;
    }

    public void setVideo_size(long video_size) {
        this.video_size = video_size;
    }

    public long getCurrent_size() {
        return current_size;
    }

    public void setCurrent_size(long current_size) {
        this.current_size = current_size;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(long finish_time) {
        this.finish_time = finish_time;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public String getMBSize(long s) {
        if (s==0) return "0M";
        double d = s/1024;
        d = d/1024;
        DecimalFormat df = new DecimalFormat("#.0");
        String result = df.format(d) + "M";
        if (result.startsWith(".")) result = "0" + result;
        return result;
    }

    public VideoEntity getVideo() {
        VideoEntity entity = new VideoEntity();
        entity.setId(vid);
        entity.setVideo_thump(video_thumb);
        entity.setName(name);
        entity.setVideo_time(video_time);
        return entity;
    }

}
