package com.fuyou.play.biz.http;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.fuyou.play.util.AES;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.fuyou.play.util.Const;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;



/**
 * Created by Administrator on 2017/8/29 0029.
 *
 * 网络访问工具类
 *
 * @author wwei
 */
public class OkHttpClientManager {

    private static final MediaType MEDIA_TYPE_JSON =
            MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_X_WWW =
            MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_NORM =
            MediaType.parse("application/x-www-form-urlencoded");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_STREAM =
            MediaType.parse("application/octet-stream;charset=utf-8");
    private static final MediaType MEDIA_TYPE_MARKDOWN =
            MediaType.parse("text/x-markdown; charset=utf-8");//mdiatype 这个需要和服务端保持一致

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;
    private PostNewDelegate mNewPostDelegate = new PostNewDelegate();
    private UploadDelegate mUploadDelegate = new UploadDelegate();
    private GetDelegate mGetDelegate = new GetDelegate();

    private OkHttpClientManager() {

        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        //Cache cache = new Cache();//这儿可以做缓存
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(2, TimeUnit.MINUTES);
        mOkHttpClient.setWriteTimeout(2, TimeUnit.MINUTES);
        //信任所有证书
        mOkHttpClient.setSslSocketFactory(createSSLSocketFactory());
        mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public PostNewDelegate getPostNewDelegate() {
        return mNewPostDelegate;
    }

    public UploadDelegate getUploadDelegate() { return mUploadDelegate; }

    public GetDelegate getDelegate(){
        return mGetDelegate;
    }

    /**
     * ============POST方便的访问方式===============
     */
    public static void post(String url, Map<String, String> params, final ResultCallback callback) {
//        LogCustom.i(Const.LOG_TAG_HTTP, "请求参数是：" + params.toString());
        getInstance().getPostNewDelegate().postAsyn(url, params, callback, null);
    }

    public static void postJson(String url, Map<String, Object> params, final ResultCallback callback) {
//        LogCustom.i(Const.LOG_TAG_HTTP, "请求参数是：" + params.toString());
        getInstance().getPostNewDelegate().postJson(url, params, callback, null);
    }

    public static void uploadPost(String url, String fileKey, File file, Map<String, String> params, final ResultCallback callback){
        LogCustom.i(Const.LOG_TAG_HTTP, "请求参数是：" + params.toString());
        getInstance().getUploadDelegate().postUpload(url,fileKey,file,params,callback,null);
    }

    public static void multiUploadPost(String url, String[] fileKeys, File[] files, Map<String, String> params, final ResultCallback callback) {
        LogCustom.i(Const.LOG_TAG_HTTP, "请求参数是：" + params.toString());
        getInstance().getUploadDelegate().postMultiUpload(url, fileKeys, files, params, callback,null);
    }

    public static void uploadPostJson(String url, String fileKey, File file, Map<String, Object> params, final ResultCallback callback){
        getInstance().getUploadDelegate().postUploadForJson(url,fileKey,file,params,callback,null);
    }

    public static void multiUploadPostJson(String url, String[] fileKeys, File[] files, Map<String, Object> params, final ResultCallback callback) {
        getInstance().getUploadDelegate().postMultiUploadForJson(url, fileKeys, files, params, callback,null);
    }

    public static void get(String url, final ResultCallback callback, Object tag) {
        getInstance().getDelegate().getAsyn(url,callback, null);
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private Param[] map2NoEmptyParams(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            if (!TextUtils.isEmpty(entry.getKey())){
                if (!TextUtils.isEmpty(entry.getValue())){
                    res[i++] = new Param(entry.getKey(), entry.getValue());
                }else {
                    res[i++] = new Param(entry.getKey(), "");
                }
            }
        }
        return res;
    }
    private Param[] map2NoEmptyParamsFY(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[1];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : entries) {
            if (!TextUtils.isEmpty(entry.getKey())){
                if (i==0) {
                    i++;
                } else {
                    sb.append("&");
                }
                sb.append(URLEncoder.encode(entry.getKey()));
                sb.append("=");
                if (!TextUtils.isEmpty(entry.getValue())){
                    sb.append(URLEncoder.encode(entry.getValue()));
                }else {
                    sb.append("");
                }
            }
        }
        String temp = sb.toString();
        if (TextUtils.isEmpty(temp)) {
            res[0] = new Param("params", "");
        } else {
            res[0] = new Param("params", AES.encode(temp));
        }
        return res;
    }
    /*String temp = "";
                for (Param p:params){
                    if (TextUtils.isEmpty(temp)){
                        temp += p.key + "=" + p.value;
                    }else {
                        temp += "&" + p.key + "=" + p.value;
                    }
                }
                LogCustom.i(Const.LOG_TAG_HTTP,"请求参数："+temp);*/

