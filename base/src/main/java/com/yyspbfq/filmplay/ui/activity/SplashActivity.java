package com.yyspbfq.filmplay.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;

import java.lang.ref.WeakReference;


/**
 * Created by Administrator on 2017/8/30 0030.
 *
 * 闪屏页
 *
 * @author wwei
 */
public class SplashActivity extends BaseActivity {

    private long appStartTime;
    private MyHandler mHandler;
    private TextView tv;
    private ImageView iv;
    final String thumb = "https://fuyouoss.oss-cn-shenzhen.aliyuncs.com/10008post153546930123410615162.png";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setStatusBarTranslucent();
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
//        Glide.with(this).load(thumb).preload();
        mHandler = new MyHandler(this);
        mHandler.sendEmptyMessageDelayed(-2, 0);
    }

    private static final String IS_FIRST_RUN = "IsFirstRun";
    private void toNext(){
        String url = "https://www.baidu.com";
        ImageView iv = (ImageView) findViewById(R.id.iv_splash);
        Glide.with(this)
                .load(thumb)
                .crossFade()
                .into(iv);
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(900);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        iv.setAnimation(animation);
        animation.start();
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNext(MainActivity.class);
            }
        });
        if (!TextUtils.isEmpty(url)) {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAdvert();
                }
            });
        }
        sendMsg();
    }

    private void sendMsg() {
        if (mHandler!=null) mHandler.sendEmptyMessageDelayed(3, 900);
    }

    private void handleMsg(int what) {
        if (isJump) return;
        if (what>=0) {
            tv.setText(String.format(getString(R.string.format_splash_skip), what));
            if (tv.getVisibility()!=View.VISIBLE) tv.setVisibility(View.VISIBLE);
            if (mHandler!=null) mHandler.sendEmptyMessageDelayed(what-1, 1000);
        } else if (what==-1) {
            toNext(MainActivity.class);
        } else {
            toNext();
        }
    }

    private void clickAdvert() {
        try {
            clearHandler();
//            Factory.resp(this, HttpFlag.FLAG_INVITE_CLICK_ADS, null, null).get(null);
            toNext(MainActivity.class);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private boolean isJump = false;
    private void clearHandler() {
        isJump = true;
        try {
            if (mHandler!=null) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void toNext(Class<? extends Activity> mClass){
        startActivity(mClass);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        clearHandler();
        super.onDestroy();
    }

    private static class MyHandler extends Handler {
        private WeakReference<SplashActivity> weak;
        private MyHandler(SplashActivity activity) {
            weak = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weak!=null&&weak.get()!=null) {
                weak.get().handleMsg(msg.what);
            }
        }
    }
 }
