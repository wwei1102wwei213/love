package com.yyspbfq.filmplay.biz.login;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.ui.WLibDialogHelper;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.MessageEvent;
import com.yyspbfq.filmplay.bean.UserInfoBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.SystemUtils;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;


/**
 * 用户相关接口辅助类
 * Created by tankai on 2016/7/3.
 */
public class UserHelper {

    private static UserHelper userHelper;

    private UserHelper() {
    }

    public static synchronized UserHelper getInstance() {
        if (userHelper == null) {
            userHelper = new UserHelper();
        }
        return userHelper;
    }

    public void getUserInfo(Context context) {
        getUserInfo(context, false);
    }

    public void getUserInfo(Context context, final boolean needProgress) {
        getUserInfo(context, needProgress, false);
    }

    /**
     * 获取用户信息
     *
     * @param isLoading 是否开启加载浮层
     * @param isLogin   是否是登陆后获取用户信息
     */
    public void getUserInfo(final Context mContext, final boolean isLoading, final boolean isLogin) {
        getUserInfo(mContext, isLoading, isLogin, false);
    }

    /**
     * 获取用户信息
     *
     * @param isLoading 是否开启加载浮层
     * @param isLogin   是否是登陆后获取用户信息
     */
    public void getUserInfo(final Context mContext, final boolean isLoading, final boolean isLogin, final boolean isPull) {
        Logger.d("网络刷新用户信息");
        final Dialog userProgressDialog = WLibDialogHelper.createProgressDialog(mContext, mContext.getString(R.string.msg_loading_userinfo));
        Factory.resp(new WLibHttpListener() {
            @Override
            public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
                try {
                    UserInfoBean bean = (UserInfoBean) formatData;
                    UserDataUtil.saveLoginType(BaseApplication.getInstance(), bean.getType()+"");
                    UserDataUtil.saveUserData(BaseApplication.getInstance(), bean.getData());
                    MessageEvent event = new MessageEvent();
                    event.setMessage(MessageEvent.MSG_GET_USER_INFO);
                    EventBus.getDefault().post(event);
                } catch (Exception e){
                    BLog.e(e);
                }
            }

            @Override
            public void handleLoading(int flag, Object tag, boolean isShow) {
                if (isLoading&&isShow)
                    WLibDialogHelper.show(userProgressDialog);
            }

            @Override
            public void handleError(int flag, Object tag, int errorType, String response, String hint) {

            }

            @Override
            public void handleAfter(int flag, Object tag) {
                if (isLoading)
                    WLibDialogHelper.dismiss(userProgressDialog);
            }
        }, HttpFlag.FLAG_USER_INFO, null, UserInfoBean.class).post(null);

    }

    public void getUserInfo() {
        Logger.d("网络刷新用户信息");
        Factory.resp(new WLibHttpListener() {
            @Override
            public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
                try {
                    UserInfoBean bean = (UserInfoBean) formatData;
                    UserDataUtil.saveLoginType(BaseApplication.getInstance(), bean.getType()+"");
                    UserDataUtil.saveUserData(BaseApplication.getInstance(), bean.getData());
                    MessageEvent event = new MessageEvent();
                    event.setMessage(MessageEvent.MSG_GET_USER_INFO);
                    EventBus.getDefault().post(event);
                } catch (Exception e){
                    BLog.e(e);
                }
            }

            @Override
            public void handleLoading(int flag, Object tag, boolean isShow) {

            }

            @Override
            public void handleError(int flag, Object tag, int errorType, String response, String hint) {

            }

            @Override
            public void handleAfter(int flag, Object tag) {

            }
        }, HttpFlag.FLAG_USER_INFO, null, UserInfoBean.class).post(null);

    }

    /*public void loginMobile(final Context mContext, final Dialog dialog, String phone, String code) {

        if (dialog!=null) dialog.dismiss();

        final Dialog loginProgressDialog = DialogUtil.createProgressDialog(mContext, mContext.getString(R.string.msg_loading_login));

        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("phoneNum", phone);
        params.put("verifyCode", code);
        params.put("device", SystemUtils.getIMEI());
        params.put("act_channel", SystemUtils.getChannelActivity(AppUtils.getContext()));
        params.put("channel", SystemUtils.getChannel(AppUtils.getContext()));
        String mInviteCode = SystemUtils.getChannelFromClip(mContext);
        if (!TextUtils.isEmpty(mInviteCode)) {
            params.put("invite", mInviteCode);
        } else {
            params.put("invite", PreferencesUtils.getString(mContext, "kkxs_channel_code", ""));
        }
        Factory.resp(new WLibHttpListener() {
            @Override
            public void handleResp(Object response, int flag, Object obj, String source, String hint) {
                try {
                    JSONObject resJson = new JSONObject(source);
                    int status = resJson.optInt("code");
                    String msg = resJson.optString("msg");
                    if (status == 1) {
                        //同步cookie到webview
                        SystemUtils.sychCookie(mContext, HttpFlag.BASE_URL);
                        //获取用户信息
                        UserHelper.getInstance().getUserInfo(mContext, true, true);
                    } else {
                        ToastUtils.showToast(msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showToast("数据异常");
                    e.printStackTrace();
                }
            }

            @Override
            public void showLoading(int flag, Object obj) {
                DialogUtil.show(loginProgressDialog);
            }

            @Override
            public void hideLoading(int flag, Object obj) {
                DialogUtil.dismiss(loginProgressDialog);
            }

            @Override
            public void handleError(int flag, Object obj, int errorType, String source, String hint) {

            }

            @Override
            public void handleAfter(int flag, Object obj) {

            }
        }, HttpFlag.FLAG_LOGIN_MOBILE, null, null, 1).post(params);

    }*/


    /**
     * phoneNum	是	string	手机号
     * verifyCode	是	string	手机验证码
     * device	是	string	机器码
     * invite	否	string	邀请码
     * channel	否	string	渠道号
     * @param mContext
     * @param dialog
     * @param phone
     * @param code
     * @param invite_code
     */
    public void loginMobile(final Context mContext, final Dialog dialog, String phone, String code, String invite_code) {

        if (dialog!=null) dialog.dismiss();

        final Dialog loginProgressDialog = WLibDialogHelper.createProgressDialog(mContext, mContext.getString(R.string.msg_loading_login));

        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("phoneNum", phone);
        params.put("verifyCode", code);
        params.put("device", CommonUtils.getUUID());
        params.put("channel", CommonUtils.getChannelName());
        String temp = SystemUtils.getChannelFromClip(mContext);
        if (TextUtils.isEmpty(temp)) {
            temp = SPLongUtils.getString(mContext, "video_invite_code_clip", "");
        }
        params.put("invite", TextUtils.isEmpty(temp)?"": temp);
        Factory.resp(new WLibHttpListener() {
            @Override
            public void handleResp(Object response, int flag, Object obj, String source, String hint) {
                try {
                    JSONObject resJson = new JSONObject(source);
                    int status = resJson.optInt("code");
                    String msg = resJson.optString("msg");
                    if (status == 1) {
                        //同步cookie到webview
                        SystemUtils.sychCookie(mContext, HttpFlag.BASE_URL);
                        //获取用户信息
                        UserHelper.getInstance().getUserInfo(mContext, true, true);
                    } else {
                        ToastUtils.showToast(msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showToast("数据异常");
                    e.printStackTrace();
                }
            }

            @Override
            public void handleLoading(int flag, Object tag, boolean isShow) {
                if (isShow) {
                    WLibDialogHelper.show(loginProgressDialog);
                } else {
                    WLibDialogHelper.dismiss(loginProgressDialog);
                }
            }

            @Override
            public void handleError(int flag, Object obj, int errorType, String source, String hint) {

            }

            @Override
            public void handleAfter(int flag, Object obj) {

            }
        }, HttpFlag.FLAG_LOGIN_MOBILE, null, null, 1).post(params);

    }

}
