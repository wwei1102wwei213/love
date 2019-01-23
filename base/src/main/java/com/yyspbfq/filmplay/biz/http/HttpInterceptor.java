package com.yyspbfq.filmplay.biz.http;

import android.content.Context;

import com.yyspbfq.filmplay.utils.CommonUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpInterceptor implements Interceptor{

    //渠道channel
    public static final String KEY_CHANNEL = "VIDEO-CHANNEL";
    //用户类型
//    public static final String KEY_TYPE = "film-type";

    private Context mContext;

    public HttpInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request requestOrigin = chain.request();
        Headers headersOrigin = requestOrigin.headers();
        Headers headers = headersOrigin.newBuilder()
                .set("User-Agent", "okhttp3" + CommonUtils.getUserAgent())
                .set(KEY_CHANNEL, CommonUtils.getChannelName())
//                .set(KEY_TYPE, UserDataUtil.getLoginType(mContext))
                .build();
        return chain.proceed(requestOrigin.newBuilder().headers(headers).build());
    }

}
