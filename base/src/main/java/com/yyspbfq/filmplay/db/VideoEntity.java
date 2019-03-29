package com.yyspbfq.filmplay.db;

import android.text.TextUtils;

import com.yyspbfq.filmplay.utils.glide.ImageHostUtils;

public class VideoEntity {

/*
“id”: “3”,
“name”: “aaaaaaa”,
“video_thump”: “http://p.xxxxxxxx.com/20181220/mQ87DQxF/1.jpg“,
“video_url”: “http://www.baidu.com/20181220/mQ87DQxF/index.m3u8“,
“down_url”: “http://www.taobao.com/20181220/mQ87DQxF/mp4/mQ87DQxF.mp4“,
“play_lid”: “1”,
“down_lid”: “2”,
“video_time”: “00:00:48”,
“watch_num”: “2”,
“love_num”: “1”,
“hate_num”: “1”,
“cdate”: “1545301545”,
“intro”: “”,
“label”: “asdfadsfasd”,
“canWatch”: 0 //0:不可以观看了（没有观看次数了） 1:可以观看*/

    //视频ID
    private String id;
    //分类ID
    private String tid;
    //视频类型
    private String video_type;
    //观看次数
    private String watch_num;
    //喜欢人数
    private String love_num;
    //踩人数
    private String hate_num;
    //视频名称
    private String name;
    //播放地址
    private String video_url;
    //下载地址
    private String down_url;
    //缩微图
    private String video_thump;
    //时长
    private String video_time;
    //简介
    private String intro;
    //创建时间
    private String cdate;
    //标签
    private String label;
    //是否可观看 0:不可以观看了（没有观看次数了） 1:可以观看
    private int canWatch;
    private int canDown;
    private int canCollection;
    private int canLoveOrHate;
    private String c_date;
    private String watch_time;

    public String getC_date() {
        return c_date;
    }

    public void setC_date(String c_date) {
        this.c_date = c_date;
    }

    public String getWatch_time() {
        return watch_time;
    }

    public void setWatch_time(String watch_time) {
        this.watch_time = watch_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getWatch_num() {
        return watch_num;
    }

    public void setWatch_num(String watch_num) {
        this.watch_num = watch_num;
    }

    public String getLove_num() {
        return love_num;
    }

    public void setLove_num(String love_num) {
        this.love_num = love_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    public String getVideo_thump() {
        return ImageHostUtils.contact(video_thump);
    }

    public void setVideo_thump(String video_thump) {
        this.video_thump = video_thump;
    }

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getHate_num() {
        return hate_num;
    }

    public void setHate_num(String hate_num) {
        this.hate_num = hate_num;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCanWatch() {
        return canWatch;
    }

    public int getCanDown() {
        return canDown;
    }

    public int getCanCollection() {
        return canCollection;
    }

    public int getCanLoveOrHate() {
        return canLoveOrHate;
    }

    public void setCanWatch(int canWatch) {
        this.canWatch = canWatch;
    }

    public void setCanDown(int canDown) {
        this.canDown = canDown;
    }

    public void setCanCollection(int canCollection) {
        this.canCollection = canCollection;
    }

    public void setCanLoveOrHate(int canLoveOrHate) {
        this.canLoveOrHate = canLoveOrHate;
    }

    public long getVideoSize() {
        if (TextUtils.isEmpty(video_time)) return 0;
        long r = 0;
        try {
            String[] args = video_time.split(":");
            if (args.length==2) {
                r = Integer.parseInt(args[0])*60 + Integer.parseInt(args[1]);
            } else if (args.length==3) {
                r = Integer.parseInt(args[0])*3600 + Integer.parseInt(args[1])*60 + Integer.parseInt(args[2]);
            }
        } catch (Exception e){

        }
        return r;
    }
}
