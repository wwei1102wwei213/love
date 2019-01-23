package com.yyspbfq.filmplay.utils.sp;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yyspbfq.filmplay.bean.UserInfo;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/29 0029.
 *
 * 用户基本信息帮助类
 *
 * @author wwei
 */
public class UserDataUtil {

    private static String LoginType;   //登陆模式 1为账号登陆、2为google登陆、3为facebook登陆、4为游客登陆

    private static UserInfo mUserInfo;

    public static UserInfo getUserInfo(Context context) {
        initUserInfo(context);
        return mUserInfo==null?(new UserInfo()):mUserInfo;
    }

    public static String getLoginType(Context context) {
        if (TextUtils.isEmpty(LoginType)) {
            LoginType = SharePrefUtil.getString(context, "film_user_login_type", "0");
        }
        return LoginType;
    }

    public static void saveLoginType(Context context, String type) {
        SharePrefUtil.saveString(context, "film_user_login_type", type);
        LoginType = type;
    }

    public static boolean isLogin (Context context) {
        String type = getLoginType(context);
        if ("2".equals(type)) return true;
        return false;
    }

    public static String getUserID(Context context) {
        initUserInfo(context);
        if (mUserInfo==null) return "";
        if (TextUtils.isEmpty(mUserInfo.id)) return "";
        return mUserInfo.id;
    }

    private static void initUserInfo(Context context) {
        if (mUserInfo==null) {
            String userData = getUserData(context);
            if (!TextUtils.isEmpty(userData)) {
                try {
                    mUserInfo = new Gson().fromJson(userData, UserInfo.class);
                } catch (Exception e){

                }
            }
        }
    }

    private static String getUserData(Context context) {
        return SharePrefUtil.getString(context, "film_user_info", "");
    }

    public static void saveUserData(Context context, UserInfo userInfo) {
        SharePrefUtil.saveString(context, "film_user_info", new Gson().toJson(userInfo));
        mUserInfo = null;
        initUserInfo(context);
    }

    public static <T> void saveDataList(Context context, String key, List<T> list){
        SharePrefUtil.saveDataList(context, key, list);
    }

    public static <T> List<T> getDataList(Context context, String key) {
        List<T> datalist = new ArrayList<>();
        try {
            datalist = SharePrefUtil.getDataList(context, key);
        } catch (Exception e){
            BLog.e(e);
        }
        return datalist;
    }

    public static void clearUserData(Context context){
        LoginType = null;
        SharePrefUtil.clear(context);
    }

}
