package com.yyspbfq.filmplay.biz;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.bean.HostInfoBean;
import com.yyspbfq.filmplay.bean.HostItem;
import com.yyspbfq.filmplay.bean.UpdateConfig;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AppUpdateBiz {

    private final String KEY_CURRENT_UPDATE_CONFIG = "film_update_app_config";
    private final String KEY_CURRENT_WEBSITE_CONFIG = "film_website_app_config";

    private Handler mHandler;
    private int success, fail;
    private boolean needWebsite;
    private List<HostItem> updateUrls;
    private List<HostItem> websites;

    public AppUpdateBiz(Handler mHandler, int success, int fail, boolean needWebsite) {
        this.mHandler = mHandler;
        this.success = success;
        this.fail = fail;
        this.needWebsite = needWebsite;
    }

    public void update() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String config = SPLongUtils.getString(BaseApplication.getInstance(), AppInitBiz.KEY_CURRENT_CONFIG, null);
                    if (TextUtils.isEmpty(config)) return;
                    HostInfoBean hostInfoBean = new Gson().fromJson(config, HostInfoBean.class);
                    if (hostInfoBean==null||hostInfoBean.getUpdate()==null||
                            hostInfoBean.getUpdate().size()==0) return;
                    updateUrls = hostInfoBean.getUpdate();
                    websites = hostInfoBean.getOfficial();
                    if (updateUrls==null||updateUrls.size()==0) return;
                    HostItem hostItem = null;
                    String currentConfig = SPLongUtils.getString(BaseApplication.getInstance(),
                            KEY_CURRENT_UPDATE_CONFIG, null);
                    if (currentConfig==null) {
                        hostItem = updateUrls.get(0);
                    } else {
                        try {
                            hostItem = new Gson().fromJson(currentConfig, HostItem.class);
                        } catch (Exception e){

                        }
                        if (hostItem==null || TextUtils.isEmpty(hostItem.getUrl())) {
                            hostItem = updateUrls.get(0);
                        }
                    }
                    checkUpdate(hostItem);
                } catch (Exception e){

                }
            }
        }).start();
    }


    private final String URL_UPDATE_SUFFIX = "android/update_";
    private final String URL_UPDATE_SUFFIX2 = ".json";

    private String getUpdateSuffix() {
        return URL_UPDATE_SUFFIX + CommonUtils.getChannelName() + URL_UPDATE_SUFFIX2;
    }

    /**
     * 检查更新
     * @param
     */
    public void checkUpdate(HostItem hostItem) {
        if (hostItem==null||TextUtils.isEmpty(hostItem.getUrl())) return;
        Logger.e("AppInitBiz: checkUpdate===>"+hostItem.getUrl());
        Request request = new Request.Builder().url(hostItem.getUrl()+getUpdateSuffix()).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                nextUpdate(hostItem);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code()==200) {
                        ResponseBody body = response.body();
                        UpdateConfig config = new Gson().fromJson(body.charStream(), UpdateConfig.class);
                        if (!TextUtils.isEmpty(config.downurl)&&!TextUtils.isEmpty(config.versionCode)) {
                            config.meHost = hostItem.getUrl();
                            SPLongUtils.saveString(BaseApplication.getInstance(),
                                    KEY_CURRENT_UPDATE_CONFIG, new Gson().toJson(hostItem));
                            if (mHandler!=null) {
                                Message msg = Message.obtain();
                                msg.what = success;
                                msg.obj = config;
                                mHandler.sendMessage(msg);
                            }
                        }
                    } else {
                        nextUpdate(hostItem);
                    }
                } catch (Exception e) {
                    nextUpdate(hostItem);
                }
            }
        });
    }

    /**
     * 检测下一条更新配置
     * @param oldItem
     */
    private void nextUpdate(HostItem oldItem) {
        try {
            if (CommonUtils.IsNetWorkEnable(BaseApplication.getInstance())) {
                if (updateUrls==null) return;
                updateUrls.remove(oldItem);
                if (updateUrls.size()>0) {
                    checkUpdate(updateUrls.get(0));
                } else {
                    handleNoUpdate();
                }
            }
        } catch (Exception ee){
            BLog.e(ee);
        }
    }

    /**
     * 处理我可用升级配置
     */
    private void handleNoUpdate() {
        try {
            if (needWebsite) {
                handleWebsite();
            }
        } catch (Exception e){

        }
    }

    /**
     * 处理主页配置
     */
    private void handleWebsite() {
        try {
            if (websites==null||websites.size()==0) {
                handleError(null);
            } else {
                HostItem hostItem = null;
                String currentConfig = SPLongUtils.getString(BaseApplication.getInstance(),
                        KEY_CURRENT_WEBSITE_CONFIG, null);
                if (currentConfig==null) {
                    hostItem = websites.get(0);
                } else {
                    try {
                        hostItem = new Gson().fromJson(currentConfig, HostItem.class);
                    } catch (Exception e){

                    }
                    if (hostItem==null || TextUtils.isEmpty(hostItem.getUrl())) {
                        hostItem = websites.get(0);
                    }
                }
                checkWebsite(hostItem);
            }
        } catch (Exception e){
            handleError(null);
        }
    }

    /**
     * 检查主页
     * @param
     */
    public void checkWebsite(HostItem hostItem) {
        if (hostItem==null||TextUtils.isEmpty(hostItem.getRequest_url())) return;
        Logger.e("AppInitBiz: checkWebsite===>"+hostItem.getUrl());
        Request request = new Request.Builder().url(hostItem.getRequest_url()).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                nextWebsite(hostItem);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code()==200) {
                        SPLongUtils.saveString(BaseApplication.getInstance(),
                                KEY_CURRENT_WEBSITE_CONFIG, new Gson().toJson(hostItem));
                        handleError(hostItem.getWebsite());
                    } else {
                        nextWebsite(hostItem);
                    }
                } catch (Exception e) {
                    nextWebsite(hostItem);
                }
            }
        });
    }

    /**
     * 检测下一条主页配置
     * @param oldItem
     */
    private void nextWebsite(HostItem oldItem) {
        try {
            if (CommonUtils.IsNetWorkEnable(BaseApplication.getInstance())) {
                if (websites==null) return;
                websites.remove(oldItem);
                if (websites.size()>0) {
                    checkWebsite(websites.get(0));
                }
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void handleError(String website) {
        try {
            if (mHandler!=null) {
                if (TextUtils.isEmpty(website)) website = HttpFlag.DEFAULT_WEBSITE;
                Message msg = Message.obtain();
                msg.what = fail;
                msg.obj = website;
                mHandler.sendMessage(msg);
            }
        } catch (Exception e){

        }
    }

}
