package com.slove.play.biz;

import com.slove.play.biz.http.HttpRepListener;
import com.slove.play.biz.http.HttpRespBiz;
import com.slove.play.biz.http.IHttpRespBiz;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class Factory {

    public static IHttpRespBiz getHttpRespBiz(HttpRepListener mResp, int flag, Object tag){
        return new HttpRespBiz(mResp, flag, tag);
    }

}
