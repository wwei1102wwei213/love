package com.yyspbfq.filmplay;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.danikula.videocache.HttpProxyCacheServer;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.ta.TAApplication;
import com.wei.wlib.WLibManager;
import com.wei.wlib.util.WLibLog;
import com.yyspbfq.filmplay.biz.http.HttpInterceptor;

import cn.jpush.android.api.JPushInterface;

public class BaseApplication extends TAApplication{

    private static BaseApplication instance;
    protected Handler handler;
    public static int mainThreadId;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mainThreadId = android.os.Process.myTid();
        //初始化日志工具
        initLogger();
        //网络初始化
        WLibManager.getInstance().initOkHttp(this, true, new HttpInterceptor(this));
        //设置网络请求日志
        WLibLog.setIsDebug(BuildConfig.DEBUG);
        // 设置推广日志,发布时请关闭日志
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        // 初始化 JPush
        JPushInterface.init(this);
    }

    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //处理4.4.4手机报错java.lang.ClassNotFoundException: Didn't find class "android.support.v4.content.FileProvider"
        MultiDex.install(this);
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder().showThreadInfo(true).tag("WWEI").build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        BaseApplication app = (BaseApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

    /**
     * 处理修改系统字体大小
     */
    private void intResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public Handler getHandler() {
        if (handler==null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }
}