    private void deliveryResult(ResultCallback callback, final Request request) {
        if (callback == null) callback = DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        //UI thread
        callback.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();

                    LogCustom.i(Const.LOG_TAG_HTTP, "返回数据："+response.request().toString()+ string);
                    if (resCallBack.mType == String.class) {
                        sendSuccessResultCallback(string, resCallBack);
                    } else {
                        Object o = mGson.fromJson(string, resCallBack.mType);
                        sendSuccessResultCallback(o, resCallBack);
                    }

                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }

            }
        });
    }

    private void mcDeliveryResult(ResultCallback callback, Request request) {
        if (callback == null) callback = DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        //UI thread
        callback.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                LogCustom.i(Const.LOG_TAG_HTTP, "请求失败异常信息：" + request.toString());
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
//                    LogCustom.i(Const.LOG_TAG_HTTP, "请求原始数据：" + string);
                    sendSuccessResultCallback(string, resCallBack);

                } catch (Exception e) {
                    LogCustom.i(Const.LOG_TAG_HTTP, "请求成功,IO异常"+response.request().toString()+"异常信息：" + e.toString());
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }
            }
        });
    }



    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    private Request buildPostFormRequest(String url, Param[] params, Object tag) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();


        Request.Builder reqBuilder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("mc_platform", "android")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("mc_os_version", Build.VERSION.SDK_INT+"")
                .addHeader("mc_appid", "2001");

        reqBuilder.url(url)
                .post(requestBody);

        if (tag != null) {
            reqBuilder.tag(tag);
        }
        return reqBuilder.build();
    }

    private Request buildPostForJson(String url, Map<String, Object> params, Object tag) {
        String json = "";
        if (params != null){
            json = mGson.toJson(params);
        }
        LogCustom.i(Const.LOG_TAG_HTTP, "请求参数是：" + json);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request.Builder reqBuilder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("mc_platform", "android")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("mc_os_version", Build.VERSION.SDK_INT+"")
                .addHeader("mc_appid", "2001");

        reqBuilder.url(url)
                .post(requestBody);
        if (tag != null) {
            reqBuilder.tag(tag);
        }
        return reqBuilder.build();
    }

    public static abstract class ResultCallback<T> {
        Type mType;
        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }
        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }
        public void onBefore(Request request) {
        }
        public void onAfter() {
        }
        public abstract void onError(Request request, Exception e);
        public abstract void onResponse(T response);
    }

    private final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {
        }
        @Override
        public void onResponse(String response) {
        }
    };


    public static class Param {
        public Param() {}
        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
        String key;
        String value;
    }

    public class GetDelegate {
        private Request buildGetRequest(String url, Object tag) {
            Request.Builder builder = new Request.Builder()
                    .addHeader("Connection", "keep-alive")
                    .addHeader("mc_platform", "android")
                    .addHeader("phoneModel", Build.MODEL)
                    .addHeader("systemVersion", Build.VERSION.RELEASE)
                    .url(url);
            if (tag != null) {
                builder.tag(tag);
            }
            return builder.build();
        }

        /**
         * 通用的方法
         */
        public Response get(Request request) throws IOException {
            Call call = mOkHttpClient.newCall(request);
            Response execute = call.execute();
            return execute;
        }

        /**
         * 同步的Get请求
         */
        public Response get(String url) throws IOException {
            return get(url, null);
        }
        public Response get(String url, Object tag) throws IOException {
            final Request request = buildGetRequest(url, tag);
            return get(request);
        }

        /**
         * 同步的Get请求
         */
        public String getAsString(String url) throws IOException {
            return getAsString(url, null);
        }

        public String getAsString(String url, Object tag) throws IOException {
            Response execute = get(url, tag);
            return execute.body().string();
        }

        /**
         * 通用的方法
         */
        public void getAsyn(Request request, ResultCallback callback) {
            deliveryResult(callback, request);
        }

        /**
         * 异步的get请求
         */
        public void getAsyn(String url, final ResultCallback callback) {
            getAsyn(url, callback, null);
        }

        public void getAsyn(String url, final ResultCallback callback, Object tag) {
            LogCustom.i(Const.LOG_TAG_HTTP, "getAsyn():" + url);
            final Request request = buildGetRequest(url, tag);
            getAsyn(request, callback);
        }
    }

    //====================UploadDelegate=======================

    /**
     * 上传相关的模块
     */
    public class UploadDelegate {
        /**
         * 同步基于post的文件上传:上传单个文件
         */
        public Response post(String url, String fileKey, File file, Object tag) throws IOException {
            return post(url, new String[]{fileKey}, new File[]{file}, null, tag);
        }

        /**
         * 同步基于post的文件上传:上传多个文件以及携带key-value对：主方法
         */
        public Response post(String url, String[] fileKeys, File[] files, Param[] params, Object tag) throws IOException {
            Request request = buildMultipartFormRequest(url, files, fileKeys, params, tag);
            return mOkHttpClient.newCall(request).execute();
        }

        /**
         * 同步单文件上传
         */
        public Response post(String url, String fileKey, File file, Param[] params, Object tag) throws IOException {
            return post(url, new String[]{fileKey}, new File[]{file}, params, tag);
        }

        /**
         * 异步基于post的文件上传:主方法
         */
        public void postAsyn(String url, String[] fileKeys, File[] files, Param[] params, ResultCallback callback, Object tag) {
            Request request = buildMultipartFormRequest(url, files, fileKeys, params, tag);
            mcDeliveryResult(callback, request);
        }

        public void postAsynForJson(String url, String[] fileKeys, File[] files, Map<String, Object> params, ResultCallback callback, Object tag) {
            Request request = buildMultipartForJson(url, files, fileKeys, params, tag);
            mcDeliveryResult(callback, request);
        }

        /**
         * 异步基于post的文件上传，单文件且携带其他form参数上传
         */
        public void postUploadForJson(String url, String fileKey, File file, Map<String, Object> paramsMap, ResultCallback callback, Object tag) {
            postAsynForJson(url, new String[]{fileKey}, new File[]{file}, paramsMap, callback, tag);
        }

        /**
         * 异步基于post的文件上传，多文件且携带其他form参数上传
         */
        public void postMultiUploadForJson(String url, String[] fileKeys, File[] files, Map<String, Object> paramsMap, ResultCallback callback, Object tag) {
            postAsynForJson(url, fileKeys, files, paramsMap, callback, tag);
        }

        /**
         * 异步基于post的文件上传，单文件且携带其他form参数上传
         */
        public void postUpload(String url, String fileKey, File file, Map<String, String> paramsMap, ResultCallback callback, Object tag) {
            Param[] paramsArr = map2NoEmptyParams(paramsMap);
            postAsyn(url, new String[]{fileKey}, new File[]{file}, paramsArr, callback, tag);
        }

        /**
         * 异步基于post的文件上传，多文件且携带其他form参数上传
         */
        public void postMultiUpload(String url, String[] fileKeys, File[] files, Map<String, String> paramsMap, ResultCallback callback, Object tag) {
            Param[] paramsArr = map2NoEmptyParams(paramsMap);
            postAsyn(url, fileKeys, files, paramsArr, callback, tag);
        }

        private Request buildMultipartFormRequest(String url, File[] files,
                                                  String[] fileKeys, Param[] params, Object tag) {
            params = validateParam(params);

            MultipartBuilder builder = new MultipartBuilder()
                    .type(MultipartBuilder.FORM);

            for (Param param : params) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                        RequestBody.create(null, param.value));
            }
            if (files != null) {
                RequestBody fileBody = null;
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file != null && file.exists()) {
                        String fileName = file.getName();
                        fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                        //TODO 根据文件名设置contentType
                        builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                                fileBody);
                    }
                }
            }
            RequestBody requestBody = builder.build();
            Request.Builder reqBuilder = new Request.Builder()
                    .addHeader("Connection", "keep-alive")
                    .addHeader("mc_platform", "android")
                    .addHeader("phoneModel", Build.MODEL)
                    .addHeader("systemVersion", Build.VERSION.RELEASE);
            return reqBuilder.url(url)
                    .post(requestBody)
                    .tag(tag)
                    .build();

        }

        private Request buildMultipartForJson(String url, File[] files,
                                                  String[] fileKeys, Map<String, Object> params, Object tag) {
            MultipartBuilder builder = new MultipartBuilder()
                    .type(MultipartBuilder.FORM);
            if (params!=null&&params.size()>0) {
                /*for (String key:params.keySet()) {
                    builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                            RequestBody.create(null, params.get(key)+""));
                }*/
                String json = mGson.toJson(params);
                LogCustom.i(Const.LOG_TAG_HTTP, "请求参数是：" + json);
                builder.addPart(Headers.of("Content-Disposition", "form-data;"),
                        RequestBody.create(MEDIA_TYPE_JSON, json));
            }

            if (files != null) {
                RequestBody fileBody = null;
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file != null && file.exists()) {
                        String fileName = file.getName();
                        fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                        //TODO 根据文件名设置contentType
                        builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                                fileBody);
                    }
                }
            }
            RequestBody requestBody = builder.build();
            Request.Builder reqBuilder = new Request.Builder()
                    .addHeader("Connection", "keep-alive")
                    .addHeader("mc_platform", "android")
                    .addHeader("phoneModel", Build.MODEL)
                    .addHeader("systemVersion", Build.VERSION.RELEASE);
            return reqBuilder.url(url)
                    .post(requestBody)
                    .tag(tag)
                    .build();
        }

    }

    //====================HttpsDelegate=======================
    /**
     * Https相关模块
     */
    public class HttpsDelegate {
        public void setCertificates(InputStream... certificates) {
            setCertificates(certificates, null, null);
        }
        public TrustManager[] prepareTrustManager(InputStream... certificates) {
            if (certificates == null || certificates.length <= 0) return null;
            try {

                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                int index = 0;
                for (InputStream certificate : certificates) {
                    String certificateAlias = Integer.toString(index++);
                    keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                    try {
                        if (certificate != null)
                            certificate.close();
                    } catch (IOException e)

                    {
                    }
                }
                TrustManagerFactory trustManagerFactory = null;

                trustManagerFactory = TrustManagerFactory.
                        getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);

                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

                return trustManagers;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        public KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
            try {
                if (bksFile == null || password == null) return null;

                KeyStore clientKeyStore = KeyStore.getInstance("BKS");
                clientKeyStore.load(bksFile, password.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(clientKeyStore, password.toCharArray());
                return keyManagerFactory.getKeyManagers();

            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
            try {
                TrustManager[] trustManagers = prepareTrustManager(certificates);
                KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
                SSLContext sslContext = SSLContext.getInstance("TLS");

                sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
                mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }

        private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        }


        public class MyTrustManager implements X509TrustManager {
            private X509TrustManager defaultTrustManager;
            private X509TrustManager localTrustManager;

            public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
                TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                var4.init((KeyStore) null);
                defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
                this.localTrustManager = localTrustManager;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                try {
                    defaultTrustManager.checkServerTrusted(chain, authType);
                } catch (CertificateException ce) {
                    localTrustManager.checkServerTrusted(chain, authType);
                }
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }

    }

    public class PostNewDelegate {
        private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/x-www-form-urlencoded");
        private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");


        public void postAsyn(String url, Map<String, String> params, final ResultCallback callback, Object tag) {
            Param[] paramsArr = map2NoEmptyParamsFY(params);
            mcPostAsyn(url, paramsArr, callback, tag);
        }

        public void mcPostAsyn(String url, Param[] params, final ResultCallback callback, Object tag) {
            try {
                Request request = buildPostFormRequest(url, params, tag);
                mcDeliveryResult(callback, request);
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"OKHTTP mcPostAsyn");
            }
        }

        public void postJson(String url, Map<String, Object> params, final ResultCallback callback, Object tag) {
            try {
                Request request = buildPostForJson(url, params, tag);
                mcDeliveryResult(callback, request);
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"OKHTTP postJson");
            }
        }

        /**
         * 同步的Post请求:直接将bodyFile以写入请求体
         */
        public Response post(String url, File bodyFile) throws IOException {
            return post(url, bodyFile, null);
        }

        public Response post(String url, File bodyFile, Object tag) throws IOException {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyFile);
            Request request = buildPostRequest(url, body, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        /**
         * 直接将bodyFile以写入请求体
         */
        public void postAsynWithMediaType(String url, File bodyFile, MediaType type, final ResultCallback callback, Object tag) {
            RequestBody body = RequestBody.create(type, bodyFile);
            Request request = buildPostRequest(url, body, tag);
            deliveryResult(callback, request);
        }


        /**
         * post构造Request的方法
         *
         * @param url
         * @param body
         * @return
         */
        private Request buildPostRequest(String url, RequestBody body, Object tag) {
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .post(body);
            if (tag != null) {
                builder.tag(tag);
            }
            Request request = builder.build();
            return request;
        }


    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }


    /**
     *
     * post同步请求
     * @param actionUrl  接口地址
     * @param paramsMap   请求参数
     * @Time 2016-12-26
     * @author wwei
     *
     */
    private String requestPostBySyn(String actionUrl, Map<String, String> paramsMap) {
        try {
            //处理参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址
//            String requestUrl = String.format("%s/%s", BASE_URL, actionUrl);
            //生成参数
            String params = tempParams.toString();
            //创建一个请求实体对象 RequestBody
            RequestBody body = RequestBody.create(MEDIA_TYPE_X_WWW, params.getBytes());
            //创建一个请求
            final Request request = addHeaders().url(actionUrl).post(body).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            Response response = call.execute();
            //请求执行成功
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e,"postBySyn");
        }
        return null;
    }

    /**
     *
     * post同步请求
     * @param actionUrl  接口地址
     * @param
     * @Time 2016-12-26
     * @author wwei
     *
     */
    private String requestPostBySynForSecret(String actionUrl, byte[] param) {
        try {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, param);
            //创建一个请求
            final Request request = addHeaders().url(actionUrl).post(body).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            Response response = call.execute();
            //请求执行成功
            if (response.isSuccessful()) {
                String rep = response.body().string();
                return rep;
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e,"postBySynForSerect");
        }
        return null;
    }

    /**
     * 统一为请求添加头信息
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("platform", "2")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE);

        return builder;
    }

    public static String postAsyn(String url, Map<String,String> map, int tag) {

        return getInstance().requestPostBySyn(url,map);
    }

    public static String postAsyn(String url, byte[] param, int tag) {
        return getInstance().requestPostBySynForSecret(url, param);

    }

    /*public static void main(String[] args){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String p = "{\"moonday\":\"2017-10-16\"}";
                String req = OkHttpClientManager.postAsyn("https://h37ohezyj1.execute-api.us-east-1.amazonaws.com/dev/moonphrase",p.getBytes(),1);
                LogCustom.show(Const.LOG_TAG_HTTP, req);
            }
        }).start();

    }*/

}
