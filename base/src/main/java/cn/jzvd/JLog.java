package cn.jzvd;

import android.util.Log;

import com.yyspbfq.filmplay.BuildConfig;

public class JLog {

    private static boolean DEBUG_MODEL = !BuildConfig.DEBUG;

    public static void setDebug(boolean debugModel) {
        DEBUG_MODEL = debugModel;
    }

    public static void d (String tag, String msg) {
        if (msg == null) return;
        if (!DEBUG_MODEL) {
            Log.d(tag, msg);
        }
    }

    public static void e (String tag, String msg) {
        if (msg == null) return;
        if (!DEBUG_MODEL) {
            Log.e(tag, msg);
        }
    }

    public static void i (String tag, String msg) {
        if (msg == null) return;
        if (!DEBUG_MODEL) {
            Log.i(tag, msg);
        }
    }

}
