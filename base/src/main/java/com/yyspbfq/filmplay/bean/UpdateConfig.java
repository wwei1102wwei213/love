package com.yyspbfq.filmplay.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by liu jun on 2016/4/14.
 */
public class UpdateConfig implements Serializable {
    public String versionCode;
    public String versionName;
    public String downurl;
    public String title;
    public String text;
    public String autoup;
    public String sign;
    public String website;
    public String meHost;

    public String getDownUrl() {
        if (!TextUtils.isEmpty(downurl)&&!TextUtils.isEmpty(meHost)) {
            if (!meHost.endsWith("/")) {
                return meHost + "/" + downurl;
            } else {
                return meHost + downurl;
            }
        }
        return "";
    }

}
