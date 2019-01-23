package com.yyspbfq.filmplay.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.wei.wlib.util.WLibTools;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.ui.activity.MainActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 *
 * 2018/10/12修改 接收自定义消息并处理
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle == null) return;
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.d(TAG, "[MyReceiver] extra:" + extra);
            Log.d(TAG, "[MyReceiver] package:" + context.getPackageName());
            if (WLibTools.isAppAlive(context, context.getPackageName())) {
                Log.d(TAG, "[MyReceiver] isAppAlive true");
                Intent intentMain = new Intent(context, MainActivity.class);
                intentMain.putExtra(Const.HANDLE_MSG_JPUSH, extra);
                intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentMain);
            } else {
                Log.d(TAG, "[MyReceiver] isAppAlive false");
//                PackageUtils.startAppByPackageName(context, context.getPackageName());
                //如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
                //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                launchIntent.putExtra(Const.HANDLE_MSG_JPUSH, extra);
                context.startActivity(launchIntent);
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    /*key:cn.jpush.android.EXTRA, value: [bid - 10]
    key:cn.jpush.android.EXTRA, value: [window - 1]
    key:cn.jpush.android.TITLE, value:
    key:cn.jpush.android.MESSAGE, value:自定义消息测试
    key:cn.jpush.android.CONTENT_TYPE, value:
    key:cn.jpush.android.APPKEY, value:6c9f3aee464ffde093f539cd
    key:cn.jpush.android.MSG_ID, value:3838000502*/
    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        try {
            Intent intentAct = new Intent();
            intentAct.setClass(context, IPushHandleActivity.class);
            intentAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentAct.putExtra("jpush_custom_message", bundle.getString(JPushInterface.EXTRA_EXTRA));

            String title = TextUtils.isEmpty(bundle.getString("cn.jpush.android.TITLE"))?
                    context.getResources().getString(R.string.app_name):bundle.getString("cn.jpush.android.TITLE");
            String text = TextUtils.isEmpty(bundle.getString("cn.jpush.android.MESSAGE"))?
                    "默认内容":bundle.getString("cn.jpush.android.MESSAGE");

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.ic_launcher);
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setAutoCancel(true);
            Notification mNotification = builder.build();
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intentAct, PendingIntent.FLAG_UPDATE_CURRENT);//不是Intent
//        mNotification.flags = Notification.FLAG_NO_CLEAR;// 永久在通知栏里
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;
            //使用自定义下拉视图时，不需要再调用setLatestEventInfo()方法，但是必须定义contentIntent
            mNotification.contentIntent = contentIntent;
            mNotificationManager.notify(103, mNotification);
        }catch (Exception e){
            BLog.e("自定义消息处理", e);
        }
    }
}
