package com.fuyou.play.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.biz.RequestPermissionsBiz;
import com.fuyou.play.biz.TakePhotoBiz;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.SelectableRoundedImageView;
import com.fuyou.play.widget.alertview.AlertView;
import com.fuyou.play.widget.alertview.OnDismissListener;
import com.fuyou.play.widget.alertview.OnItemClickListener;

import java.io.File;

/**
 * Created by Administrator on 2018/4/27 0027.
 *
 * 资料设置页面
 *
 * @author wwei
 */
public class InformationSettingActivity extends BaseActivity implements View.OnClickListener{

    //昵称输入控件
    private EditText et;
    //性别显示控件
    private TextView tv_sex;
    //头像控件
    private SelectableRoundedImageView iv;
    private RequestPermissionsBiz permissionsBiz;
    private TakePhotoBiz photoBiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_setting);
        initStatusBar(findViewById(R.id.status_bar));
        initViews();
    }

    private void initViews(){
        et = (EditText) findViewById(R.id.et_username);
        iv = (SelectableRoundedImageView) findViewById(R.id.sriv);
        tv_sex = (TextView) findViewById(R.id.tv_sex);

        iv.setOnClickListener(this);
        tv_sex.setOnClickListener(this);
        findViewById(R.id.tv_jump).setOnClickListener(this);
        findViewById(R.id.tv_complete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sriv:
                checkPermissions();
                break;
            case R.id.tv_sex:
                showSelectSex();
                break;
            case R.id.tv_jump:
                startActivity(MainActivity.class);
                break;
            case R.id.tv_complete:
                startActivity(MainActivity.class);
                break;
        }
    }

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private void checkPermissions(){
        int REQUEST_CODE = 322;
        if (permissionsBiz==null) {
            permissionsBiz = new RequestPermissionsBiz(this, permissions, new RequestPermissionsBiz.RequestPermissionsListener() {
                @Override
                public void RequestComplete(boolean isOk) {
                    LogCustom.show("isOK:"+isOk);
                    if (isOk) {
                        toTakePhoto();
                    }
                }
            }, REQUEST_CODE);
        }
        permissionsBiz.toCheckPermission();
    }

    private void toTakePhoto(){
        if (photoBiz==null){
            photoBiz = new TakePhotoBiz(this, new TakePhotoBiz.TakePhotoListener() {
                @Override
                public void onTakePhotoListener(Uri uri, Bitmap bmp, File resultFile) {
                    iv.setImageURI(uri);
                }
            }, true);
        }
        photoBiz.takePhoto();
    }

    private boolean changed = false;
    private String sex;
    //选择性别
    private void showSelectSex() {
        try {
            new AlertView("Select Gender", null, "Cancel", null,
                    new String[]{getString(R.string.gender_man),getString(R.string.gender_women), getString(R.string.gender_default)},
                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            tv_sex.setText(getString(R.string.gender_man));
                            sex = "2";
                            changed = true;
                            break;
                        case 1:
                            tv_sex.setText(getString(R.string.gender_women));
                            sex = "1";
                            changed = true;
                            break;
                        case 2:
                            tv_sex.setText(getString(R.string.gender_default));
                            sex = "0";
                            changed = true;
                            break;
                    }
                }
            }).setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(Object o) {

                }
            }).setCancelable(true).show();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "InformationSettingActivity");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoBiz.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsBiz.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
