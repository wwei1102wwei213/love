package com.fuyou.play.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.fuyou.play.R;
import com.fuyou.play.service.BroadcastManager;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tom on 2018/5/24.
 */

public class WXUtil {

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public WXUtil(Context context) {
        this.context = context;
    }

    public WXUtil(Context context, String action, ShareSuccessListener listener) {
        this.context = context;
        this.action = action;
        this.listener = listener;
    }

    public void setShare_from(int share_from) {
        this.share_from = share_from;
    }


    ////////////////////////////////////////

    public static final String TAG = "WXUtil";

    private Context context;
    private int share_from;
    private String action;
    private ShareSuccessListener listener;

    public static final int SHARE_FROM_SIGNLE_CARD = 0x111;
    public static final int SHARE_FROM_SHARD_APP = 0x222;
    public static final int SHARE_FROM_2COLLECT_CARD = 0x333;

    private void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }


    /**
     * 微信网页分享
     *
     * @param //https://blog.csdn.net/leap2018/article/details/53557076 // https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=&lang=zh_CN
     *                                                                  <p>
     *                                                                  weburl- 网页URL
     *                                                                  图片本地路径 -imgurl
     *                                                                  网页标题 -title
     *                                                                  网页内容摘要 -description
     *                                                                  sendtype(0:分享到微信好友，1：分享到微信朋友圈)
     */
    public void wechatShareURL(String weburl, String imgurl, String title, String description, int sendtype) {

//        showToast("分享即将开放");

        if (!mWxApi.isWXAppInstalled()) {
            showToast("您还未安装微信客户端");
        } else {




            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = weburl;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title;
            msg.description = description;
//            Bitmap bmp = BitmapFactory.decodeFile(imgurl);
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
//            msg.setThumbImage(thumbBmp);
//            bmp.recycle();

            //替换一张自己工程里的图片资源
            Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            msg.setThumbImage(thumb);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = sendtype == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
            mWxApi.sendReq(req);
        }
    }


    static final int THUMB_SIZE = 60;

    /***
     * 分享图片到聊天界面
     */
    public void shareSignleCard() {
        if (!mWxApi.isWXAppInstalled()) {
            showToast("您还未安装微信客户端");
        } else {



            showToast("正在生成分享内容....");//图片分享这里会等一会儿、所以给个提示


//            shareNetImage(HttpFlag.Card_share_url);
        }
    }


    /***
     * 分享图片到聊天界面  分享的是一张网络图片
     */

    public void shareNetImage(final String url) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    // 创建WXImageObject对象，包装bitmap
                    WXImageObject image = new WXImageObject();
//                    image.imagePath = url;
                    image.imageData = PhotoUtils.getImageFromURL(url);
                    // 创建对象，并且包装WXImageObject对象
                    WXMediaMessage msg = new WXMediaMessage();
                    msg.mediaObject = image;

                    Bitmap bitmap = BitmapFactory.decodeStream(new URL(url).openStream());
                    // 压缩图片--宽度120 高度150的图像
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
                    bitmap.recycle();// 回收图片占用的内存资源
                    // 设置缩略图
                    msg.thumbData = bmpToByteArray(thumbBmp, true);

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("img");
                    req.message = msg;
                    // 场景-标示发送给朋友还是朋友圈
                    req.scene = SendMessageToWX.Req.WXSceneSession;// 聊天界面
                    //req.scene =  SendMessageToWX.Req.WXSceneTimeline;// 朋友圈
                    mWxApi.sendReq(req);
                    Toast.makeText(context, String.valueOf(mWxApi.sendReq(req)), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    /***
     * 分享图片到聊天界面  分享的是一张res下的图片
     */
//    public void shareSignleCard2() {
//        if (!mWxApi.isWXAppInstalled()) {
//            showToast("您还未安装微信客户端");
//        } else {
//
//            try {
//                switch (share_from) {
//                    case SHARE_FROM_SIGNLE_CARD: {   /* 用户点击分享单张牌
//                            e1p7o4*/
//                        AdjustEvent event2 = new AdjustEvent("e1p7o4");
//                        Adjust.trackEvent(event2);
//                    }
//                    break;
//                    case SHARE_FROM_SHARD_APP: {   /*用户点击分享APP
//                            z9pq98*/
//                        AdjustEvent event2 = new AdjustEvent("z9pq98");
//                        Adjust.trackEvent(event2);
//                    }
//                    break;
//                    case SHARE_FROM_2COLLECT_CARD: {     /*用户点击分享集卡
//                        5qfhl9*/
//                        AdjustEvent event2 = new AdjustEvent("5qfhl9");
//                        Adjust.trackEvent(event2);
//                    }
//                    break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            showToast("正在生成分享内容....");//图片分享这里会等一会儿、所以给个提示
//
//            //https://blog.csdn.net/xiong_it/article/details/48317527/
//            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.share_signle_card);//加载一张res下的图片
////初始化wXImageObject和wxXMediaMessage对象
//            WXImageObject imgObj = new WXImageObject(bmp);
//            WXMediaMessage msg = new WXMediaMessage();
//            msg.mediaObject = imgObj;
////设置缩略图
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//            bmp.recycle();
//            msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
////构造一个Req
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = buildTransaction("img");//transaction字段用于唯一标识一 个请求
//            req.message = msg;
////        req.scene =  isTimelineCb.isChecked() ? SendMessageToWX.Req.wXSceneTimeline : SendMessageToWX.Req.wXSceneSession;
//
//            req.scene = SendMessageToWX.Req.WXSceneSession;// 聊天界面
//            //req.scene =  SendMessageToWX.Req.WXSceneTimeline;// 朋友圈
//
//
////调用api接口发送数据到微信
//            mWxApi.sendReq(req);
//        }
//    }


    public IWXAPI mWxApi;

    public void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(context, Const.CONTACTS_WX_APPID, true);
        // 将该app注册到微信
        mWxApi.registerApp(Const.CONTACTS_WX_APPID);


        BroadcastManager.getInstance(context).addAction(!TextUtils.isEmpty(action) ? action : Const.RECEIVER_ACTION_WXSHARE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null) {
                    try {
                        String code = intent.getStringExtra(BroadcastManager.EXTRA_STRING);
                        onResp(code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void onResp(String code) {
        LogCustom.e(TAG, "onResp" + code);

        if ((BaseResp.ErrCode.ERR_OK + "").equals(code)) {
            //分享成功
            showToast("分享成功");
            if (listener != null) {
                listener.onShareSuccess(true);
            }



        } else if ((BaseResp.ErrCode.ERR_USER_CANCEL + "").equals(code)) {
            if (listener != null) {
                listener.onShareSuccess(false);
            }
            //分享取消
            showToast("分享取消");

        } else if ((BaseResp.ErrCode.ERR_AUTH_DENIED + "").equals(code)) {
            //分享拒绝
            /*if (listener!=null) {
                listener.onShareSuccess(false);
            }*/
//            showToast("分享拒绝");

        }
    }

    public void onDestroy() {
        BroadcastManager.getInstance(context).destroy(TextUtils.isEmpty(action) ? Const.RECEIVER_ACTION_WXSHARE : action);
    }

    public interface ShareSuccessListener {
        void onShareSuccess(boolean isSuccess);
    }


    ///////////////////////////////////////////////////////////////
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws Exception {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }


    /**
     * 根据一个网络连接(String)获取bitmap图像
     *
     * @param imageUri
     * @return
     * @throws Exception
     */
    public static Bitmap getbitmap(String imageUri) {
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    ///////////////////////////////////////////////////////////////

}
