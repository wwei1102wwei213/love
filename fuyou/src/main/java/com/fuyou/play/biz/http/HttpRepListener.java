package com.fuyou.play.biz.http;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/21.
 *
 * Http请求的接口
 *
 * @author wwei
 */
public interface HttpRepListener {

    /**
     * 参数说明
     *  flag  接口标识
     *  obj   请求标识
     *  response   请求到的数据
     */


    //获取请求参数,返回Map,
    Map getParamInfo(int flag, Object obj);

    //获取请求参数，返回byte[]
    byte[] getPostParams(int flag, Object obj);

    //处理返回数据
    void toActivity(Object response, int flag, Object obj);

    //请求中loading操作
    void showLoading(int flag, Object obj);

    //中断loading
    void hideLoading(int flag, Object obj);

    //请求错误
    void showError(int flag, Object obj, int errorType);

}
