package com.fuyou.play.util;


import android.os.Handler;
import android.os.Looper;

public class UiHandler {

    private static Handler sUiHandler;

    public static void runOnUiThread(Runnable runnable) {
        initUIHandlerIfNeed();
        sUiHandler.post(runnable);
    }

    public static void runOnUiThreadDelayed(Runnable runnable, long delayMills) {
        initUIHandlerIfNeed();
        sUiHandler.postDelayed(runnable, delayMills);
    }

    private static void initUIHandlerIfNeed() {
        if (sUiHandler == null) {
            synchronized (UiHandler.class) {
                if (sUiHandler == null) {
                    sUiHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
    }
}
