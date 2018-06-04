package com.fuyou.play.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fuyou.play.R;
import com.fuyou.play.view.BaseActivity;


/**
 * Created by Administrator on 2018/4/27 0027.
 *
 * @author wwei
 */
public class LoginActivity extends BaseActivity {

    private Context mContext;
    //微信QQ登陆控件
    private View v_wx,v_qq;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initStatusBar(findViewById(R.id.status_bar));
        mContext = this;
        initViews();
        initData();
    }

    private void initViews(){
        v_wx = findViewById(R.id.v_wx_login);
        v_qq = findViewById(R.id.v_qq_login);
        dialog = new ProgressDialog(this);
        v_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toThreeLogin(0);
            }
        });
        v_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toThreeLogin(1);
            }
        });
    }

    private void initData(){

    }

    private void toThreeLogin(int type) {
        /*if (type==0){//微信
            UMShareAPI.get(mContext).doOauthVerify(this, SHARE_MEDIA.WEIXIN.toSnsPlatform().mPlatform, authListener);
        } else if (type==1){
            UMShareAPI.get(mContext).doOauthVerify(this, SHARE_MEDIA.QQ.toSnsPlatform().mPlatform, authListener);
        }*/
    }

    /*UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SocializeUtils.safeCloseDialog(dialog);
            for (String key:data.keySet()) {
                LogCustom.d("UM_TEST", key + ":"+data.get(key) );
            }
            Toast.makeText(mContext, "成功了", Toast.LENGTH_LONG).show();
            try {
                UMShareAPI.get(mContext).getPlatformInfo(LoginActivity.this, platform, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {}
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        StringBuilder sb = new StringBuilder();
                        for (String key : map.keySet()) {
                            LogCustom.d("UM_TEST", key + ":"+map.get(key) );
                        }
                    }
                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Toast.makeText(mContext, "错误", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Toast.makeText(mContext, "用户已取消", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e){
                LogCustom.d("UM_TEST", e.getMessage() );
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
        }
    };*/
}
