package com.yyspbfq.filmplay.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.utils.tools.Md5Util;

import java.io.File;
import java.text.DecimalFormat;
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

    //获取语言
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

    //获取版本号(对外方法)
    public static String getVersionNum() {
        if (VersionNum==null) {
            VersionNum = getVersion(BaseApplication.getInstance());
        }
        return VersionNum;
    }

    //获取版本CODE(对外方法)
    public static String getVersionCodeStr() {
        if (VersionCodeStr==null) {
            VersionCodeStr = getVersionCode(BaseApplication.getInstance());
        }
        return VersionCodeStr;
    }

    //获取唯一标识
    public static String getUUID() {
        if (UUID==null) {
            UUID = new DeviceUuidFactory(BaseApplication.getInstance()).getUuid();
        }
        return UUID;
    }

    //获取渠道名称
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

    //获取屏幕宽度
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

    //拼接UserAgent
    public static String getUserAgent() {
        if (TextUtils.isEmpty(UserAgent)) {
            UserAgent = ";device_id:" + getUUID() + ";"+AD_CODE+"_" + getVersionCodeStr();
        }
        return UserAgent;
    }

    //获取版本号
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

    //获取版本CODE
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

    //判断SD卡是否可用
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    //获取SD卡剩余空间
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getFreeSpace() {
        if (!isSDCardEnable()) return "SD卡不能使用";
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize, availableBlocks;
            availableBlocks = stat.getAvailableBlocksLong();
            blockSize = stat.getBlockSizeLong();
            double d = availableBlocks * blockSize / 1024L;
            d = d/1024;
            d = d/1024;
            DecimalFormat df = new DecimalFormat("#.00");
            String result = df.format(d) + "GB";
            if (result.contains(".00")) {
                result = result.replace(".00", "");
            } else {
                if (result.endsWith(".0GB")) {
                    result = result.replace(".0GB", "GB");
                }
            }
            return result;
        } catch (Exception e) {

        }
        return "SD卡加载出错";
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean IsNetWorkEnable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 比较指定apk包名
     *
     * @param context
     * @param file
     * @return
     */
    public static boolean comparePackage(Context context, File file) {
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            PackageInfo pkgInfo = pm.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES);
            if (pkgInfo==null) return true;
            String name = pkgInfo.applicationInfo.packageName;
            if (name.equals(context.getPackageName())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            BLog.e(e);
        }
        return true;
    }

    /**
     * 比较指定apkMD5值
     *
     * @param file
     * @return
     */
    public static boolean compareMd5(File file, String md5) {
        if(file == null) return false;
        if(TextUtils.isEmpty(md5))return true;//升级文件没有sign,不做md5检查直接升级
        String md5New = Md5Util.getFileMD5String(file);
        if(TextUtils.isEmpty(md5New)) return false;
        if (md5New.equalsIgnoreCase(md5)) {
            return true;
        } else {
            return false;
        }
    }

}
