package com.fuyou.play.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.fuyou.play.LApplication;

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
            VersionNum = getVersion(LApplication.getInstance());
        }
        return VersionNum;
    }
    public static String getVersionCodeStr() {
        if (VersionCodeStr==null) {
            VersionCodeStr = getVersionCode(LApplication.getInstance());
        }
        return VersionCodeStr;
    }

    public static String getUUID() {
        if (UUID==null) {
            UUID = new DeviceUuidFactory(LApplication.getInstance()).getDeviceUuid().toString();
        }
        return UUID;
    }

    public static String getChannelName(){
        if (ChannelName==null){
            try {
                ApplicationInfo appInfo = LApplication.getInstance().getPackageManager().getApplicationInfo(LApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
                ChannelName =appInfo.metaData.get("COM_MICRO_MILU_CHANNEL_NAME")+"";
            } catch (Exception e){
                e.printStackTrace();
                ChannelName = "2000";
            }
        }
        return ChannelName;
    }

    private static String getVersion(Context context) {
        String version = "1.0";
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
