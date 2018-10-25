package com.fuyou.play.view.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fuyou.play.R;
import com.fuyou.play.bean.chat.ChatMessageBean;
import com.fuyou.play.bean.login.CheckBaseBean;
import com.fuyou.play.biz.Factory;
import com.fuyou.play.biz.db.DBHelper;
import com.fuyou.play.biz.http.HttpFlag;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.biz.xmpp.XmppConnection;
import com.fuyou.play.util.CommonUtils;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseActivity;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/8/30 0030.
 *
 * 闪屏页
 *
 * @author wwei
 */
public class SplashActivity extends BaseActivity implements HttpRepListener{

    private long appStartTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        // 打开连接
        try {
            XmppConnection.getInstance().connectChatServer();
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
        boolean isFirstRun = sp.getBoolean(IS_FIRST_RUN, false);
        // 是否是第一次启动
        if (!isFirstRun) {
            SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
            spe.putBoolean(IS_FIRST_RUN, true);
            spe.apply();
            toNext(GuideScrollActivity.class);
        } else {
            Factory.getHttpRespBiz(this, HttpFlag.CHECK, null).post();
        }
        LogCustom.show(TextUtils.isEmpty(CommonUtils.getUUID())?"User UUID is empty":"User UUID:"+CommonUtils.getUUID());
//        DBHelper.getInstance().dropTable(ChatMessageBean.class);
    }

    private static final String IS_FIRST_RUN = "IsFirstRun";
    private void toNext(){
        if (TextUtils.isEmpty(UserDataUtil.getAccessToken(this))||TextUtils.isEmpty(UserDataUtil.getUserID(this))) {
            toNext(MobileLoginActivity.class);
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
        }, 500);
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (flag== HttpFlag.CHECK) {
            map.put("VersionName", CommonUtils.getVersionNum());
            map.put("UserID", UserDataUtil.getUserID(this));
            map.put("Token", UserDataUtil.getAccessToken(this));
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {
        if (flag == HttpFlag.CHECK) {
            CheckBaseBean bean = (CheckBaseBean) response;
            if (bean.getStatus()==0&&bean.getCheck()==1) {
                UserDataUtil.clearUserData(this);
                toNext(MobileLoginActivity.class);
            } else {
                toNext();
            }
        }
    }

    @Override
    public void showLoading(int flag, Object obj) {

    }

    @Override
    public void hideLoading(int flag, Object obj) {

    }

    @Override
    public void showError(int flag, Object obj, int errorType) {
        LogCustom.show("err");
        //测试阶段这么写，上线删掉
        if (flag == HttpFlag.CHECK) {
            toNext();
        }
    }




}
