package com.yyspbfq.filmplay.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wei.wlib.util.WLibTools;
import com.yyspbfq.filmplay.ui.activity.MainActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.Const;


public class IPushHandleActivity extends AppCompatActivity {

    private final String TAG = "IPushHandleActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            String msg = getIntent().getStringExtra("jpush_custom_message");
            Intent intentAct;
            if (WLibTools.isAppAlive(this, getPackageName())) {
                intentAct = new Intent(this, MainActivity.class);
                intentAct.putExtra(Const.HANDLE_MSG_JPUSH, msg);
            } else {
                //如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
                //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入
                intentAct = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intentAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intentAct.putExtra(Const.HANDLE_MSG_JPUSH, msg);
            }
            startActivity(intentAct);
        } catch (Exception e){
            BLog.e(e);
        }
        finish();
    }
}
