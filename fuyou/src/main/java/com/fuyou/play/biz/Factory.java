package com.fuyou.play.biz;

import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.biz.http.HttpRespBiz;
import com.fuyou.play.biz.http.IHttpRespBiz;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class Factory {

    public static IHttpRespBiz getHttpRespBiz(HttpRepListener mResp, int flag, Object tag){
        return new HttpRespBiz(mResp, flag, tag);
    }

}
