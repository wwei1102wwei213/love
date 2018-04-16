package com.slove.play;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Administrator on 2018/3/30 0030.
 *
 * @author wwei
 */
public class LApplication extends Application{


    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }


}
