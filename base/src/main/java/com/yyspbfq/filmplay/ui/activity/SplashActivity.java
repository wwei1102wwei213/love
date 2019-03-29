package com.yyspbfq.filmplay.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.service.DownloadService;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.AdvertBean;
import com.yyspbfq.filmplay.bean.HostItem;
import com.yyspbfq.filmplay.bean.ThumbEntity;
import com.yyspbfq.filmplay.biz.AppInitBiz;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.SystemUtils;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;

import net.nightwhistler.htmlspanner.HtmlSpanner;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/8/30 0030.
 *
 * 闪屏页
 *
 * @author wwei
 */
public class SplashActivity extends BaseActivity implements WLibHttpListener{

    private long appStartTime;
    private MyHandler mHandler;
    private TextView tv;
    private ImageView iv_close;
    private int time, jumpAble;
    private TextView tv_hint_loading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_splash);
        if (SPLongUtils.isFirst(this)) {
            String invite_code = SystemUtils.getChannelFromClip(this);
            SPLongUtils.saveFirst(this, false);
            if (!TextUtils.isEmpty(invite_code)) {
                SPLongUtils.saveString(this, "video_invite_code_clip", invite_code);
            }
        }
        tv_hint_loading = findViewById(R.id.tv_hint_loading);
        mHandler = new MyHandler(this);
        tv_hint_loading.setVisibility(View.VISIBLE);
        new AppInitBiz(mHandler).init();
    }

    private static final String IS_FIRST_RUN = "IsFirstRun";
    private AdvertBean advertBean;
    private void toNext(){
        if (advertBean==null) {
            enterMain();
        } else {
            if (advertBean.getThumb()==null) {
                enterMain();
            } else {
                try {
                    List<ThumbEntity> thumb = new Gson().fromJson(new Gson().toJson(advertBean.getThumb()),
                            new TypeToken<List<ThumbEntity>>(){}.getType());
                    if (thumb==null||thumb.size()==0) {
                        enterMain();
                    } else {
                        String defaultThumb = null;
                        String matchThumb = null;
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        int widthPixels = dm.widthPixels;
                        int heightPixels = dm.heightPixels;
                        for (ThumbEntity entity:thumb) {
                            if (!TextUtils.isEmpty(entity.getUrl())) {
                                if (entity.getWidth()==1080 && entity.getHeight()==1920) defaultThumb = entity.getUrl();
                                if ( widthPixels * entity.getHeight() == heightPixels * entity.getWidth()) {
                                    matchThumb = entity.getUrl();
                                    break;
                                }
                            }
                        }
                        if (!TextUtils.isEmpty(matchThumb)) {
                            settingShipView(matchThumb);
                        } else if (!TextUtils.isEmpty(defaultThumb)) {
                            settingShipView(defaultThumb);
                        } else {
                            settingShipView(thumb.get(0).getUrl());
                        }
                    }
                } catch (Exception e){
                    BLog.e(e);
                    enterMain();
                }
            }
        }
    }

    private void settingShipView(String thumb) {
        time = advertBean.getShowtime();
        if (time<=0) time = 3;
        jumpAble = advertBean.getIsclose();
        ImageView iv = (ImageView) findViewById(R.id.iv);
        Glide.with(this)
                .load(thumb)
                .crossFade()
                .into(iv);
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(900);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        iv.setAnimation(animation);
        animation.start();
        iv_close = (ImageView) findViewById(R.id.iv_close);
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumpAble==1) {
                    enterMain();
                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterMain();
            }
        });
        if (!TextUtils.isEmpty(advertBean.getUrl())) {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAdvert();
                }
            });
        }
        sendMsg();
    }

    private void sendMsg() {
        if (mHandler!=null) mHandler.sendEmptyMessageDelayed(time, 900);
    }

    private void clickAdvert() {
        try {
            clearHandler();
            Map<String, String> map = new HashMap<>();
            map.put("id", advertBean.getId());
            Factory.resp(this, HttpFlag.FLAG_INVITE_CLICK_ADS, null, null).post(map);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void toClickAdvComplete() {
        if (advertBean==null||TextUtils.isEmpty(advertBean.getUrl())) {
            enterMain();
        } else {
            String url = advertBean.getUrl();
            if (advertBean.getType()==2) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    BLog.s("downloadApk");
                    String apkName = url.substring(url.lastIndexOf("/"));
                    Intent downloadIntent = new Intent(this, DownloadService.class);
                    downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_NAME, apkName);
                    downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_URL, url);
                    this.startService(downloadIntent);
                } else {
                    Toast.makeText(this, this.getResources().getString(R.string.not_find_sdcard), Toast.LENGTH_SHORT).show();
                }
                enterMain();
            } else {
                ShowWebActivity.actionStart(this, url, 1);
            }
            finish();
        }

    }

    private void handleMsg(int what) {
        if (isJump) return;
        if (what>0) {
            tv.setText(getTimeStr(what));
            if (tv.getVisibility()!=View.VISIBLE) tv.setVisibility(View.VISIBLE);
            if (mHandler!=null) mHandler.sendEmptyMessageDelayed(what-1, 1000);
        } else {
            if (jumpAble==1) {
                enterMain();
            } else {
                iv_close.setVisibility(View.VISIBLE);
                tv.setVisibility(View.GONE);
            }
        }
    }

    private String getTimeStr (int what) {
        String result = "";
        result = what + "";
        if (jumpAble==1) {
            result += "s | 跳过";
        }
        return result;
    }

    private void enterMain() {
        try {
            clearHandler();
            Intent intent = new Intent(this, MainActivity.class);
            if (getIntent().getExtras() != null) {
                intent.putExtras(getIntent().getExtras());
            }
            startActivity(intent);
            this.finish();
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private boolean isJump = false;
    private void clearHandler() {
        isJump = true;
        try {
            if (mHandler!=null) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_ADVERT_FIRST_SCREEN) {
            try {
                advertBean = (AdvertBean) formatData;
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    public void handleLoading(int flag, Object tag, boolean isShow) {
        if (flag==HttpFlag.FLAG_ADVERT_FIRST_SCREEN) {

        }
    }

    private boolean enterError = false;
    @Override
    public void handleError(int flag, Object tag, int errorType, String response, String hint) {
        if (flag==HttpFlag.FLAG_ADVERT_FIRST_SCREEN) {

        }
    }

    @Override
    public void handleAfter(int flag, Object tag) {

        if (HttpFlag.FLAG_INVITE_CLICK_ADS==flag) {
            toClickAdvComplete();
        } else if (flag==HttpFlag.FLAG_ADVERT_FIRST_SCREEN) {
            if (advertBean==null) {
                if (!enterError) enterMain();
            } else {
                toNext();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            if (!enterError) return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        clearHandler();
        super.onDestroy();
    }

    private static class MyHandler extends Handler {
        private WeakReference<SplashActivity> weak;
        private MyHandler(SplashActivity activity) {
            weak = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weak!=null&&weak.get()!=null) {
                if (msg.what == AppInitBiz.MSG_WHAT_CHECK_HOST_FINISHED ||
                        msg.what == AppInitBiz.MSG_WHAT_CHECK_HOST_NOT_USE ||
                        msg.what == AppInitBiz.MSG_WHAT_CHECK_HOST_NO_INETERNET ||
                        msg.what == AppInitBiz.MSG_WHAT_CHECK_HOST_OTHER ||
                        msg.what == AppInitBiz.MSG_WHAT_CHECK_HOST_WEBSITE){
                    weak.get().initFinished(msg);
                } else {
                    weak.get().handleMsg(msg.what);
                }
            }
        }
    }

    private void initFinished(Message msg) {
        if (mBaseExit) return;
        try {
            String currentConfig = SPLongUtils.getString(BaseApplication.getInstance(), AppInitBiz.KEY_CURRENT_API, null);
            String currentImageHost = SPLongUtils.getString(BaseApplication.getInstance(), AppInitBiz.KEY_CURRENT_IMAGE_HOST, null);
            if (!TextUtils.isEmpty(currentConfig)) {
                HostItem item = new Gson().fromJson(currentConfig, HostItem.class);
                HttpFlag.BASE_URL = item.getUrl();
                if (TextUtils.isEmpty(HttpFlag.BASE_URL)) return;
                HttpFlag.changeBaseUrl();
                if (!TextUtils.isEmpty(currentImageHost)) {
                    item = new Gson().fromJson(currentImageHost, HostItem.class);
                    HttpFlag.changeImageHost(item.getUrl());
                }
            }
            if (msg.what == AppInitBiz.MSG_WHAT_CHECK_HOST_FINISHED) {
                tv_hint_loading.setVisibility(View.GONE);
                Factory.resp(this, HttpFlag.FLAG_ADVERT_FIRST_SCREEN, null, AdvertBean.class).post(null);
            } else if (msg.what == AppInitBiz.MSG_WHAT_CHECK_HOST_NOT_USE){
                enterError = true;
                if (CommonUtils.IsNetWorkEnable(BaseApplication.getInstance())) {
                    HtmlSpanner spanner = new HtmlSpanner();
                    String temp = "请至官网重新下载最新版本\n<a href=\""+msg.obj+"\"><span style=\"color:#ffffff\">"+msg.obj+"</span></a>";
                    tv_hint_loading.setText(spanner.fromHtml(temp));
                    tv_hint_loading.setMovementMethod(LinkMovementMethod.getInstance());
                    tv_hint_loading.setVisibility(View.VISIBLE);
                }
            } else if (msg.what == AppInitBiz.MSG_WHAT_CHECK_HOST_NO_INETERNET){
                enterError = true;
                tv_hint_loading.setText("请检查网络连接");
                tv_hint_loading.setVisibility(View.VISIBLE);
            } else if (msg.what == AppInitBiz.MSG_WHAT_CHECK_HOST_OTHER){ //配置加载异常，尝试进入APP使用更新渠道
                if (!TextUtils.isEmpty(currentConfig)) {
                    enterMain();
                } else {
                    enterError = true;
                    tv_hint_loading.setText("配置加载异常");
                    tv_hint_loading.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e){

        }
    }

    /*private void handleUpdate(UpdateConfig config) {
        if (mBaseExit) return;
        SPLongUtils.saveString(this, "mevideo_update_config", new Gson().toJson(config));
        if (updateUrls!=null) {
            SPLongUtils.saveString(this, "video_update_app_url", config.meHost);
        }
        if (UiUtils.checkUpdate(config)) {
            UpdateAppDialog dialog = new UpdateAppDialog(this);
            dialog.setData(config);
            dialog.show();
        }
    }*/

 }
