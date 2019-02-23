package com.fuyou.play.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.biz.Factory;
import com.fuyou.play.biz.RequestPermissionsBiz;
import com.fuyou.play.biz.UpdateAppBiz;
import com.fuyou.play.biz.http.HttpFlag;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.util.CommonUtils;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.view.dailog.CustomDialog;

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

    private CustomDialog dialog;
    private void toLoginOut(){
        if (dialog==null){
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setTitle("提示").setMessage("是否退出账号?")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (dialog!=null) dialog.dismiss();
                            findViewById(R.id.tv_sign_out).setClickable(false);
                            Factory.getHttpRespBiz(SystemSettingActivity.this, HttpFlag.LOGIN_OUT, null).post();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (dialog!=null) dialog.dismiss();
                        }
                    });
            dialog = builder.create();
        }
        dialog.show();
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, Object> map = new HashMap<>();
        map.put("UserID", UserDataUtil.getUserID(this));
        map.put("Token", UserDataUtil.getAccessToken(this));
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {
        if (flag == HttpFlag.LOGIN_OUT){
            signOut();
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
        if (flag == HttpFlag.LOGIN_OUT){
            signOut();
        }
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
