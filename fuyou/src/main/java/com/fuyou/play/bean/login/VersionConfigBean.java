package com.fuyou.play.bean.login;

/**
 * Created by Administrator on 2018-07-25.
 */

public class VersionConfigBean {
    /*"Update":0,"Summary":"修复已知bug","Url":"http://www.baidu.com","VersionNum":"null"*/
    private int Update;
    private String Summary, Url, VersionNum;

    public int getUpdate() {
        return Update;
    }

    public void setUpdate(int update) {
        Update = update;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getVersionNum() {
        return VersionNum;
    }

    public void setVersionNum(String versionNum) {
        VersionNum = versionNum;
    }
}
