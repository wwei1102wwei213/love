package com.fuyou.play.wxapi;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fuyou.play.LApplication;
import com.fuyou.play.service.BroadcastManager;
import com.fuyou.play.util.Const;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.view.BaseActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            IWXAPI mWxApi = WXAPIFactory.createWXAPI(this, Const.CONTACTS_WX_APPID, true);
            // 将该app注册到微信
            mWxApi.registerApp(Const.CONTACTS_WX_APPID);
            mWxApi.handleIntent(getIntent(), this);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "onCreate");
        }
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                try {
                    switch (resp.getType()) {
                        case RETURN_MSG_TYPE_SHARE:
                            LogCustom.show(resp.getType()+"");
                            LogCustom.show("LApplication.SHARE_TAG:"+ LApplication.SHARE_TAG);
//                            BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_WXSHARE, resp.errCode + "");
                            /*if (LApplication.SHARE_TAG == 4) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_4, resp.errCode + "");
                            } else if (LApplication.SHARE_TAG == 3) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_3, resp.errCode + "");
                            } else if (LApplication.SHARE_TAG == 2) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_2, resp.errCode + "");
                            }else if (LApplication.SHARE_TAG == 1) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_1, resp.errCode + "");
                            } else {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_WXSHARE, resp.errCode + "");
                            }*/
                            break;
                    }

                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onResp ERR_AUTH_DENIED");
                }

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                try {
                    switch (resp.getType()) {
                        case RETURN_MSG_TYPE_SHARE:
//                            BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_WXSHARE, resp.errCode + "");
                            if (LApplication.SHARE_TAG == 4) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_4, resp.errCode + "");
                            } else if (LApplication.SHARE_TAG == 3) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_3, resp.errCode + "");
                            } else if (LApplication.SHARE_TAG == 2) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_2, resp.errCode + "");
                            }else if (LApplication.SHARE_TAG == 1) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_1, resp.errCode + "");
                            } else {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_WXSHARE, resp.errCode + "");
                            }
                            break;
                    }

                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onResp ERR_USER_CANCEL");
                }

                break;
            case BaseResp.ErrCode.ERR_OK:
                try {
                    switch (resp.getType()) {
                        case RETURN_MSG_TYPE_LOGIN:
                            //拿到了微信返回的code,立马再去请求access_token
                            String code = ((SendAuth.Resp) resp).code;
                            //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                            BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_WXLOGIN, code);
                            break;
                        case RETURN_MSG_TYPE_SHARE:
                            if (LApplication.SHARE_TAG == 4) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_4, resp.errCode + "");
                            } else if (LApplication.SHARE_TAG == 3) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_3, resp.errCode + "");
                            } else if (LApplication.SHARE_TAG == 2) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_2, resp.errCode + "");
                            }else if (LApplication.SHARE_TAG == 1) {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_SHARE_1, resp.errCode + "");
                            } else {
                                BroadcastManager.getInstance(this).sendBroadcast(Const.RECEIVER_ACTION_WXSHARE, resp.errCode + "");
                            }
                            break;
                    }
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onResp ERR_OK");
                }

                break;

        }
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    /*public IWXAPI mWxApi;
    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        // 将该app注册到微信
        mWxApi.registerApp(APP_ID);
    }

    private void wxLogin() {
        if (!mWxApi.isWXAppInstalled()) {
            showToast("您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login_12121212";
        mWxApi.sendReq(req);
    }*/
}