package com.fuyou.play.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Create by 2018/3/27
 *
 * @author wwei
 */
public class NetStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        /*if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            // 获取网络连接管理器
            ConnectivityManager connectivityManager
                = (ConnectivityManager) MicroGateClient.getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取当前网络状态信息
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();

            if (info != null && info.isAvailable() && MicroGateClient.getInstance().isMinerLiving()) {
                Logger.t("WsManager").d("监听到可用网络切换,调用重连方法");
                WsManager.getInstance().reconnect();//wify 4g切换重连websocket
            }

        }*/
    }
}
