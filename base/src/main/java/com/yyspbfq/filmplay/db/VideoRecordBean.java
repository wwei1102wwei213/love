package com.yyspbfq.filmplay.db;

import com.google.gson.Gson;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.TimeZone;

public class VideoRecordBean {

    //视频信息
    private String detail;
    //视频id
    private String vid;
    //更新时间
    private long update_time;
    //最后保存的进度
    private long last_progress;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getLast_progress() {
        return last_progress;
    }

    public void setLast_progress(long last_progress) {
        this.last_progress = last_progress;
    }

    /*public String getName() {
        String result = "";
        try {
            UserInfo userInfo = new Gson().fromJson(detail, UserInfo.class);
            result = userInfo.name==null?"":userInfo.name;
        } catch (Exception e){
            BLog.e(e);
        }
        return result;
    }*/

    public VideoEntity getVideoData() {
        VideoEntity entity = null;
        try {
            entity = new Gson().fromJson(detail, VideoEntity.class);
            return entity==null ? new VideoEntity() : entity;
        } catch (Exception e){
            BLog.e(e);
        }
        return new VideoEntity();
    }

    public int getTodayType() {
        long current = System.currentTimeMillis();
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();
        if (update_time>=zero) {
            return 1;
        }
        if (update_time >= zero-1000*3600*24*6 ){
            return 2;
        }
        return 3;
    }

}
