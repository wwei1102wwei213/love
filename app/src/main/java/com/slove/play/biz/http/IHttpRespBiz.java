package com.slove.play.biz.http;

/**
 * Created by Administrator on 2017/8/29 0029.
 *
 * 网络请求解耦接口
 *
 * @author wwei
 */

public interface IHttpRespBiz {

    void post();
    void get();
    void uploadPost();
    void multiUploadPost();

}
