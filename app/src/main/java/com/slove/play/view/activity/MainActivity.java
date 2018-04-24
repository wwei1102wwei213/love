package com.slove.play.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.slove.play.R;
import com.slove.play.biz.http.HttpFlag;
import com.slove.play.biz.http.HttpRepListener;

import java.util.HashMap;
import java.util.Map;

import static com.slove.play.util.Const.TAG_LOG;

public class MainActivity extends AppCompatActivity implements HttpRepListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Factory.getHttpRespBiz(this, HttpFlag.LOGIN, null).post();
//        Factory.getHttpRespBiz(this, HttpFlag.REGISTER, null).post();
//        Factory.getHttpRespBiz(this, HttpFlag.USER_DETAIL, null).post();

    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, String> map = new HashMap<>();
        if (HttpFlag.LOGIN == flag) {
            map.put("username", "test");
            map.put("password", "123456");
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
}
