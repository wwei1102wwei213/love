package com.slove.play.biz;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.slove.play.R;
import com.slove.play.service.DownloadService;

import java.io.File;


/**
 * Created by Administrator on 2018/1/30 0030.
 */

public class UpdateAppBiz {


    private Activity context;

    public UpdateAppBiz(Activity context){
        this.context = context;
        compareVersion(context);
    }

    private static final int COMPARE_VERSION = 54;
    public void compareVersion(final Activity activity){
        /*AsyncTaskManager.getInstance(activity).request(COMPARE_VERSION, new OnDataListener() {
            @Override
            public Object doInBackground(int requestCode, String parameter) throws HttpException {
                return new SealAction(context).getSealTalkVersion3();
            }

            @Override
            public void onSuccess(int requestCode, Object result2) {
                if (result2 != null) {
                    try {
                        //key带有空格 org.json.JSONException: No value for
                        String result= (String) result2;
                        NLog.e("MCTEST", "--onSuccess.result--" + result.toString());
                        result=result.replaceAll(" ","").trim();
                        JSONObject resultjson=new JSONObject((String) result);
                        String data=resultjson.getString("data");
                        JSONObject datajson=new JSONObject(data);
                        String config=datajson.getString("config");
                        JSONObject configjson=new JSONObject(config);
                        String version=configjson.getString("version");
                        String tmpurl=configjson.getString("url");
                        int is_must_upgrade=configjson.getInt("is_must_upgrade");
                        String describe=configjson.getString("describe");
                        int status=configjson.getInt("status");
                        if (status==1){
                            VersionResponse.VersionConfig config1 = new VersionResponse.VersionConfig();
                            config1.version = version;
                            config1.url = tmpurl;
                            config1.describe = describe;
                            config1.is_must_upgrade = is_must_upgrade;
                            config1.status = status;
                            toUpdateApp(config1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, int state, Object result) {

            }
        });*/
    }

    private String mUrl = "";
    private AlertDialog dialog;
    private void toUpdateApp(Object result){
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        }
    }

    private String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private void toCheckDownPermission(){
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(context, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED ) {
                // 如果没有授予该权限，就去提示用户请求
                startRequestPermission();
            } else {
                downloadApk();
            }
        } else {
            downloadApk();
        }
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(context, permissions, 321);
    }

    public void downloadApk() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String downloadDir = Environment.getExternalStorageDirectory().getPath() + "/Download/APK";
            String apkName = TextUtils.isEmpty(mUrl) ? "machat.apk" : mUrl.substring(mUrl.lastIndexOf("/"));
            File file = new File(downloadDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            Intent downloadIntent = new Intent(context, DownloadService.class);
            downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_NAME, apkName);
            downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_URL, mUrl);
            context.startService(downloadIntent);
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.not_find_sdcard), Toast.LENGTH_SHORT).show();
        }
    }

    // 提示用户去应用设置界面手动开启权限
    public void showDialogTipUserGoToAppSettting() {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("手机存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许手机存储功能")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivityForResult(intent, 123);
    }

}
