package com.yyspbfq.filmplay.bean;

public class InviteCodeBean {

    /*"data": {
        "extension": "1、视频全部免费，没有充值入口！\r\n\r\n2、视频全部免费，永久免费，\r\n\r\n下载请戳我， 使劲戳不会疼哦↓↓↓ http://gg.jxyvip.com/?rid=WOQWE8\r\n（如果链接打不开请复制到浏览器中打开）",  //复制内容
        "officialWebsite": "http://www.baidu.com", //官网
        "extendBackGroup": "http://p.xxxxxxxx.com/uploads/default/201901/16/default201901161343127500.png", //背景
        "url": "http://gg.jxyvip.com/?rid=WOQWE8" //推广链接
    }*/

    private String extension, officialWebsite, extendBackGroup, url;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getOfficialWebsite() {
        return officialWebsite;
    }

    public void setOfficialWebsite(String officialWebsite) {
        this.officialWebsite = officialWebsite;
    }

    public String getExtendBackGroup() {
        return extendBackGroup;
    }

    public void setExtendBackGroup(String extendBackGroup) {
        this.extendBackGroup = extendBackGroup;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
