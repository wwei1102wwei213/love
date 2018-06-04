package com.fuyou.play.util.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;


/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class SPLongUtils {

    private final static String SP_NAME = "milu_long_config";

    /**
     * 保存字符串
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key, String value) {
        try {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
            LogCustom.show(TextUtils.isEmpty(value)?"version_config:null":"version_config:"+value);
            sp.edit().putString(key, value).apply();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"偏好设置出错");
        }
    }

    /**
     * 获取字符值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        try {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
            return sp.getString(key, defValue);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"偏好设置出错");
        }
        return defValue;
    }

    /**
     * 获取字符值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static <T> Object getString(Context context, String key, String defValue, Class<T> mClass) {
        try {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
            String result = sp.getString(key, defValue);
//            LogCustom.show(TextUtils.isEmpty(result)?"version_config:null":"version_config:"+result);
            if (TextUtils.isEmpty(result)) {
                return defValue;
            }
            Object o = new Gson().fromJson(result, mClass);
            return o;
        } catch (JsonParseException je){
            ExceptionUtils.ExceptionSend(je,"偏好设置出错");
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"偏好设置出错");
        }
        return defValue;
    }


}
