package com.doojaa.base.biz.http;

/**
 * Created by Administrator on 2018/10/10.
 *
 * Http请求的接口
 *
 * @author wwei
 */
public interface HttpRepListener {

    /**
     *
     *  参数说明
     *
     *  flag  接口标识
     *
     *  obj   请求标识
     *
     *  data       请求数据（格式化）
     *
     *             格式化数据需要和
     *             @see com.doojaa.base.biz.Factory 中的 Class<?> mClass 参数配合使用
     *             如果mClass为null,则response为String类型且不为空
     *
     *  response   请求数据（源数据）
     *
     *  errorType  错误标识
     *
     *  hint  提示信息
     *
     */

    //处理返回数据
    void handleResp(Object data, int flag, Object obj, String response, String hint);

    //请求中loading操作
    void showLoading(int flag, Object obj);

    //中断loading
    void hideLoading(int flag, Object obj);

    //请求错误
    void handleError(int flag, Object obj, int errorType, String response, String hint);

    //请求结束后操作
    void handleAfter(int flag, Object obj);
}
