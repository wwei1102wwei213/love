package com.doojaa.base.biz;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;


/**
 * Created by Administrator on 2018/1/30 0030.
 */
public class UpdateAppBiz {

    private Activity context;
    private boolean isHome;

    public UpdateAppBiz(Activity context, boolean isHome){
        this.context = context;
        this.isHome = isHome;
        /*Object obj = SPLongUtils.getString(context, Const.SP_VERSION_CONFIG, null, CheckVersionConfig.class);
        if (obj!=null) {
            CheckVersionConfig bean = (CheckVersionConfig) obj;
            if (!TextUtils.isEmpty(bean.getVersion())&&!TextUtils.isEmpty(bean.getUrl())) {
                if (getVersionNameIntForString(bean.getVersion())>getVersionNameIntForString(CommonUtils.getVersionNum())) {
                    toUpdateApp(bean);
                }else {
                    if (!isHome) {
                        Toast.makeText(context, "已是最新版本，无需更新!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (!isHome) {
                    Toast.makeText(context, "已是最新版本，无需更新!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (!isHome) {
                Toast.makeText(context, "已是最新版本，无需更新!", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private String mUrl = "";
    /*private MLDialog dialog;
    private void toUpdateApp(CheckVersionConfig bean){
        try {
            mUrl = bean.getUrl();
            MLDialog.Builder builder = new MLDialog.Builder(context);
            builder.setTitle("有新的版本:");
            if (!TextUtils.isEmpty(bean.getDescribe())) {
                builder.setMessage(bean.getDescribe());
            }
            if ("1".equals(bean.getIs_must_upgrade())){
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toCheckDownPermission();
                        dialog.dismiss();
                    }
                });
                builder.setCancelAble(false);
            } else {
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toCheckDownPermission();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        }
    }*/

    private String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private void toCheckDownPermission(){
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(context, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED ) {
                // 如果没有授予该权限，就去提示用户请求
                try {
                    if (isHome) {
//                        ((MainActivity) context).checkPermissionUpdateApp();
                    } else {
//                        ((SystemSettingActivity) context).checkPermissionUpdateApp();
                    }
                } catch (Exception e){
//                    ExceptionUtils.ExceptionSend(e, "toCheckDownPermission");
                }
            } else {
                downloadApk();
            }
        } else {
            downloadApk();
        }
    }

    public void downloadApk() {
        /*SPLongUtils.saveString(context, Const.SP_VERSION_CONFIG, "");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            LogCustom.show("downloadApk");
            String downloadDir = Environment.getExternalStorageDirectory().getPath() + "/Download/APK";
            String apkName = TextUtils.isEmpty(mUrl) ? "milu.apk" : mUrl.substring(mUrl.lastIndexOf("/"));
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
        }*/
    }

    private int getVersionNameIntForString(String versionName){
        if (TextUtils.isEmpty(versionName)||"0".equals(versionName)) return 0;
        int result = 0;
        try {
            versionName = versionName.trim().replace(".","");
            int spaceLength = 5 - versionName.length();
            if (spaceLength>0){
                for (int i=0;i<spaceLength;i++){
                    versionName.concat("0");
                }
            }
            result = Integer.valueOf(versionName);
        } catch (Exception e){

        }
        return result;
    }
}
