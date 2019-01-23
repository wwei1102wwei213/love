package com.yyspbfq.filmplay.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.yyspbfq.filmplay.BaseApplication;

import java.util.Locale;

/**
 * Created by wwei on 2018/4/24 0024.
 *
 * 公共数据帮助类
 */

public class CommonUtils {

    private static String VersionNum;
    private static String VersionCodeStr;
    private static String UUID;
    private static String ChannelName;
    private static String LANGUAGE;
    private static String UserAgent;
    private static int DeviceWidth;
    //版本CODE
    private static final String AD_CODE = "VIDEO/android";

    public static String getLanguage(){
        if (LANGUAGE==null){
            try {
                LANGUAGE = Locale.getDefault().toString();
            } catch (Exception e){

            }
            if (TextUtils.isEmpty(LANGUAGE)) LANGUAGE = "ZH_CN";
        }
        return LANGUAGE;
    }

    public static String getVersionNum() {
        if (VersionNum==null) {
            VersionNum = getVersion(BaseApplication.getInstance());
        }
        return VersionNum;
    }
    public static String getVersionCodeStr() {
        if (VersionCodeStr==null) {
            VersionCodeStr = getVersionCode(BaseApplication.getInstance());
        }
        return VersionCodeStr;
    }

    public static String getUUID() {
        if (UUID==null) {
            UUID = new DeviceUuidFactory(BaseApplication.getInstance()).getUuid();
        }
        return UUID;
    }

    public static String getChannelName(){
        if (ChannelName==null){
            try {
                ApplicationInfo appInfo = BaseApplication.getInstance().getPackageManager().getApplicationInfo(
                        BaseApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
                ChannelName =appInfo.metaData.get("JPUSH_CHANNEL")+"";
            } catch (Exception e){
                e.printStackTrace();
                ChannelName = "2000";
            }
        }
        return ChannelName;
    }

    public static int getDeviceWidth(Context context) {
        if (DeviceWidth==0) {
            if (context instanceof Activity) {
                DisplayMetrics dm = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
                DeviceWidth = dm.widthPixels;
            }
        }
        return DeviceWidth;
    }

    public static String getUserAgent() {
        if (TextUtils.isEmpty(UserAgent)) {
            UserAgent = ";device_id:" + getUUID() + ";"+AD_CODE+"_" + getVersionCodeStr();
        }
        return UserAgent;
    }

    private static String getVersion(Context context) {
        String version = "1.0.0";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
    private static String getVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode+"";
    }
}
