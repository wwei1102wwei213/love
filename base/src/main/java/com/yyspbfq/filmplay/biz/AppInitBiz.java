package com.yyspbfq.filmplay.biz;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.bean.HostInfoBean;
import com.yyspbfq.filmplay.bean.HostItem;
import com.yyspbfq.filmplay.bean.UpdateHostBean;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.Secret;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;



/**
 * APP初始化业务
 */
public class AppInitBiz {

    //检测完成
    public static final int MSG_WHAT_CHECK_HOST_FINISHED = 12343;
    //无可用配置
    public static final int MSG_WHAT_CHECK_HOST_NOT_USE = 12344;
    //没有网络
    public static final int MSG_WHAT_CHECK_HOST_NO_INETERNET = 12345;
    //配置加载失败
    public static final int MSG_WHAT_CHECK_HOST_OTHER = 12346;
    public static final int MSG_WHAT_CHECK_HOST_WEBSITE = 12347;

    //当前检测更新地址
    public static final String KEY_CURRENT_CHECK_UPDATE = "film_current_check_update";
    //当前配置数据
    public static final String KEY_CURRENT_CONFIG = "film_hostInfo_config";
    //配置最近可访问地址
//    public static final String KEY_CURRENT_CONFIG_URL = "film_hostInfo_config_url";
    //当前可以使用的API
    public static final String KEY_CURRENT_API = "film_current_api";
    //当前可以使用的图片HOST
    public static final String KEY_CURRENT_IMAGE_HOST = "film_current_image_host";
    //上次的更新时间
    private final String KEY_LAST_UPDATE_TIME = "film_last_update_time";
    //是否已使用备用配置
    private final String KEY_IS_USE_BACKUP = "film_is_use_backup";

    private final String KEY_CURRENT_UPDATE_CONFIG = "film_update_app_config";
    private final String KEY_CURRENT_WEBSITE_CONFIG = "film_website_app_config";

    private List<String> baseUrls;
    private List<HostItem> updateCheckUrls;
    private String lastUpdateTime;

    private Handler mHandler;


    private int isBackup;

    public AppInitBiz(Handler mHandler) {
        baseUrls = HttpFlag.getDefaultHostUrls();
        lastUpdateTime = SPLongUtils.getString(BaseApplication.getInstance(), KEY_LAST_UPDATE_TIME, null);
        isBackup = SPLongUtils.getInt(BaseApplication.getInstance(), KEY_IS_USE_BACKUP, 0);
        this.mHandler = mHandler;
    }

    public void init() {

        String mHostConfig = SPLongUtils.getString(BaseApplication.getInstance(), KEY_CURRENT_CONFIG, null);
        if (mHostConfig==null) {
            checkHost(baseUrls.get(0));
        } else {
            hostInfoBea = new Gson().fromJson(mHostConfig, HostInfoBean.class);
            updateCheckUrls = hostInfoBea.getStatus();
            if (updateCheckUrls!=null&&updateCheckUrls.size()>0) {
                checkUpdateTime(updateCheckUrls.get(0));
            } else {
                checkHost(baseUrls.get(0));
            }
        }

        /*String currentCheckUrl = SPLongUtils.getString(BaseApplication.getInstance(), KEY_CURRENT_CHECK_UPDATE, null);
        checkUpdateTime(currentCheckUrl==null?updateCheckUrls.get(0):currentCheckUrl);*/
    }

