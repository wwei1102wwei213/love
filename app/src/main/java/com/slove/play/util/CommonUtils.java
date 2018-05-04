package com.slove.play.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.slove.play.LApplication;

/**
 * Created by wwei on 2018/4/24 0024.
 *
 * 公共数据帮助类
 */

public class CommonUtils {

    private static String VersionNum;
    private static String UUID;
    private static String ChannelName;

    public static String getVersionNum() {
        if (VersionNum==null) {
            VersionNum = getVersion(LApplication.getInstance());
        }
        return VersionNum;
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
                ApplicationInfo appInfo = LApplication.getInstance().getPackageManager()
                        .getApplicationInfo(LApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
                ChannelName =appInfo.metaData.get("MC_CHANNEL_NAME")+"";
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
}
