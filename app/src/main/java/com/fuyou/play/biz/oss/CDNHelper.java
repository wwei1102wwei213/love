package com.fuyou.play.biz.oss;

import android.content.Context;


import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.FileNotFoundException;



/**
 * Created by Administrator on 2015/9/14.
 * 上传图片的帮助类
 * 这个是用来上传到cdn的类
 */
public final class CDNHelper {
    private static final String tag = "?";//这个是cdn返回的url 根据？来切割得到图片
    private OSSBucket bucket;
    private Context context;

    public CDNHelper(Context context) {
        this.context = context;
        ossService = OSSServiceManager.getInstance(context);
        bucket = ossService.getOssBucket(); // 替换为你的bucketName
    }

    private OSSServiceManager ossService;

    private OSSFile ossFile;

    /**
     * 上传文件到阿里的服务器
     * @param filePath 文件的路径
     * @param fileName  文件设置的名字
     * @param saveCallback  上传是否成功的回调
     * @throws FileNotFoundException e
     */
    public void uploadFile(String filePath, String fileName, SaveCallback saveCallback) throws FileNotFoundException {
        ossFile = ossService.getOssFile(bucket, fileName);
        ossFile.setUploadFilePath(filePath, "multipart/form-data");
        ossFile.uploadInBackground(saveCallback);
    }

    public String getResourseURL() {
        if(ossFile == null) {
            throw new RuntimeException("ossFile is empty");
        }
        int sencond = 5 *  365 * 24 * 60 * 60;//图片有效期秒数
        String url = ossFile.getResourceURL(OSSServiceManager.accessKey, sencond);
        int index = url.indexOf(tag);//？在的位置切 给的url 在?切
        url = url.substring(0, index);
        return url;
    }


    public static void downloadFile(final String url, final String destFileDir, Callback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .build();
        final Call call = new OkHttpClient().newCall(request);
        call.enqueue(callback);
    }
}

