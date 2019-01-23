package com.yyspbfq.filmplay.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.Toast;

import com.wei.wlib.http.persistentcookiejar.PersistentCookieJar;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;


/**
 * 系统工具类
 * Created by tankai on 2015/11/4.
 */
public class SystemUtils {

    private static final String TAG = "SystemUtils";

    private static String appChannel;//剪切板或apk包中渠道号
    private static String appActChannel;//答题渠道号

    /**
     * 获取本应用的版本号
     *
     * @param
     * @return
     */
    public static String getVersionName() {
        /*String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }*/
        return CommonUtils.getVersionNum();
    }

    /**
     * 获取本应用的VersionCode
     *
     * @return
     */
    public static int getVersionCode() {
        /*int versioncode = -1;
        try {
            versioncode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/
        return Integer.parseInt(CommonUtils.getVersionCodeStr());
    }

    /**
     * 创建快捷方式
     *
     * @param mContext
     */
    public static void createShortCut(Context mContext) {
        try {
            //有待改进
            ComponentName component = new ComponentName(mContext, "com.kkmfxs.reade.activity.SplashActivity");
            Intent intent = new Intent(Intent.ACTION_MAIN).setComponent(component);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            Intent shortCutIntent = new Intent();
            Intent.ShortcutIconResource resource = Intent.ShortcutIconResource.fromContext(mContext, R.mipmap.ic_launcher);
            shortCutIntent.putExtra("duplicate", false);
            shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, mContext.getString(R.string.app_name));
            shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, resource);
            shortCutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

            mContext.sendBroadcast(shortCutIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取本机渠道标识
     *
     * @param context
     * @param prefix
     * @return
     */
    public static String getChannel(Context context, String prefix) {
        if (TextUtils.isEmpty(appChannel)) {
            appChannel = CommonUtils.getChannelName();
        }
        return appChannel;

    }

    /**
     * 复制文字到剪切板
     *
     * @param context
     * @param content
     */
    public static void copyTextToClip(Context context, String content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", content);
        clipboard.setPrimaryClip(clip);
    }

    /**
     * 获取答题渠道号
     *
     * @param context
     * @return
     */
    public static String getChannelActivity(Context context) {
        //kksx_activity_code:43453453
        if (TextUtils.isEmpty(appActChannel)) {
            appActChannel = "";
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
                ClipData clipData = clipboardManager.getPrimaryClip();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        if (item != null && !TextUtils.isEmpty(item.getText())) {
                            String text = item.getText().toString();
                            //todo
                            if (!TextUtils.isEmpty(text) && text.contains("kksx_activity_code:")) {
                                String[] strings = text.split(":");
                                if (strings.length == 3) {
                                    appActChannel = strings[2];
                                }
                            }
                        }
                    }
                }
            }
        }
        return appActChannel;
    }

    /**
     * 从剪切板获取邀请id
     *
     * @param context
     * @return
     */

    public static String getChannelFromClip(Context context) {
        //格式
        //kksx_invitation_code:123456
        String channel = "";
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager == null) return channel;
        if (!clipboardManager.hasPrimaryClip()) {
            return channel;
        }
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null) return channel;
        for (int i = 0; i < clipData.getItemCount(); i++) {
            ClipData.Item item = clipData.getItemAt(i);
            if (item != null && !TextUtils.isEmpty(item.getText())) {
                String text = item.getText().toString();
                //todo
                if (!TextUtils.isEmpty(text) && text.contains("kksx_invitation_code:")) {
                    String[] strings = text.split(":");
                    if (strings.length > 1) {
                        return strings[1].trim();
                    }
                }
            }
        }
        return channel;
    }

    /**
     * 清除cookies
     */
    public static void clearCookie(Context context) {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
    }

    /**
     * 同步cookies
     */
    public static void sychCookie(Context context, String url) {
        try {
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeAllCookie();
            CookieJar cookieJar = OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
            if (cookieJar instanceof PersistentCookieJar) {
                PersistentCookieJar persistentCookieJar = (PersistentCookieJar) cookieJar;
                List<Cookie> cookies = persistentCookieJar.getCookies();
                if (cookies==null||cookies.size()==0) {

                } else {
                    for (Cookie cookie : cookies) {
                        cookieManager.setCookie(url, cookie.toString());
                    }
                }
            }
            CookieSyncManager.getInstance().sync();
        } catch (Exception e){
            BLog.e(e, "sychCookie");
        }

    }

    public static void clearWebCache(Context context) {
        //清理Webview缓存数据库
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean fileIsExists(String path) {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File file = new File(path);
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWeixinAvilible(Context context) {
        try {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.tencent.mm")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * 跳转指定QQ号
     *
     * @param context
     * @param qq
     */
    public static void openQQ(Context context, String qq) {

        try {
            String url;
            if (TextUtils.isEmpty(qq)) {
                url = "mqqwpa://im/chat?chat_type=wpa&uin=613276288";
            } else {
                url = "mqqwpa://im/chat?chat_type=wpa&uin=".concat(qq);
            }
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "请安装QQ", Toast.LENGTH_SHORT).show();
        }

    }

    /****************
     *
     * 发起添加群流程。群号：交流群(528487060) 的 key 为： 6My5JaKoMGYz0Jaa_Rc8_tLe7FO0S6v-
     * 调用 joinQQGroup(6My5JaKoMGYz0Jaa_Rc8_tLe7FO0S6v-) 即可发起手Q客户端申请加群 交流群(528487060)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(context, "请安装QQ", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 打开微信
     *
     * @param context
     */
    public static void openWechat(Context context) {

        try {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "请安装微信", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 跳转应用市场
     */

    public static void goMarket(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "您没有安装应用市场", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                //app进程存在
                return true;
            }
        }
        //app还没启动
        return false;
    }

    /**
     * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
     *
     * @param view  控件view
     * @param event 焦点位置
     * @return 是否隐藏
     */
    public static void hideKeyboard(MotionEvent event, View view,
                                    Activity activity) {
        try {
            if (view != null && view instanceof EditText) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth() + DensityUtils.dp2px(activity, 30f), bootom = top + view.getHeight();//right加30dp,包含清空图标
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSoftKeyboard(Context mContext, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取json
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getAssetsJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
