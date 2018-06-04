package com.fuyou.play.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Tom on 2018/5/22.
 */

public class NetworkUtil {
    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        boolean isNetworkConnected = true;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            boolean isConnectedOrConnecting = ni != null && ni.isConnectedOrConnecting();
            boolean isAvailable = ni != null && ni.isAvailable();
            isNetworkConnected =isConnectedOrConnecting||isAvailable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isNetworkConnected;
    }
}
