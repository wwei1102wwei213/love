package com.slove.play.biz.http;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.slove.play.util.Const;
import com.slove.play.util.LogCustom;
import com.squareup.okhttp.Request;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.Map;



/**
 * Created by Administrator on 2017/8/29 0029.
 *
 * 网络请求业务类
 *
 * @author wwei
 */
public class HttpRespBiz implements IHttpRespBiz{

    private HttpRepListener mResp;
    private int flag;
    private Object tag;
    private Gson mGson;
    private File tempFile;
    private String fileKey;

    private File[] files;
    private String[] fileKeys;

    public HttpRespBiz(HttpRepListener mResp, int flag , Object tag){
        this.mResp = mResp;
        this.flag = flag;
        this.tag = tag;
        mGson = new Gson();
    }

    public HttpRespBiz(HttpRepListener mResp, int flag , Object tag, String fileKey, File file){
        this.mResp = mResp;
        this.flag = flag;
        this.tag = tag;
        this.fileKey = fileKey;
        this.tempFile = file;
        mGson = new Gson();
    }

    public HttpRespBiz(HttpRepListener mResp, int flag, Object tag, String[] fileKeys, File[] files){
        this.mResp = mResp;
        this.flag = flag;
        this.tag = tag;
        this.fileKeys = fileKeys;
        this.files = files;
        mGson = new Gson();
    }

    @Override
    public void post() {
        mResp.showLoading(flag , tag);
        OkHttpClientManager.postJson(getPostUrl(), mResp.getParamInfo(flag, tag),
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if (mResp!=null){
                            mResp.hideLoading(flag, tag);
                            LogCustom.i(Const.LOG_TAG_HTTP, "请求失败：" + request.toString());
                            showError(e);
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if (mResp!=null){
                            mResp.hideLoading(flag, tag);
                            Object o = getGsonType(response);
                            if (o!=null){
                                mResp.toActivity(o, flag, tag);
                            }else {
                                LogCustom.i(Const.LOG_TAG_HTTP, "请求成功但数据为空");
                                mResp.showError(flag, tag, HttpFlag.ERROR_NORMAL);
                            }
                        }
                    }
                });
    }

    @Override
    public void get() {
        mResp.showLoading(flag, tag);
        OkHttpClientManager.get(getGetUrl(),
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if (mResp==null) return;
                        mResp.hideLoading(flag, tag);
                        LogCustom.i(Const.LOG_TAG_HTTP, "请求失败：" + request.toString());
                        showError(e);
                    }

                    @Override
                    public void onResponse(String response) {
                        if (mResp==null) return;
                        mResp.hideLoading(flag, tag);
                        Object o = getGsonType(response);
                        if (o != null) {
                            mResp.toActivity(o, flag, tag);
                        } else {
                            LogCustom.i(Const.LOG_TAG_HTTP, "请求成功但数据为空");
                            mResp.showError(flag, tag, HttpFlag.ERROR_NORMAL);
                        }
                    }
                },null);
    }

    @Override
    public void uploadPost() {
        mResp.showLoading(flag , tag);
        OkHttpClientManager.uploadPostJson(getPostUrl(),fileKey, tempFile ,mResp.getParamInfo(flag, tag),
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if (mResp==null) return;
                        mResp.hideLoading(flag, tag);
                        LogCustom.i(Const.LOG_TAG_HTTP, "请求失败：" + request.toString());
                        showError(e);
                    }

                    @Override
                    public void onResponse(String response) {
                        if (mResp==null) return;
                        mResp.hideLoading(flag, tag);
                        Object o = getGsonType(response);
                        if (o!=null){
                            mResp.toActivity(o, flag, tag);
                        }else {
                            LogCustom.i(Const.LOG_TAG_HTTP, "请求成功但数据为空");
                            mResp.showError(flag, tag, HttpFlag.ERROR_NORMAL);
                        }
                    }
                });
    }

    @Override
    public void multiUploadPost() {
        mResp.showLoading(flag , tag);
        OkHttpClientManager.multiUploadPostJson(getPostUrl(), fileKeys, files ,mResp.getParamInfo(flag, tag),
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if (mResp==null) return;
                        mResp.hideLoading(flag, tag);
                        LogCustom.i(Const.LOG_TAG_HTTP, "请求失败：" + request.toString());
                        showError(e);
                    }

                    @Override
                    public void onResponse(String response) {
                        if (mResp==null) return;
                        mResp.hideLoading(flag, tag);
                        Object o = getGsonType(response);
                        if (o!=null){
                            mResp.toActivity(o, flag, tag);
                        }else {
                            LogCustom.i(Const.LOG_TAG_HTTP, "请求成功但数据为空");
                            mResp.showError(flag, tag, HttpFlag.ERROR_NORMAL);
                        }
                    }
                });
    }

    private void showError(Exception e){
        int error = HttpFlag.ERROR_NORMAL;
        try {
            if (e.getCause().equals(SocketTimeoutException.class)) {
                error = HttpFlag.ERROR_TIME_OUT;
            }
        } catch (Exception ex){

        }
        mResp.showError(flag, tag, error);
    }

    /**
     * get 获取请求地址
     * @return 请求地址
     */
    private String getGetUrl(){
        String url = null;
        switch (flag){
            case HttpFlag.TEST:
                url = getPostUrl();
                Map<String,String> map = mResp.getParamInfo(flag, tag);
                String params = "";
                try {
                    for (String key:map.keySet()){
                        if (TextUtils.isEmpty(params)){
                            params += ("?" + key + "=" + map.get(key));
                        } else {
                            params += ("&"+key + "=" + map.get(key));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                url += params;
                LogCustom.show(url);
                break;
        }
        LogCustom.show( "请求地址：" + url);
        return url;
    }

    /**
     * post 获取请求地址
     * @return 请求地址
     */
    private String getPostUrl(){
        String url = null;
        switch (flag){
            case HttpFlag.TEST:
                url = HttpFlag.TEST_URL;
                break;
            case HttpFlag.LOGIN:
                url = HttpFlag.LOGIN_URL;
                break;
            case HttpFlag.REGISTER:
                url = HttpFlag.REGISTER_URL;
                break;
            case HttpFlag.USER_DETAIL:
                url = HttpFlag.USER_DETAIL_URL;
                break;
            default:
        }
        LogCustom.i(Const.LOG_TAG_HTTP, "请求地址：" + url);
        return url;
    }

    /**
     * 格式化返回数据
     * @param response
     * @return 格式化数据
     */
    private Object getGsonType(String response){
        Object o = null;
        if (response==null) return null;
        response = response.trim();
        LogCustom.i(Const.LOG_TAG_HTTP, "请求数据：" + response);
        try {
            switch (flag){
                case HttpFlag.TEST:
                    o = response;
                    break;
                case HttpFlag.LOGIN:
                    o = response;
                    break;
                case HttpFlag.REGISTER:
                    o = response;
                    break;
                case HttpFlag.USER_DETAIL:
                    o = response;
                    break;
            }
        } catch (Exception e) {
            LogCustom.i(Const.LOG_TAG_HTTP, "请求成功,JSON异常" + e.toString());
        }
        return o;
    }


}
