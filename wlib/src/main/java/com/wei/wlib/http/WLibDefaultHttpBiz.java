package com.wei.wlib.http;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wei.wlib.util.WLibLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

import okhttp3.Call;


public class WLibDefaultHttpBiz implements IWLibHttpBiz {

    private int flag;
    private Object tag;
    private String OkTag;
    private Gson mGson;
//    private WeakReference<WLibHttpListener> weak;
    private Class<?> mClass;
    private int codeType = -1;
    private WLibHttpListener callback;
    private MyHandler mHandler;

    public WLibDefaultHttpBiz(int flag, Object tag, WLibHttpListener callback, Class<?> mClass) {
        this.flag = flag;
        this.tag = tag;
        this.callback = callback;
        mGson = new Gson();
        this.mClass = mClass;
        OkTag = flag + "-" + System.currentTimeMillis();
//        mHandler = new MyHandler(this);
    }

    public WLibDefaultHttpBiz(int flag, Object tag, WLibHttpListener callback, Class<?> mClass, int codeType) {
        this.flag = flag;
        this.tag = tag;
        this.callback = callback;
        mGson = new Gson();
        this.mClass = mClass;
        OkTag = flag + "-" + System.currentTimeMillis();
        this.codeType = codeType;
//        mHandler = new MyHandler(this);
    }

    @Override
    public void post(final Map<String, String> params) {
        if (callback!=null) callback.handleLoading(flag, tag, true);
        PostFormBuilder builder = OkHttpUtils.post().url(postUrl());
        if (params!=null&&params.size()>0) {
            builder.params(params);
        }
        builder.tag(OkTag).build().execute(new WLibStringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                WLibLog.h("请求地址："+postUrl()+",FLAG:"+flag + ",错误信息:"+e.getMessage()+",请求参数："+mGson.toJson(params));
                handleError(WLibHttpFlag.HTTP_ERROR_DISCONNECT, null, null);
                cancel();
            }

            @Override
            public void onResponse(String response, int id) {
                WLibLog.h("请求地址："+postUrl()+"(POST)\n请求参数："+mGson.toJson(params)+ "\n请求数据："+response);
                handleResponse(response);
                cancel();
            }
        });
//        WLibThreadPoolManager.execute(new HttpRunnable(mHandler, postUrl(), params,"POST"));
    }

    @Override
    public void get(final Map<String, String> params) {
        if (callback!=null) callback.handleLoading(flag, tag, true);
        GetBuilder builder = OkHttpUtils.get().url(getUrl());
        if (params!=null&&params.size()>0) {
            builder.params(params);
        }
        builder.tag(OkTag).build().execute(new WLibStringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                WLibLog.h("请求地址："+getUrl()+",FLAG:"+flag + ",错误信息:"+e.getMessage()+",请求参数："+mGson.toJson(params));
                handleError(WLibHttpFlag.HTTP_ERROR_DISCONNECT, null, null);
                cancel();
            }

            @Override
            public void onResponse(String response, int id) {
                WLibLog.h("请求地址："+getUrl()+"(GET)\n请求参数："+mGson.toJson(params)+ "\n请求数据："+response);
                handleResponse(response);
                cancel();
            }
        });
//        WLibThreadPoolManager.execute(new HttpRunnable(mHandler, getUrl(), params,"GET"));
    }

    @Override
    public void uploadPost() {

    }

    @Override
    public void multiUploadPost() {

    }

    protected String getUrl() {
        return null;
    }

    protected String postUrl() {
        return null;
    }

    private void handleResponse(String response) {
        if (callback!=null) {
            callback.handleLoading(flag, tag, false);
            try {
                if (response != null) {
                    if (codeType!=-1) {
                        callback.handleResp(response, flag, tag, response, null);
                    } else {
                        JSONObject resJson = new JSONObject(response);
                        int status = resJson.optInt("code");
                        String msg = resJson.optString("msg");
                        if (status == WLibHttpFlag.RESULT_OK) {
                            Object data = resJson.opt("data");

                            if (mClass==null) {
                                callback.handleResp(data, flag, tag, response, msg);
                            } else {
                                Object o = null;
                                if (data!=null) {
                                    o = formatResultData(data+"");
                                }
                                if (o!=null) {
                                    callback.handleResp(o, flag, tag, response, msg);
                                } else {
                                    handleError(WLibHttpFlag.HTTP_ERROR_DATA_EMPTY, response, "数据异常");
                                }
                            }
                        } else {
                            handleError(WLibHttpFlag.HTTP_ERROR_CODE, response, msg);
                        }
                    }
                } else {
                    handleError(WLibHttpFlag.HTTP_ERROR_RESULT_EMPTY, null, "数据异常");
                }
            } catch (JSONException | JsonSyntaxException e) {
                WLibLog.e(e);
                handleError(WLibHttpFlag.HTTP_ERROR_FORMAT, response, "数据异常");
            } catch (Exception e) {
                WLibLog.e(e);
                handleError(WLibHttpFlag.HTTP_ERROR_OTHER, response, null);
            }
        } else {
            WLibLog.h("请求地址："+getUrl(), " handleResponse: callback is empty.");
        }
    }

    private void handleError(int errorType, String source, String msg) {
        WLibLog.e("请求地址："+getUrl(), "错误类型："+getErrorSummary(errorType));
        try {
            if (callback!=null) {
                callback.handleError(flag, tag, errorType, source, msg);
            }
        } catch (Exception e){
            WLibLog.e(e);
        }
    }

    private Object formatResultData(String response) {
        if (response == null) return null;
        if (mClass == null) return response;
        Object o = null;
        try {
            o = mGson.fromJson(response, mClass);
        } catch (JsonSyntaxException e) {
            WLibLog.e("请求成功,JSON异常:" + e.toString());
        } catch (Exception e){
            WLibLog.e("请求成功,其他异常:" + e.toString());
        }
        return o;
    }

    private String getErrorSummary(int errorType){
        String error  = "未知错误";
        switch (errorType) {
            case WLibHttpFlag.HTTP_ERROR_NETWORK:
                error = "网络异常";
                break;
            case WLibHttpFlag.HTTP_ERROR_DISCONNECT:
                error = "连接异常";
                break;
            case WLibHttpFlag.HTTP_ERROR_RESULT_EMPTY:
                error = "返回数据为空";
                break;
            case WLibHttpFlag.HTTP_ERROR_FORMAT:
                error = "数据解析异常";
                break;
            case WLibHttpFlag.HTTP_ERROR_CODE:
                error = "数据code异常";
                break;
            case WLibHttpFlag.HTTP_ERROR_DATA_EMPTY:
                error = "data数据为空";
                break;
            case WLibHttpFlag.HTTP_ERROR_OTHER:
                error = "其他异常";
                break;
        }
        return error;
    }

    private void cancel() {
        try {
            if (callback!=null) {
                callback.handleAfter(flag, tag);
            }
        } catch (Exception e){
            WLibLog.e(e);
        }
        try {
            OkHttpUtils.getInstance().cancelTag(OkTag);
//            mHandler.removeCallbacksAndMessages(null);
//            mHandler = null;
        } catch (Exception e){
            WLibLog.e(e);
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<WLibDefaultHttpBiz> weak;

        private MyHandler(WLibDefaultHttpBiz biz) {
            weak = new WeakReference<>(biz);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weak.get()!=null) {
                if (msg.what==1) {
                    weak.get().handleResponse((String) msg.obj);
                } else if (msg.what==2) {
                    weak.get().handleError(0, null, null);
                }
            }
        }
    }

}
