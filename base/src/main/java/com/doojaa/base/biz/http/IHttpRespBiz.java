package com.doojaa.base.biz.http;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/29 0029.
 *
 * 网络请求解耦接口
 *
 * @author wwei
 */

public interface IHttpRespBiz {

    void post(Map<String, String> params);
    void get(Map<String, String> params);
    void uploadPost();
    void multiUploadPost();

}
