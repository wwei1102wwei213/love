package com.slove.play.view.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.micro.milu.MLApplication;
import com.micro.milu.R;
import com.micro.milu.util.CommonUtils;
import com.micro.milu.util.LogCustom;
import com.micro.milu.util.sp.UserDataUtil;
import com.micro.milu.view.BaseActivity;

/**
 * Created by Administrator on 2017/8/30 0030.
 *
 * 闪屏页
 *
 * @author wwei
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!TextUtils.isEmpty(UserDataUtil.getAccessToken(this))){
            MLApplication.getInstance().setInfoToken(UserDataUtil.getAccessToken(this));
            LogCustom.show("User Token:"+ MLApplication.getInstance().getInfoToken());
        }
        LogCustom.show(TextUtils.isEmpty(CommonUtils.getUUID())?"User UUID is empty":"User UUID:"+CommonUtils.getUUID());
        toNext();
    }

    private static final String IS_FIRST_RUN = "IsFirstRun";
    private void toNext(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        boolean isFirstRun = sp.getBoolean(IS_FIRST_RUN, true);
        // 是否是第一次启动
        if (isFirstRun) {
            SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
            spe.putBoolean(IS_FIRST_RUN, false);
            spe.apply();
            toNext(GuideScrollActivity.class);
        } else {
            toNext(MainActivity.class);
        }
    }

    private void toNext(Class<? extends Activity> mClass){
        startActivity(mClass);
        overridePendingTransition(R.anim.activity_in_from_right,R.anim.activity_out_to_left);
        findViewById(R.id.fl).postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2400);
    }

}
