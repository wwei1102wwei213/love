package com.fuyou.play.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fuyou.play.R;
import com.fuyou.play.biz.RequestPermissionsBiz;
import com.fuyou.play.biz.TakePhotoBiz;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.util.ToolUtils;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.CircleImageView;
import com.fuyou.play.widget.alertview.AlertView;
import com.fuyou.play.widget.alertview.OnDismissListener;
import com.fuyou.play.widget.alertview.OnItemClickListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9 0009.
 */
public class InfoSettingActivity extends BaseActivity implements View.OnClickListener, HttpRepListener {

    private RequestPermissionsBiz permissionsBiz;
    private TakePhotoBiz photoBiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_setting);
        initStatusBar(findViewById(R.id.status_bar));
        initTitleViews();
        initViews();
        initData();
    }

    private void initTitleViews() {
        setBackViews(R.id.iv_back_base);
        TextView tv_right = findViewById(R.id.tv_right_base);
        Drawable drawable = getResources().getDrawable(R.mipmap.system_setting_icon);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_right.setCompoundDrawables(drawable, null, null, null);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SystemSettingActivity.class);
            }
        });
        tv_right.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_title_base)).setText(getString(R.string.title_info_setting));
    }

    private CircleImageView civ;
    private TextView tv_nickname, tv_gender, tv_birthday, tv_city, tv_record, tv_id;

    private void initViews() {
        civ = findViewById(R.id.civ);
        civ.setOnClickListener(this);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_gender = findViewById(R.id.tv_gender);
        tv_birthday = findViewById(R.id.tv_birthday);
        tv_city = findViewById(R.id.tv_city);
        tv_record = findViewById(R.id.tv_record);
        tv_id = findViewById(R.id.tv_id);
        setValues();

        findViewById(R.id.v_nickname).setOnClickListener(this);
        findViewById(R.id.v_gender).setOnClickListener(this);
        findViewById(R.id.v_birthday).setOnClickListener(this);
        findViewById(R.id.v_city).setOnClickListener(this);
        findViewById(R.id.v_record).setOnClickListener(this);
        findViewById(R.id.v_invitation_code).setOnClickListener(this);
        findViewById(R.id.v_menu).setOnClickListener(this);
        findViewById(R.id.v_news_center).setOnClickListener(this);
    }

    public static final String TAG = "InfoSettingActivity---" + android.os.Process.myPid();

    private void setValues() {
        LogCustom.show(UserDataUtil.getAvatar(this));

        if (!TextUtils.isEmpty(UserDataUtil.getAvatar(this))) {
            Glide.with(this).load(UserDataUtil.getAvatar(this))
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(civ);
        }
        tv_id.setText(UserDataUtil.getUserName(this));
        tv_nickname.setText(UserDataUtil.getUserName(this));
        String gender = UserDataUtil.getGender(this);
        String gender_text = "1".equals(gender) ? "男" : ("2".equals(gender) ? "女" : "未填写");
        LogCustom.e(TAG, "setValues--" + gender_text + " gender " + gender);
        tv_gender.setText(gender_text);
        tv_birthday.setText(ToolUtils.getTimeStringForTills(UserDataUtil.getBornTime(this)));
//        tv_city.setText(CommonUtils.getCityZh(this, UserDataUtil.getCity(this)));
    }

    private void initData() {
//        Factory.getHttpRespBiz(this, HttpFlag.USER_INFO, null).get();
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, Object> map = new HashMap<>();
        /*try {
            if (HttpFlag.EDIT_INFO == flag) {
                String[] args = (String[]) obj;
                map.put(args[0], args[1]);
            } else if (HttpFlag.USER_INFO == flag) {
                map.put("query_uid", UserDataUtil.getUserID(this));
            } else if (HttpFlag.GET_QN_TOKEN == flag) {

            } else if (HttpFlag.ACTIVE_INVITE_CODE == flag) {
                map.put("code", obj+"");
            } else if (HttpFlag.NEWS_CENTER == flag) {
                map.put("page_index", 0);
                map.put("page_size", 1);
                String last_time = UserDataUtil.getNewsLastTime(this);
                map.put("last_time", TextUtils.isEmpty(last_time)?"0":last_time);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, this.getClass().getSimpleName() + " getParamInfo");
        }*/
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {
        /*if (HttpFlag.EDIT_INFO == flag) {
            BaseBean bean = (BaseBean) response;
            if (bean.getCode() == 0) {
                String[] args = (String[]) obj;
                LogCustom.e(TAG, "args--" + args[0] + " " + args[1]);
                if ("gender".equals(args[0])) {
                    UserDataUtil.setGender(this, args[1]);
                } else if ("avatar".equals(args[0])) {
                    UserDataUtil.setAvatar(this, args[1]);
                } else if ("nick_name".equals(args[0])) {
                    UserDataUtil.setUserName(this, args[1]);
                } else if ("birthday".equals(args[0])) {
                    UserDataUtil.setBornTime(this, args[1]);
                } else if ("location".equals(args[0])) {
                    UserDataUtil.setCity(this, args[1]);
                }
            } else {
                showBaseError(bean);
            }
        } else if (HttpFlag.USER_INFO == flag) {
            LoginBaseBean bean = (LoginBaseBean) response;
            if (bean.getCode() == 0 && bean.getData() != null && bean.getData().getUser_info() != null) {
                UserDataUtil.saveUserData(this, bean.getData().getUser_info());
                tv_record.setText(bean.getData().getUser_info().getTest_count() + "");
                setValues();
            }
        } else if (HttpFlag.GET_QN_TOKEN == flag) {
            QNTokenBean bean = (QNTokenBean) response;
            if (bean.getCode() == 0) {
                if (bean.getData() != null) {
                    try {
                        File temp = PhotoUtils.scal(obj.toString(), PhotoUtils.SCAL_IMAGE_30);
                        LogCustom.show("uri2:" + obj.toString());
                        UploadImage(bean.getData().getHost(), bean.getData().getToken(), temp.getPath());
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionSend(e, "toActivity UploadImage");
                    }

                }
            } else {
                showBaseError(bean);
            }
        } else if (HttpFlag.ACTIVE_INVITE_CODE == flag) {
            InvitationResultBean bean = (InvitationResultBean) response;
            if (bean.getCode() == 0) {
                showToast("激活成功");
                UserDataUtil.setExchange(this, true);
                Factory.getHttpRespBiz(this, HttpFlag.NEWS_CENTER, null).get();
                if (bean.data != null && bean.data.img_url != null && !bean.data.img_url.trim().equals("")) {

                    final String final_img_url=bean.data.img_url.trim();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new InvitationCodeResultView(InfoSettingActivity.this, Gravity.CENTER, new InvitationCodeResultView.InvitationCodeResultViewCallBack() {
                                @Override
                                public void onCall(int code, String msg) {
                                    showToast("收下礼包");
                                }
                            },final_img_url).show();


                        }
                    }, 500);//为什么这里要延时?因为前一个dialog在dismiss的时候有退出动画大约需要300ms;不延时的话dialog弹不出来了
                } else {
                    showToast("激活失败");
                }

            } else {
                showBaseError(bean);
            }
        } else if (HttpFlag.NEWS_CENTER == flag) {
            NewsCenterBean bean = (NewsCenterBean) response;
            if (bean.getCode() == 0 && bean.getData()!=null && bean.getData().getList()!=null && bean.getData().getList().size()>0) {
                findViewById(R.id.v_red_point).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.v_red_point).setVisibility(View.GONE);
            }
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ:
                checkPermissions();
                break;
            case R.id.v_nickname:
                changeNickname();
                break;
            case R.id.v_gender:
                showSelectSex();
                break;
            case R.id.v_birthday:
                chooseBirthday();
                break;
            case R.id.v_city:

                break;
            case R.id.v_record:

                break;
            case R.id.v_invitation_code:

                break;
            case R.id.v_menu:
                break;
            case R.id.v_news_center:

                break;
        }
    }


    private void onInvitationCode() {

    }


    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private final int REQUEST_CODE = 322;

    private void checkPermissions() {
        if (permissionsBiz == null) {
            permissionsBiz = new RequestPermissionsBiz(this, permissions, new RequestPermissionsBiz.RequestPermissionsListener() {
                @Override
                public void RequestComplete(boolean isOk) {
                    if (isOk) {
                        toTakePhoto();
                    }
                }
            }, REQUEST_CODE);
        }
        permissionsBiz.toCheckPermission();
    }

    private void toTakePhoto() {
        if (photoBiz == null) {
            photoBiz = new TakePhotoBiz(this, new TakePhotoBiz.TakePhotoListener() {
                @Override
                public void onTakePhotoListener(Uri uri, Bitmap bmp, File resultFile) {
                    LogCustom.show("Uri1:" + (uri == null ? "null" : uri));
                    if (uri != null) {
                        civ.setImageURI(uri);
                        LogCustom.show("Uri1:" + (uri == null ? "null" : uri));
                        if (!TextUtils.isEmpty(resultFile.getPath())) {
//                            Factory.getHttpRespBiz(InfoSettingActivity.this, HttpFlag.GET_QN_TOKEN, resultFile.getPath()).get();
                        }
                    }
                }
            }, true);
        }
        photoBiz.takePhoto();
    }

    private void changeNickname() {

    }

    private void chooseBirthday() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            default:
                if (photoBiz != null) {
                    photoBiz.handleActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }


    private boolean changed = false;
    private String sex = "-2";

    //选择性别
    private void showSelectSex() {
        try {
            new AlertView(getString(R.string.gender_choose), null, getString(R.string.gender_cancel), null,
                    new String[]{getString(R.string.gender_man), getString(R.string.gender_women), getString(R.string.gender_default)},
                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    String[] params = new String[2];
                    params[0] = "gender";
                    switch (position) {
                        case 0:
                            tv_gender.setText(getString(R.string.gender_man));
                            if (!"1".equals(sex)) {
                                params[1] = "1";
                                changeSetting(params);
                            }
                            break;
                        case 1:
                            tv_gender.setText(getString(R.string.gender_women));
                            if (!"2".equals(sex)) {
                                params[1] = "2";
                                changeSetting(params);
                            }
                            break;
                        case 2:
                            tv_gender.setText(getString(R.string.gender_default));
                            if (!"0".equals(sex)) {
                                params[1] = "0";
                                changeSetting(params);
                            }
                            break;
                    }
                }
            }).setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(Object o) {
                }
            }).setCancelable(true).show();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "InformationSettingActivity");
        }
    }

    private void changeSetting(String[] params) {
//        Factory.getHttpRespBiz(this, HttpFlag.EDIT_INFO, params).post();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsBiz != null)
            permissionsBiz.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    private UploadManager uploadManager;

    public void UploadImage(final String domain, String imageToken, String imagePath) {
        if (TextUtils.isEmpty(domain) && TextUtils.isEmpty(imageToken) && TextUtils.isEmpty(imagePath)) {
            throw new RuntimeException("upload parameter is null!");
        }
        File imageFile = new File(imagePath);
        /*if (this.uploadManager == null) {
            Configuration config = new Configuration.Builder().zone(FixedZone.zone2).build();
            this.uploadManager = new UploadManager(config);
        }
        final String key = "images/avatar/" + UserDataUtil.getUserID(this) + "/" + System.currentTimeMillis() + "/" + imageFile.getName();

        this.uploadManager.put(imageFile, key, imageToken, new UpCompletionHandler() {
            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {

                Log.e("uploadImage", "---responseInfo.isOK---" + responseInfo.isOK());
                Log.e("uploadImage", "---responseInfo.isOK---" + responseInfo.error);
                Log.e("uploadImage", "---responseInfo.isOK---" + responseInfo.toString());

                if (responseInfo.isOK()) {
                    try {
                        String key = (String) jsonObject.get("key");
                        String imageUrl = domain + "/" + key;
                        LogCustom.show("uploadImage" + imageUrl);
                        String[] params = new String[2];
                        params[0] = "avatar";
                        params[1] = imageUrl;
                        changeSetting(params);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (responseInfo.statusCode == -6) {
                        showToast("当前图片已被删除，请选择其他图片上传。");
                    }
                }
            }
        }, null);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Factory.getHttpRespBiz(this, HttpFlag.NEWS_CENTER, null).get();
    }
}
