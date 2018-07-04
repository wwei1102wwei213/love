package com.fuyou.play.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.fuyou.play.R;
import com.fuyou.play.biz.Factory;
import com.fuyou.play.biz.http.HttpFlag;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.CircleImageView;
import com.fuyou.play.widget.custom.MyScrollerView;

import java.util.HashMap;
import java.util.Map;

import static com.fuyou.play.util.Const.TAG_LOG;

public class MainActivity extends BaseActivity implements HttpRepListener, View.OnClickListener{

    private MyScrollerView msv;
    private CircleImageView civ_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStatusBar(findViewById(R.id.status_bar));
        initViews();

        Factory.getHttpRespBiz(this, HttpFlag.LOGIN, null).post();
//        Factory.getHttpRespBiz(this, HttpFlag.REGISTER, null).post();
//        Factory.getHttpRespBiz(this, HttpFlag.USER_DETAIL, null).post();

    }

    private void initViews(){
        findViewById(R.id.iv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv:
                startActivity(InfoSettingActivity.class);
                break;
        }
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, String> map = new HashMap<>();
        if (HttpFlag.LOGIN == flag) {
            map.put("PhoneNum", "13723701704");
            map.put("Code", "123456");
        } else if (HttpFlag.REGISTER == flag) {
            map.put("username", "goodluck");
            map.put("password", "123456");
            map.put("gender", "0");
        } else if (HttpFlag.USER_DETAIL == flag) {
            map.put("InfoID", "100003");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {
        Log.d(TAG_LOG, "toActivity,"+response);
    }

    @Override
    public void showLoading(int flag, Object obj) {

    }

    @Override
    public void hideLoading(int flag, Object obj) {

    }

    @Override
    public void showError(int flag, Object obj, int errorType) {
        Log.d(TAG_LOG, "showError");
    }

    private long exitSpaceTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitSpaceTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitSpaceTime = System.currentTimeMillis();
            } else {
                System.gc();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
