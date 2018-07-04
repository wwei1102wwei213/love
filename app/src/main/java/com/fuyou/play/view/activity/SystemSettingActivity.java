package com.fuyou.play.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.biz.RequestPermissionsBiz;
import com.fuyou.play.biz.UpdateAppBiz;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.util.CommonUtils;
import com.fuyou.play.view.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9 0009.
 * 系统设置
 * @author wwei
 */
public class SystemSettingActivity extends BaseActivity implements HttpRepListener {

    private UpdateAppBiz mUpdateAppBiz;
    private RequestPermissionsBiz mUpdateRequestPermissionsBiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        initStatusBar(findViewById(R.id.status_bar));
        initTitleViews();
        initViews();
    }

    private void initTitleViews() {
//        setImmersiveStatusBar(R.mipmap.base_status_bar_bg);
        setBackViews(R.id.iv_back_base);
        ((TextView) findViewById(R.id.tv_title_base)).setText(getString(R.string.title_system_setting));
    }

    private void initViews() {
        ((TextView) findViewById(R.id.tv_version)).setText(CommonUtils.getVersionNum());
        findViewById(R.id.tv_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginOut();
            }
        });
        findViewById(R.id.v_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdateAppBiz = new UpdateAppBiz(SystemSettingActivity.this, false);
            }
        });
        /*findViewById(R.id.v_secret).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemSettingActivity.this, ShowHtmlActivity.class);
                intent.putExtra("Html_Page_Title", 1);
                startActivity(intent);
            }
        });*/
        /*findViewById(R.id.v_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FeedbackActivity.class);
            }
        });*/
    }

    private void toLoginOut(){
        /*final ConfirmDialogBuilder builder = new ConfirmDialogBuilder(this, Gravity.CENTER);
        builder.setMsg(null);
        builder.setTitle("是否退出账号?");
        builder.setConfirm("确认退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.tv_sign_out).setClickable(false);
                Factory.getHttpRespBiz(SystemSettingActivity.this, HttpFlag.SIGN_OUT, null).get();
            }
        });
        builder.setCancel("暂时不了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        builder.show();*/
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {
        /*if (flag == HttpFlag.SIGN_OUT){
            MobclickAgent.onProfileSignOff();
            signOut();
        }*/
    }

    @Override
    public void showLoading(int flag, Object obj) {

    }

    @Override
    public void hideLoading(int flag, Object obj) {

    }

    @Override
    public void showError(int flag, Object obj, int errorType) {
        /*if (flag == HttpFlag.SIGN_OUT){
            signOut();
        }*/
    }

    private final int UPDATE_APP_CODE = 325;
    private String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public void checkPermissionUpdateApp(){
        mUpdateRequestPermissionsBiz = new RequestPermissionsBiz(this, permissions, new RequestPermissionsBiz.RequestPermissionsListener() {
            @Override
            public void RequestComplete(boolean isOk) {
                if (isOk&&mUpdateAppBiz!=null) {
                    mUpdateAppBiz.downloadApk();
                }
            }
        }, UPDATE_APP_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mUpdateRequestPermissionsBiz!=null)
            mUpdateRequestPermissionsBiz.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
