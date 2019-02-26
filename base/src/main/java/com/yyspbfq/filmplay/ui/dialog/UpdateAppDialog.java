package com.yyspbfq.filmplay.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Browser;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.UpdateConfig;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.tools.FileUtils;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class UpdateAppDialog extends Dialog{

    private Context context;
    private MyHandler mHandler;

    public UpdateAppDialog(@NonNull Context context) {
        this(context, R.style.dialog_base_style);
    }

    public UpdateAppDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initViews();
    }

    private TextView tv_version, tv_content, tv_index, tv_progress, tv_cancel, tv_update;
    private View v_btns, v_progress;
    private ProgressBar pb;
    private void initViews() {
        setContentView(R.layout.dialog_update_app);
        tv_version = findViewById(R.id.tv_version);
        tv_content = findViewById(R.id.tv_content);
        tv_index = findViewById(R.id.tv_index);
        tv_progress = findViewById(R.id.tv_progress);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_update = findViewById(R.id.tv_update);
        v_btns = findViewById(R.id.v_btns);
        v_progress = findViewById(R.id.v_progress);
        pb = findViewById(R.id.pb);
    }

    private String mSign = null;
    public void setData(UpdateConfig config) {
        try {
            tv_version.setText(TextUtils.isEmpty(config.versionName)?"":("V"+config.versionName));
            tv_content.setText(TextUtils.isEmpty(config.text)?"":config.text);
            if ("1".equals(config.autoup)) {
                setCancelable(false);
                tv_cancel.setVisibility(View.GONE);
                setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return keyCode == KeyEvent.KEYCODE_BACK;
                    }
                });
            } else {
                tv_cancel.setVisibility(View.VISIBLE);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDownload(config.versionCode, config.downurl);
                }
            });
            if (!TextUtils.isEmpty(config.website)) {
                tv_index.setText("官方网址："+config.website);
                tv_index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(config.website));
                            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                            context.startActivity(intent);
                        } catch (Exception e) {
                            BLog.e(e);
                        }
                    }
                });
            }
            mSign = config.sign;
        } catch (Exception e){
            BLog.e(e);
        }
    }


    private void toDownload(String newVersion, String downurl) {
        if (!FileUtils.isSdcardExit()) {
            ToastUtils.showToast("SD卡不存在");
            dismiss();
            return;
        }
        tv_update.setClickable(false);
        v_btns.setVisibility(View.GONE);
        v_progress.setVisibility(View.VISIBLE);
        tv_progress.setText("下载进度: 0%");
        pb.setProgress(0);
        try {
            String format = "update_%s.apk";
            File updateFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + FileUtils.UPDATE_APK_BASE_PATH,
                    String.format(format, newVersion));
            Logger.e("filePath:"+updateFile.getPath()+",updateFile.exists():"+updateFile.exists());
            if (updateFile.exists() && updateFile.isFile()) {
                /*installApk(updateFile.getPath());
                *//*tv_update.setClickable(true);
                v_btns.setVisibility(View.VISIBLE);
                v_progress.setVisibility(View.GONE);
                tv_update.setText("立即安装");
                tv_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        installApk(updateFile.getPath());
                    }
                });*/
                updateFile.delete();
            }
            if (!updateFile.getParentFile().exists()) {
                updateFile.getParentFile().mkdirs();
            }
            mHandler = new MyHandler(this);
            downloadInThread(downurl, updateFile);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void updateProgress(Message msg) {
        try {
            int progress = msg.arg1;
            if (progress>0) {
                pb.setProgress(progress);
            }
            tv_progress.setText("下载进度:"+progress+"%");
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void onError() {
        try {
            tv_progress.setText("下载失败,点击弹窗外部关闭次弹窗");
            setCancelable(true);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void downloadInThread(final String url, final File updateFile) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = Message.obtain();
                try {
                    download(url, updateFile);
                    msg.what = MSG_WHAT_DOWNLOAD_SUCCESS;
                    msg.obj = updateFile.getPath();
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    try {
                        if (updateFile.exists()) {
                            updateFile.delete();
                        }
                        msg.what = MSG_WHAT_DOWNLOAD_FAIL;
                        mHandler.sendMessage(msg);
                    } catch (Exception ex){
                        BLog.e(ex);
                    }
                }
            }
        }.start();
    }

    private synchronized void download(@NonNull String url, @NonNull File destFile) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = new OkHttpClient().newCall(request).execute();
        ResponseBody body = response.body();
        long contentLength = body.contentLength();
        BufferedSource source = body.source();
        BufferedSink sink = Okio.buffer(Okio.sink(destFile));
        Buffer sinkBuffer = sink.buffer();
        long totalBytesRead = 0;
        int bufferSize = 8 * 1024;
        for (long bytesRead; (bytesRead = source.read(sinkBuffer, bufferSize)) != -1; ) {
            sink.emit();
            totalBytesRead += bytesRead;
            int progress = (int) ((totalBytesRead * 100) / contentLength);
            publishProgress(progress);
        }
        sink.flush();
        sink.close();
        source.close();
    }

    private void publishProgress(int progress) {
        Message msg = Message.obtain();
        msg.what = MSG_WHAT_DOWNLOAD_PROGRESS;
        msg.arg1 = progress;
        mHandler.sendMessage(msg);
    }

    //安装apk
    private void installApk(String path) {
        /*try {
            tv_update.setClickable(true);
            v_btns.setVisibility(View.VISIBLE);
            v_progress.setVisibility(View.GONE);
            tv_update.setText("立即安装");
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    installApk(path);
                }
            });
        } catch (Exception e){

        }*/
        try {
            File apkFile = new File(path);
            //比较包名
            if (!CommonUtils.comparePackage(context, apkFile)) {
                ToastUtils.showToast("安装包错误，您可能遭到DNS劫持，请稍后再重新升级");
                if (apkFile.isFile() && apkFile.exists()) {
                    apkFile.delete();
                }
                dismiss();
                return;
            }
            //比较md5
            if (!CommonUtils.compareMd5(apkFile, mSign)) {
                ToastUtils.showToast("安装包错误，请重新升级");
                if (apkFile.isFile() && apkFile.exists()) {
                    apkFile.delete();
                }
                dismiss();
                return;
            }
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(installIntent);
            dismiss();
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private static final int MSG_WHAT_DOWNLOAD_SUCCESS = 1;
    private static final int MSG_WHAT_DOWNLOAD_FAIL = 2;
    private static final int MSG_WHAT_DOWNLOAD_PROGRESS = 3;
    private static class MyHandler extends Handler {
        private WeakReference<UpdateAppDialog> weak;
        private MyHandler(UpdateAppDialog dialog) {
            weak = new WeakReference<>(dialog);
        }
        @Override
        public void handleMessage(Message msg) {
            try {
                if (weak.get()==null) return;
                if (msg.what==MSG_WHAT_DOWNLOAD_SUCCESS) {
                    weak.get().installApk(msg.obj.toString());
                } else if (msg.what==MSG_WHAT_DOWNLOAD_FAIL) {
                    weak.get().onError();
                } else if (msg.what==MSG_WHAT_DOWNLOAD_PROGRESS) {
                    weak.get().updateProgress(msg);
                }
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }



}
