package com.yyspbfq.filmplay.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wei.wlib.WLibManager;
import com.wei.wlib.glide.GlideCacheUtil;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.ui.WLibDialogHelper;
import com.wei.wlib.widget.CircleImageView;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.UpdateConfig;
import com.yyspbfq.filmplay.bean.UserInfo;
import com.yyspbfq.filmplay.biz.AppUpdateBiz;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.biz.login.UserHelper;
import com.yyspbfq.filmplay.db.DBHelper;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.ui.dialog.EditNickNameDialog;
import com.yyspbfq.filmplay.ui.dialog.SexDialog;
import com.yyspbfq.filmplay.ui.dialog.UpdateAppDialog;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.CropUtils;
import com.yyspbfq.filmplay.utils.SystemUtils;
import com.yyspbfq.filmplay.utils.UiUtils;
import com.yyspbfq.filmplay.utils.sp.SharePrefUtil;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SettingActivity extends BaseActivity implements View.OnClickListener, WLibHttpListener{

    private Dialog dialogLoading;
    private MyHandler mHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_setting);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("系统设置");
        initViews();
        initData();
    }

    private void initData() {
        mHandler = new MyHandler(this);
        new AppUpdateBiz(mHandler, MSG_WHAT_CHECK_UPDATE, MSG_WHAT_CHECK_UPDATE_ERROR, false).update();
    }

    private TextView tv_name, tv_sex, tv_version, tv_version_hint;
    private CircleImageView civ;
    private void initViews() {
        try {
            findViewById(R.id.v_lj).setOnClickListener(this);
            civ = findViewById(R.id.civ);
            tv_name = findViewById(R.id.tv_name);
            tv_sex = findViewById(R.id.tv_sex);
            tv_version = findViewById(R.id.tv_version);
            tv_version.setText("版本 V"+ CommonUtils.getVersionNum());
            tv_version_hint = findViewById(R.id.tv_version_hint);
            if (UserDataUtil.isLogin(this)) {
                findViewById(R.id.tv_exit).setVisibility(View.VISIBLE);
                findViewById(R.id.v_avatar).setOnClickListener(this);
                findViewById(R.id.v_sex).setOnClickListener(this);
                findViewById(R.id.v_name).setOnClickListener(this);
                findViewById(R.id.tv_exit).setOnClickListener(this);
            }
            findViewById(R.id.v_version).setOnClickListener(this);

            UserInfo userInfo = UserDataUtil.getUserInfo(this);
            Glide.with(this).load(userInfo.getAvatar()).crossFade().into(civ);
            tv_name.setText(userInfo.name==null?"":userInfo.name);
            tv_sex.setText(getSex(userInfo.sex));
            dialogLoading = WLibDialogHelper.createProgressDialog(this, "正在提交...");
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private String getSex(String sex) {
        String result = "未设置";
        if (!TextUtils.isEmpty(sex)) {
            result = "1".equals(sex)?"男":"女";
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_avatar:
                showChoosePicDialog();
                break;
            case R.id.v_sex:
                toChooseSex();
                break;
            case R.id.v_name:
                toChangeName();
                break;
            case R.id.v_lj:
                GlideCacheUtil.getInstance().clearImageDiskCache(BaseApplication.getInstance());
                SystemUtils.clearWebCache(this);
                showToast("清除成功");
                break;
            case R.id.tv_exit:
                toExit();
                break;
            case R.id.v_version:
                checkUpdateApp();
                break;
        }
    }

    private void update(Map<String, String> map, TempTag t) {
        Factory.resp(this, HttpFlag.FLAG_UPDATE_INFO, t, null).post(map);
    }

    private void toChooseSex() {
        SexDialog sexDialog = new SexDialog(this);
        sexDialog.setChooseSex(UserDataUtil.getUserInfo(this).sex).setOnOkListener(v1 -> {

            int sex = sexDialog.getSex();
            if (!UserDataUtil.getUserInfo(this).sex.equals(sex+"")) {
                tv_sex.setText(getSex(sex+""));
                Map<String, String> params = new HashMap<>();
                params.put("sex", sex + "");
                update(params, new TempTag(3, sex+""));
            }
            sexDialog.dismiss();

        }).show();
    }

    class TempTag {
        public int type;
        public String str;

        public TempTag(int type, String str) {
            this.type = type;
            this.str = str;
        }
    }

    private void checkUpdateApp() {
        try {
            if (hasUpdate) {
                UpdateAppDialog dialog = new UpdateAppDialog(this);
                dialog.setData(mConfig);
                dialog.show();
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void toChangeName() {
        EditNickNameDialog editNickNameDialog = new EditNickNameDialog(this);
        editNickNameDialog.setNickName(UserDataUtil.getUserInfo(this).name).setOnOkListener(v1 -> {
            String nickName = editNickNameDialog.getNickName();
            if (TextUtils.isEmpty(nickName)) {
                ToastUtils.showToast("昵称不能为空");
                return;
            }
            if (nickName.length() >= 16) {
                ToastUtils.showToast("昵称不能超过16个字符");
                return;
            }
            if (!nickName.equals(UserDataUtil.getUserInfo(this).name)) {
                tv_name.setText(nickName);
                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                params.put("nickname", nickName);
                update(params, new TempTag(2, nickName));
            }
            editNickNameDialog.dismiss();
        }).show();
    }

    private static final int PHOTO_REQUEST_GALLERY = 0;// 从相册中选择
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 2;// 结果
    private File tempFile;
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    /**
     * 修改头像浮窗
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0: // 选择本地照片
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, PHOTO_REQUEST_GALLERY);
                    break;
                case 1: // 拍照
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 判断存储卡是否可以用，可用进行存储
                    if (SystemUtils.hasSdcard()) {
                        tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                        // 从文件中创建uri
                        Uri uri = getUriForFile(this, tempFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    }
                    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
                    startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
                    break;
            }
        });
        builder.create().show();
    }

    //解决Android 7.0之后的Uri安全问题
    private Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider
                    .getUriForFile(context.getApplicationContext(), getString(R.string.wlib_provider_authorities), file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private void toExit() {
        Factory.resp(this, HttpFlag.FLAG_USER_EXIT, null, null).post(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_GALLERY: //相册图片后返回的uri
                    if (data != null) {
                        // 得到图片的全路径
                        Uri uri = data.getData();
                        startActivityForResult(CropUtils.invokeSystemCrop(uri), PHOTO_REQUEST_CUT);
                    }
                    break;
                case PHOTO_REQUEST_CAREMA: //相机返回的uri
                    if (hasSdcard()) {
                        startActivityForResult(CropUtils.invokeSystemCrop(Uri.fromFile(tempFile)), PHOTO_REQUEST_CUT);
                    } else {
                        ToastUtils.showToast("未找到存储卡，无法存储照片！");
                    }
                    break;
                case PHOTO_REQUEST_CUT:
                    // 从剪切图片返回的数据
                    try {
                        if (data != null) {
                            //获取裁剪后的图片，并显示出来
                            Uri uri = Uri.fromFile(new File(CropUtils.getPath()));
                            Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
                            if (bitmap != null) {
                                civ.setImageBitmap(bitmap);
                                //保存到SharedPreferences
                                saveBitmapToSharedPreferences(bitmap);

                                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                                params.put("uid", UserDataUtil.getUserInfo(this).id);
                                params.put("file", SharePrefUtil.getString(this, "film_head_image", ""));
                                requestHeadImg(params);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (tempFile != null)
                                // 将临时文件删除
                                tempFile.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 上传图片
     *
     * @param params
     */
    private void requestHeadImg(Map<String, String> params) {
        Factory.resp(this, HttpFlag.FLAG_UPLOAD_IMG, null, null, 1).post(params);
    }

    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        SharePrefUtil.saveString(this, "film_head_image", imageString);
    }


    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_USER_EXIT) {
            try {
                UserDataUtil.clearUserData(this);
                WLibManager.getInstance().clearCookieJar();
                SystemUtils.clearCookie(this);
                SystemUtils.clearWebCache(this);
                DBHelper.getInstance().clearVideoRecord(BaseApplication.getInstance());
                UserHelper.getInstance().getUserInfo(this);
                finish();
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag==HttpFlag.FLAG_UPDATE_INFO) {
            try {
                if (!TextUtils.isEmpty(hint)) showToast(hint);
                UserHelper.getInstance().getUserInfo(this);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_UPLOAD_IMG) {
            try {
                JSONObject resJson = new JSONObject(response.toString());
                String status = resJson.getString("status");
                if (status.equals("success")) {
                    String path = resJson.getString("path");
                    String host = resJson.getString("host");
                    LinkedHashMap<String, String> params = new LinkedHashMap<>();
                    params.put("avatar", path);
                    showAvatar(host + path);
                    update(params, new TempTag(1, host + path));
                } else {
                    WLibDialogHelper.dismiss(dialogLoading);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                WLibDialogHelper.dismiss(dialogLoading);
            }
        }
    }

    private void showAvatar(String path) {
        if (!TextUtils.isEmpty(path)) {
            Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .preload();
        }
    }

    @Override
    public void handleLoading(int flag, Object tag, boolean isShow) {

    }

    @Override
    public void handleError(int flag, Object tag, int errorType, String response, String hint) {

    }

    @Override
    public void handleAfter(int flag, Object tag) {

    }

    private boolean hasUpdate = false;
    private UpdateConfig mConfig = null;
    private void handleUpdate(Message msg) {
        try {
            if (msg.what == MSG_WHAT_CHECK_UPDATE) {
                mConfig = (UpdateConfig) msg.obj;
                hasUpdate = UiUtils.checkUpdate(mConfig);
                if (hasUpdate) {
                    tv_version_hint.setText("有新版本");
                    tv_version_hint.setTextColor(getResources().getColor(R.color.base_title_color));
                } else {
                    tv_version_hint.setText("当前已是最新版");
                }
            } else if (msg.what == MSG_WHAT_CHECK_UPDATE_ERROR) {
                tv_version_hint.setText("检测版本信息失败");
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private static final int MSG_WHAT_CHECK_UPDATE = 14352;
    private static final int MSG_WHAT_CHECK_UPDATE_ERROR = 14353;
    private static class MyHandler extends Handler {
        private WeakReference<SettingActivity> weak;
        private MyHandler(SettingActivity activity) {
            weak = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            try {
                if (weak.get()!=null&&(msg.what==MSG_WHAT_CHECK_UPDATE||msg.what==MSG_WHAT_CHECK_UPDATE_ERROR)) {
                    weak.get().handleUpdate(msg);
                }
            } catch (Exception e){

            }
        }
    }
}
