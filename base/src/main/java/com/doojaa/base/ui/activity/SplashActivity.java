package com.doojaa.base.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.doojaa.base.R;
import com.doojaa.base.ui.BaseActivity;


/**
 * Created by Administrator on 2017/8/30 0030.
 *
 * 闪屏页
 *
 * @author wwei
 */
public class SplashActivity extends BaseActivity {

    private long appStartTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_splash);
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        boolean isFirstRun = sp.getBoolean(IS_FIRST_RUN, false);
        // 是否是第一次启动
        /*if (!isFirstRun) {
            SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
            spe.putBoolean(IS_FIRST_RUN, true);
            spe.apply();
            toNext(GuideScrollActivity.class);
        } else {
            Factory.getHttpRespBiz(this, HttpFlag.CHECK, null).post();
        }
        LogCustom.show(TextUtils.isEmpty(CommonUtils.getUUID())?"User UUID is empty":"User UUID:"+CommonUtils.getUUID());*/
//        DBHelper.getInstance().dropTable(ChatMessageBean.class);
    }

    private static final String IS_FIRST_RUN = "IsFirstRun";
    private void toNext(){
        /*if (TextUtils.isEmpty(UserDataUtil.getAccessToken(this))||TextUtils.isEmpty(UserDataUtil.getUserID(this))) {
            toNext(MobileLoginActivity.class);
        } else {
            toNext(MainActivity.class);
        }*/
    }

    private void toNext(Class<? extends Activity> mClass){
        startActivity(mClass);
        finish();
    }
}
