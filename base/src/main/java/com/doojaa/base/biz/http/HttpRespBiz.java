package com.doojaa.base.biz.http;

import android.os.Handler;
import android.os.Message;

import com.doojaa.base.utils.BLog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;


public class HttpRespBiz implements IHttpRespBiz {

    private int flag;
    private Object tag;
    private String OkTag;
    private Gson mGson;
//    private WeakReference<HttpRepListener> weak;
    private Class<?> mClass;
    private int codeType = -1;
    private HttpRepListener callback;
    private MyHandler mHandler;

    public HttpRespBiz(int flag, Object tag, HttpRepListener callback, Class<?> mClass) {
        this.flag = flag;
        this.tag = tag;
        this.callback = callback;
        mGson = new Gson();
        this.mClass = mClass;
        OkTag = flag + "-" + System.currentTimeMillis();
//        mHandler = new MyHandler(this);
    }

    public HttpRespBiz(int flag, Object tag, HttpRepListener callback, Class<?> mClass, int codeType) {
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
    public void post(Map<String, String> params) {
        if (callback!=null) callback.showLoading(flag, tag);
        /*PostFormBuilder builder = OkHttpUtils.post().url(postUrl());
        if (params!=null&&params.size()>0) {
            builder.params(params);
        }
        builder.tag(OkTag).build().execute(new HttpStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                BLog.h("请求地址："+postUrl()+",FLAG:"+flag + ",错误信息:"+e.getMessage()+",请求参数："+mGson.toJson(params));
                handleError(call, e);
                cancel();
            }

            @Override
            public void onResponse(String response) {
                BLog.h("请求地址："+postUrl()+"(POST)\n请求参数："+mGson.toJson(params)+ "\n请求数据："+response);
                handleResponse(response);
                cancel();
            }
        });*/
//        BookThreadPoolManager.execute(new HttpRunnable(mHandler, postUrl(), params,"POST"));
    }

    @Override
    public void get(Map<String, String> params) {
        if (callback!=null) callback.showLoading(flag, tag);
        /*GetBuilder builder = OkHttpUtils.get().url(getUrl());
        if (params!=null&&params.size()>0) {
            builder.params(params);
        }
        builder.tag(OkTag).build().execute(new HttpStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                BLog.h("请求地址："+getUrl()+",FLAG:"+flag + ",错误信息:"+e.getMessage()+",请求参数："+mGson.toJson(params));
                handleError(call, e);
                cancel();
            }

            @Override
            public void onResponse(String response) {
                BLog.h("请求地址："+getUrl()+"(GET)\n请求参数："+mGson.toJson(params)+ "\n请求数据："+response);
                handleResponse(response);
                cancel();
            }
        });*/
//        BookThreadPoolManager.execute(new HttpRunnable(mHandler, getUrl(), params,"GET"));
    }

    @Override
    public void uploadPost() {

    }

    @Override
    public void multiUploadPost() {

    }

    private String getUrl() {
        String result = null;
        switch (flag) {
            case HttpFlag.FLAG_USER_UPDATE_INFO:
                result = HttpFlag.URL_USER_UPDATE_INFO;
                break;
            case HttpFlag.FLAG_FREE:
                result = HttpFlag.URL_FREE;
                break;
            case HttpFlag.FLAG_BOOK_CATALOG:
                result = HttpFlag.URL_BOOK_CATALOG;
                break;
            case HttpFlag.FLAG_BUY_BOOKCOIN:
                result = HttpFlag.URL_BUY_BOOKCOIN;
                break;
            case HttpFlag.FLAG_BOOK_AUTHOR_MORE:
                result = HttpFlag.URL_BOOK_AUTHOR_MORE;
                break;
            case HttpFlag.FLAG_BOOK_CITY_MORE:
                result = HttpFlag.URL_BOOK_CITY_MORE;
                break;
            case HttpFlag.FLAG_BOOK_TYPE_BOOKS:
                result = HttpFlag.URL_BOOK_TYPE_BOOKS;
                break;
            case HttpFlag.FLAG_BOOK_RECOMMEND:
                result = HttpFlag.URL_BOOK_RECOMMEND;
                break;
            case HttpFlag.FLAG_BOOK_RANK:
                result = HttpFlag.URL_BOOK_RANK;
                break;
            case HttpFlag.FLAG_BOOK_RANK_DETAIL:
                result = HttpFlag.URL_BOOK_RANK_DETAIL;
                break;
            case HttpFlag.FLAG_BOOK_TYPE:
                result = HttpFlag.URL_BOOK_TYPE;
                break;
            case HttpFlag.FLAG_BOOK_WEEKSEARCHHOT:
                result = HttpFlag.URL_BOOK_WEEKSEARCHHOT;
                break;
            case HttpFlag.FLAG_BOOK_CITY_GUESS_YOUR_LOVE:
                result = HttpFlag.URL_BOOK_CITY_GUESS_YOUR_LOVE;
                break;
            case HttpFlag.FLAG_BOOK_SEARCH_HOT:
                result = HttpFlag.URL_BOOK_SEARCH_HOT;
                break;
            case HttpFlag.FLAG_BOOK_SEARCH_AUTO:
                result = HttpFlag.URL_BOOK_SEARCH_AUTO;
                break;
            case HttpFlag.FLAG_BOOK_SEARCH:
                result = HttpFlag.URL_BOOK_SEARCH;
                break;
            case HttpFlag.FLAG_COMMENT_BOOKS:
                result = HttpFlag.URL_COMMENT_BOOKS;
                break;
            case HttpFlag.FLAG_COMMENT_VOTE:
                result = HttpFlag.URL_COMMENT_VOTE;
                break;
            case HttpFlag.FLAG_COMMENT_BOOKS_SEND:
                result = HttpFlag.URL_COMMENT_BOOKS_SEND;
                break;
            case HttpFlag.FLAG_COMMENT_CHARPTERS:
                result = HttpFlag.URL_COMMENT_CHARPTERS;
                break;
            case HttpFlag.FLAG_COMMENT_CHARPTERS_SEND:
                result = HttpFlag.URL_COMMENT_CHARPTERS_SEND;
                break;
            case HttpFlag.FLAG_COMMENT_DETAIL:
                result = HttpFlag.URL_COMMENT_DETAIL;
                break;
            case HttpFlag.FLAG_SHORT:
                result = HttpFlag.URL_SHORT;
                break;
            case HttpFlag.FLAG_FEED_BACK:
                result = HttpFlag.URL_FEED_BACK;
                break;
            case HttpFlag.FLAG_CONVERT_CODE:
                result = HttpFlag.URL_CONVERT_CODE;
                break;
            case HttpFlag.FLAG_SYSTEM_CONFIG:
                result = HttpFlag.URL_SYSTEM_CONFIG;
                break;
            case HttpFlag.FLAG_USER_INFO:
                result = HttpFlag.URL_USER_INFO;
                break;
            case HttpFlag.FLAG_ACTIVITY_CONFIG:
                result = HttpFlag.URL_ACTIVITY_CONFIG;
                break;
            case HttpFlag.FLAG_BOOK_CITY:
                result = HttpFlag.URL_BOOK_CITY;
                break;
            case HttpFlag.FLAG_BOOK_SHELF_BANNER:
                result = HttpFlag.URL_BOOK_SHELF_BANNER;
                break;
            case HttpFlag.FLAG_BOOK_READLOG_USER:
                result = HttpFlag.URL_BOOK_READLOG_USER;
                break;
            case HttpFlag.FLAG_BOOK_READLOG_VISITOR:
                result = HttpFlag.URL_BOOK_READLOG_VISITOR;
                break;
            case HttpFlag.FLAG_BOOK_PROFILE:
                result = HttpFlag.URL_BOOK_PROFILE;
                break;
            case HttpFlag.FLAG_BOOK_GUESSLIKE:
                result = HttpFlag.URL_BOOK_GUESSLIKE;
                break;
            case HttpFlag.FLAG_BOOK_READ_END:
                result = HttpFlag.URL_BOOK_READ_END;
                break;
            case HttpFlag.FLAG_LOGIN_MOBILE_CODE:
                result = HttpFlag.URL_LOGIN_MOBILE_CODE;
                break;
            case HttpFlag.FLAG_COUPON_FOR_READ:
                result = HttpFlag.URL_COUPON_FOR_READ;
                break;
            case HttpFlag.FLAG_SYSTEM_WEIXIN:
                result = HttpFlag.URL_SYSTEM_WEIXIN;
                break;
            case HttpFlag.FLAG_USER_INVITE:
                result = HttpFlag.URL_USER_INVITE;
                break;
            case HttpFlag.FLAG_USER_MESSAGE:
                result = HttpFlag.URL_USER_MESSAGE;
                break;
            case HttpFlag.FLAG_BOOK_LABEL:
                result = HttpFlag.URL_BOOK_LABEL;
                break;
            case HttpFlag.FLAG_BOOK_ADD:
                result = HttpFlag.URL_BOOK_ADD;
                break;
            case HttpFlag.FLAG_BOOK_DEL:
                result = HttpFlag.URL_BOOK_DEL;
                break;
            case HttpFlag.FLAG_USER_BOOKS:
                result = HttpFlag.URL_USER_BOOKS;
                break;
            case HttpFlag.FLAG_BOOK_CHAPTER:
                result = HttpFlag.URL_BOOK_CHAPTER;
                break;
            case HttpFlag.FLAG_BOOK_SHARE_READING:
                result = HttpFlag.URL_BOOK_SHARE_READING;
                break;
            case HttpFlag.FLAG_COMPENSATE:
                result = HttpFlag.URL_COMPENSATE;
                break;
            case HttpFlag.FLAG_UNLOCK_CHARPTER_PANEL:
                result = HttpFlag.URL_UNLOCK_CHARPTER_PANEL;
                break;
            case HttpFlag.FLAG_UNLOCK_BOOK_PANEL:
                result = HttpFlag.URL_UNLOCK_BOOK_PANEL;
                break;
            case HttpFlag.FLAG_UNLOCK_CHARPTER:
                result = HttpFlag.URL_UNLOCK_CHARPTER;
                break;
            case HttpFlag.FLAG_UNLOCK_BOOK:
                result = HttpFlag.URL_UNLOCK_BOOK;
                break;
            case HttpFlag.FLAG_INVITE_TASK:
                result = HttpFlag.URL_INVITE_TASK;
                break;
            case HttpFlag.FLAG_INVITE_SAVE_CODE:
                result = HttpFlag.URL_INVITE_SAVE_CODE;
                break;
            case HttpFlag.FLAG_INVITE_CLICK_ADS:
                result = HttpFlag.URL_INVITE_CLICK_ADS;
                break;
        }
        return result;
    }

    private String postUrl() {
        String result = null;
        switch (flag) {
            case HttpFlag.FLAG_UPLOAD_IMG:
                result = HttpFlag.URL_UPLOAD_IMG;
                break;
            case HttpFlag.FLAG_LOGIN_MOBILE:
                result = HttpFlag.URL_LOGIN_MOBILE;
                break;
            case HttpFlag.FLAG_BOOK_UPDATE:
                result = HttpFlag.URL_BOOK_UPDATE;
                break;
            case HttpFlag.FLAG_BOOK_EDIT_STATUS:
                result = HttpFlag.URL_BOOK_EDIT_STATUS;
                break;
            case HttpFlag.FLAG_SIGN_RECEIVE_REWARD:
                result = HttpFlag.URL_SIGN_RECEIVE_REWARD;
                break;
            case HttpFlag.FLAG_BOOK_RECORD:
                result = HttpFlag.URL_BOOK_RECORD;
                break;
        }
        return result;
    }

    private void handleResponse(String response) {
        if (callback!=null) {
            callback.hideLoading(flag, tag);
            try {
                if (response != null) {
                    if (codeType!=-1) {
                        callback.handleResp(response, flag, tag, response, null);
                    } else {
                        JSONObject resJson = new JSONObject(response);
                        int status = resJson.optInt("code");
                        String msg = resJson.optString("msg");
                        if (status == HttpFlag.RESULT_OK) {
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
                                    handleError(HttpFlag.HTTP_ERROR_DATA_EMPTY, response, "数据异常");
                                }
                            }
                        } else {
                            handleError(HttpFlag.HTTP_ERROR_CODE, response, msg);
                        }
                    }
                } else {
                    handleError(HttpFlag.HTTP_ERROR_RESULT_EMPTY, null, "数据异常");
                }
            } catch (JSONException | JsonSyntaxException e) {
                BLog.e(e);
                handleError(HttpFlag.HTTP_ERROR_FORMAT, response, "数据异常");
            } catch (Exception e) {
                BLog.e(e);
                handleError(HttpFlag.HTTP_ERROR_OTHER, response, null);
            }
        } else {
//            BLog.e("handleResponse null");
            BLog.h("请求地址："+getUrl(), " handleResponse: callback is empty.");
        }
    }

    /*private void handleError(Call call, Exception e) {
        if (callback!=null) {
            callback.hideLoading(flag, tag);
            handleError(HttpFlag.HTTP_ERROR_DISCONNECT, null, null);
            if (!NetworkUtils.isNetWorkAvailable(AppUtils.getContext())) {
                ToastUtils.showToast(R.string.books_network_error);
            }
        } else {
            BLog.h("请求地址："+getUrl(), "handleError: callback is empty.");
        }
    }*/

    private void handleError(int errorType, String source, String msg) {
        BLog.e("请求地址："+getUrl(), "错误类型："+getErrorSummary(errorType));
        try {
            if (callback!=null) {
                callback.handleError(flag, tag, errorType, source, msg);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private Object formatResultData(String response) {
        if (response == null) return null;
        if (mClass == null) return response;
        Object o = null;
        try {
            o = mGson.fromJson(response, mClass);
        } catch (JsonSyntaxException e) {
            BLog.e("请求成功,JSON异常:" + e.toString());
        } catch (Exception e){
            BLog.e("请求成功,其他异常:" + e.toString());
        }
        return o;
    }

    private String getErrorSummary(int errorType){
        String error  = "未知错误";
        switch (errorType) {
            case HttpFlag.HTTP_ERROR_NETWORK:
                error = "网络异常";
                break;
            case HttpFlag.HTTP_ERROR_DISCONNECT:
                error = "连接异常";
                break;
            case HttpFlag.HTTP_ERROR_RESULT_EMPTY:
                error = "返回数据为空";
                break;
            case HttpFlag.HTTP_ERROR_FORMAT:
                error = "数据解析异常";
                break;
            case HttpFlag.HTTP_ERROR_CODE:
                error = "数据code异常";
                break;
            case HttpFlag.HTTP_ERROR_DATA_EMPTY:
                error = "data数据为空";
                break;
            case HttpFlag.HTTP_ERROR_OTHER:
                error = "其他异常";
                break;
        }
        return error;
    }

    /*private void cancel() {
        try {
            if (callback!=null) {
                callback.handleAfter(flag, tag);
            }
        } catch (Exception e){
            BLog.e(e);
        }
        try {
            OkHttpUtils.getInstance().cancelTag(OkTag);
            *//*mHandler.removeCallbacksAndMessages(null);
            mHandler = null;*//*
        } catch (Exception e){
            BLog.e(e);
        }
    }*/

    private static class MyHandler extends Handler {

        private WeakReference<HttpRespBiz> weak;

        private MyHandler(HttpRespBiz biz) {
            weak = new WeakReference<>(biz);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weak.get()!=null) {
                if (msg.what==1) {
                    weak.get().handleResponse((String) msg.obj);
                } else if (msg.what==2) {
//                    weak.get().handleError(null, null);
                }
            }
        }
    }

}
