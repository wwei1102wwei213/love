package com.doojaa.base.biz;


import com.doojaa.base.biz.http.HttpRepListener;
import com.doojaa.base.biz.http.HttpRespBiz;
import com.doojaa.base.biz.http.IHttpRespBiz;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class Factory {

    public static IHttpRespBiz resp(HttpRepListener mResp, int flag, Object tag, Class<?> mClass){
        return new HttpRespBiz(flag, tag, mResp, mClass);
    }

    public static IHttpRespBiz resp(HttpRepListener mResp, int flag, Object tag, Class<?> mClass, int codeType){
        return new HttpRespBiz(flag, tag, mResp, mClass, codeType);
    }

}