    private void checkUpdateTime(HostItem mHostItem) {
        Logger.e("AppInitBiz: checkUpdateTime===>"+mHostItem.getRequest_url());
        Request request = new Request.Builder().url(mHostItem.getRequest_url()).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException eee) {
                nextUpdateTime(mHostItem);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code()==200) {
                        ResponseBody body = response.body();
                        UpdateHostBean bean = new Gson().fromJson(body.charStream(), UpdateHostBean.class);
                        String updateTime = bean.getUpdateTime();
                        String mHostConfig = SPLongUtils.getString(BaseApplication.getInstance(), KEY_CURRENT_CONFIG, null);
                        SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_CHECK_UPDATE, new Gson().toJson(mHostItem));
                        if (lastUpdateTime==null||!lastUpdateTime.endsWith(updateTime)||TextUtils.isEmpty(mHostConfig)) {//更新配置
                            SPLongUtils.saveString(BaseApplication.getInstance(), KEY_LAST_UPDATE_TIME, updateTime);
                            SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_API, "");
                            SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_IMAGE_HOST, "");
                            SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_WEBSITE_CONFIG, "");
                            SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_UPDATE_CONFIG, "");
                            checkHost(baseUrls.get(0));
                        } else {
                            hostInfoBea = new Gson().fromJson(mHostConfig, HostInfoBean.class);
                            handleHostInfo(hostInfoBea);
                        }
                    } else {
                        nextUpdateTime(mHostItem);
                    }
                } catch (Exception e) {
                    nextUpdateTime(mHostItem);
                }
            }
        });
    }

    /**
     * 下一条检查更新时间
     * @param mHostItem
     */
    private void nextUpdateTime(HostItem mHostItem) {
        try {
            if (CommonUtils.IsNetWorkEnable(BaseApplication.getInstance())) {
                updateCheckUrls.remove(mHostItem);
                if (updateCheckUrls.size()>0) {
                    checkUpdateTime(updateCheckUrls.get(0));
                } else {
                    afterUpdateCheck();
                }
            } else {
                sendMsg(MSG_WHAT_CHECK_HOST_NO_INETERNET);
            }
        } catch (Exception e){
            afterUpdateCheck();
        }
    }

    private void afterUpdateCheck() {
        try {
            SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_API, "");
            SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_IMAGE_HOST, "");
            SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_WEBSITE_CONFIG, "");
            SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_UPDATE_CONFIG, "");
//            handleBackupConfig();
            checkHost(baseUrls.get(0));
        } catch (Exception e){
            sendMsg(MSG_WHAT_CHECK_HOST_OTHER);
        }
    }

    /**
     * 处理分发
     * @param url 分发地址
     */
    private void checkHost(String url) {
        Logger.e("AppInitBiz: checkHost===>"+url);
        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException eee) {
                nextHost(url);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code()==200) {
                        ResponseBody body = response.body();
                        UpdateHostBean bean = new Gson().fromJson(body.charStream(), UpdateHostBean.class);
                        String config = Secret.AES.decrypt(Base64.decode(bean.getHostInfo().getBytes(), Base64.NO_WRAP));
//                        SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_CONFIG_URL, url);
                        handleConfig(config);
                    } else {
                        nextHost(url);
                    }
                } catch (Exception e) {
                    nextHost(url);
                }
            }
        });
    }

    private HostInfoBean hostInfoBea;

    /**
     * 处理配置
     * @param config 配置数据
     */
    private void handleConfig(String config) {
        try {
            Logger.e("AppInitBiz: handleConfig===>"+config);
            hostInfoBea = new Gson().fromJson(config, HostInfoBean.class);
            SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_CONFIG, config);
            handleHostInfo(hostInfoBea);
        } catch (Exception e){
            sendMsg(MSG_WHAT_CHECK_HOST_OTHER);
        }
    }

    /**
     * 检测下一条
     */
    private void nextHost(String url) {
        try {
            if (CommonUtils.IsNetWorkEnable(BaseApplication.getInstance())) {
                baseUrls.remove(url);
                if (baseUrls.size()>0) {
                    checkHost(baseUrls.get(0));
                } else {
                    //配置地址全部失效，使用备用配置
                    handleBackupConfig();
                }
            } else {
                sendMsg(MSG_WHAT_CHECK_HOST_NO_INETERNET);
            }
        } catch (Exception e){
            sendMsg(MSG_WHAT_CHECK_HOST_OTHER);
        }
    }

    /**
     * 处理备用配置
     */
    private void handleBackupConfig() {
        try {
            isBackup = 1;
            SPLongUtils.saveInt(BaseApplication.getInstance(), KEY_IS_USE_BACKUP , 1);
            String mHostConfig = SPLongUtils.getString(BaseApplication.getInstance(), KEY_CURRENT_CONFIG, null);
            if (!TextUtils.isEmpty(mHostConfig)) {//更新配置
                HostInfoBean hostInfoBea = new Gson().fromJson(mHostConfig, HostInfoBean.class);
                backupList = hostInfoBea.getIssue();
                if (backupList==null||backupList.size()==0) {
                    sendMsg(MSG_WHAT_CHECK_HOST_OTHER);
                } else {
                    checkBackupConfig(backupList.get(0));
                }
            } else {
                sendMsg(MSG_WHAT_CHECK_HOST_NOT_USE);
            }
        } catch (Exception e){
            sendMsg(MSG_WHAT_CHECK_HOST_OTHER);
        }
    }

    private List<HostItem> backupList;
    /**
     * 检测备用配置
     * @param hostItem
     */
    private void checkBackupConfig(HostItem hostItem) {
        Logger.e("AppInitBiz: checkBackupConfig===>"+hostItem.getRequest_url());
        Request request = new Request.Builder().url(hostItem.getUrl()+HttpFlag.URL_HOST_CONFIG_SUFFIX).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException eee) {
                nextBackupConfig(hostItem);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code()==200) {
                        ResponseBody body = response.body();
                        UpdateHostBean bean = new Gson().fromJson(body.charStream(), UpdateHostBean.class);
                        String config = Secret.AES.decrypt(Base64.decode(bean.getHostInfo().getBytes(), Base64.NO_WRAP));
//                        SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_CONFIG_URL, hostItem.getUrl()+HttpFlag.URL_HOST_CONFIG_SUFFIX);
                        handleConfig(config);
                    } else {
                        nextBackupConfig(hostItem);
                    }
                } catch (Exception e) {
                    nextBackupConfig(hostItem);
                }
            }
        });
    }

    /**
     * 下一条备用配置
     * @param hostItem
     */
    private void nextBackupConfig(HostItem hostItem) {
        try {
            if (CommonUtils.IsNetWorkEnable(BaseApplication.getInstance())) {
                backupList.remove(hostItem);
                if (backupList.size()>0) {
                    checkBackupConfig(backupList.get(0));
                } else {
                    sendMsg(MSG_WHAT_CHECK_HOST_NOT_USE);
                }
            } else {
                sendMsg(MSG_WHAT_CHECK_HOST_NO_INETERNET);
            }
        } catch (Exception e){
            sendMsg(MSG_WHAT_CHECK_HOST_OTHER);
        }
    }

    private List<HostItem> apis;
    private HostItem currentApi;
    /**
     * 处理配置
     */
    private void handleHostInfo(HostInfoBean bean) {
        try {
            apis = bean.getApi();
            if (apis==null||apis.size()==0) {
                sendMsg(MSG_WHAT_CHECK_HOST_OTHER);
            } else {
                String currentApiStr = SPLongUtils.getString(BaseApplication.getInstance(), KEY_CURRENT_API, null);
                if (TextUtils.isEmpty(currentApiStr)) {
                    currentApi = apis.get(0);
                } else {
                    try {
                        currentApi = new Gson().fromJson(currentApiStr, HostItem.class);
                    } catch (Exception e){

                    }
                    if (currentApi==null || TextUtils.isEmpty(currentApi.getRequest_url()) ||
                            TextUtils.isEmpty(currentApi.getUrl())) {
                        currentApi = apis.get(0);
                    }
                }
                checkApi();
            }
        } catch (Exception e){
            sendMsg(MSG_WHAT_CHECK_HOST_OTHER);
        }
    }

    /**
     * 检测API
     */
    private void checkApi() {
        Logger.e("AppInitBiz: checkApi===>"+currentApi.getRequest_url());
        Request request = new Request.Builder().url(currentApi.getRequest_url()).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException eee) {
                nextApi();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code()==200) {
                        SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_API, new Gson().toJson(currentApi));
                        handleImageHost();
                    } else {
                        nextApi();
                    }
                } catch (Exception e) {
                    nextApi();
                }
            }
        });
    }

    /**
     * 检测下一条API
     */
    private void nextApi() {
        try {
            if (CommonUtils.IsNetWorkEnable(BaseApplication.getInstance())) {
                apis.remove(currentApi);
                if (apis.size()>0) {
                    currentApi = apis.get(0);
                    checkApi();
                } else {
                    if (isBackup==0) {
                        handleBackupConfig();
                    } else {
                        sendMsg(MSG_WHAT_CHECK_HOST_NOT_USE);
                    }
                }
            } else {
                sendMsg(MSG_WHAT_CHECK_HOST_NO_INETERNET);
            }
        } catch (Exception e){
            sendMsg(MSG_WHAT_CHECK_HOST_OTHER);
        }
    }

    private List<HostItem> images;
    private HostItem currentImageHost;
    /**
     * 处理图片服务
     */
    private void handleImageHost() {
        try {
            images = hostInfoBea.getImage();
            if (images==null||images.size()==0) {
                //没有图片前缀则直接完成
                sendMsg(MSG_WHAT_CHECK_HOST_FINISHED);
            } else {
                String currentApiStr = SPLongUtils.getString(BaseApplication.getInstance(), KEY_CURRENT_IMAGE_HOST, null);
                if (TextUtils.isEmpty(currentApiStr)) {
                    currentImageHost = images.get(0);
                } else {
                    try {
                        currentImageHost = new Gson().fromJson(currentApiStr, HostItem.class);
                    } catch (Exception e){

                    }
                    if (currentImageHost==null || TextUtils.isEmpty(currentImageHost.getRequest_url()) ||
                            TextUtils.isEmpty(currentImageHost.getUrl())) {
                        currentImageHost = apis.get(0);
                    }
                }
                checkImageHost();
            }
        } catch (Exception e){
            sendMsg(MSG_WHAT_CHECK_HOST_OTHER);
        }
    }

    /**
     * 检测图片host
     */
    private void checkImageHost() {
        Logger.e("AppInitBiz: checkImageHost===>"+currentImageHost.getRequest_url());
        Request request = new Request.Builder().url(currentImageHost.getRequest_url()).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException eee) {
                nextImageHost();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code()==200) {
                        SPLongUtils.saveString(BaseApplication.getInstance(), KEY_CURRENT_IMAGE_HOST, new Gson().toJson(currentImageHost));
                        sendMsg(MSG_WHAT_CHECK_HOST_FINISHED);
                    } else {
                        nextImageHost();
                    }
                } catch (Exception e) {
                    nextImageHost();
                }
            }
        });

    }

    /**
     * 检测下一条图片服务
     */
    private void nextImageHost() {
        try {
            if (CommonUtils.IsNetWorkEnable(BaseApplication.getInstance())) {
                images.remove(currentImageHost);
                if (images.size()>0) {
                    currentImageHost = images.get(0);
                    checkImageHost();
                } else {
                    sendMsg(MSG_WHAT_CHECK_HOST_FINISHED);
                }
            } else {
                sendMsg(MSG_WHAT_CHECK_HOST_FINISHED);
            }
        } catch (Exception e){
            sendMsg(MSG_WHAT_CHECK_HOST_FINISHED);
        }
    }


    private List<HostItem> websites = null;
    /**
     * 处理主页配置
     */
    private void handleWebsite() {
        try {
            String mHostConfig = SPLongUtils.getString(BaseApplication.getInstance(), KEY_CURRENT_CONFIG, null);
            if (!TextUtils.isEmpty(mHostConfig)) {//更新配置
                HostInfoBean hostInfoBea = new Gson().fromJson(mHostConfig, HostInfoBean.class);
                websites = hostInfoBea.getOfficial();
            }
            if (websites==null||websites.size()==0) {
                handleError(null);
            } else {
                HostItem hostItem = null;
                String currentConfig = SPLongUtils.getString(BaseApplication.getInstance(),
                        KEY_CURRENT_WEBSITE_CONFIG, null);
                if (TextUtils.isEmpty(currentConfig)) {
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
        Logger.e("AppInitBiz: checkWebsite===>"+hostItem.getRequest_url());
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
                if (websites==null) {
                    handleError(null);
                } else {
                    websites.remove(oldItem);
                    if (websites.size()>0) {
                        checkWebsite(websites.get(0));
                    } else {
                        handleError(null);
                    }
                }
            }
        } catch (Exception e){
            handleError(null);
        }
    }

    private void handleError(String website) {
        try {
            if (mHandler==null) return;
            if (TextUtils.isEmpty(website)) website = HttpFlag.DEFAULT_WEBSITE;
            Message msg = Message.obtain();
            msg.what = MSG_WHAT_CHECK_HOST_NOT_USE;
            msg.obj = website;
            mHandler.sendMessage(msg);
        } catch (Exception e){
            mustSendMsg(MSG_WHAT_CHECK_HOST_OTHER);
        }
    }

    private void sendMsg(int what) {
        try {
            if (mHandler==null) return;
            if (what==MSG_WHAT_CHECK_HOST_NOT_USE||what==MSG_WHAT_CHECK_HOST_OTHER) {
                handleWebsite();
            } else {
                Message msg = Message.obtain();
                msg.what = what;
                mHandler.sendMessage(msg);
            }
        } catch (Exception e){

        }
    }

    private void mustSendMsg(int what) {
        try {
            if (mHandler==null) return;
            Message msg = Message.obtain();
            msg.what = what;
            mHandler.sendMessage(msg);
        } catch (Exception e){

        }
    }
}
