package com.doojaa.base.utils.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.doojaa.base.utils.BLog;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;


/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class SPLongUtils {

    private final static String SP_NAME = "fuyou_long_config";

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
            sp.edit().putString(key, value).apply();
        }catch (Exception e){
            BLog.e(e);
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
            BLog.e(e);
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
            if (TextUtils.isEmpty(result)) {
                return defValue;
            }
            Object o = new Gson().fromJson(result, mClass);
            return o;
        } catch (JsonParseException je){
            BLog.e(je);
        } catch (Exception e){
            BLog.e(e);
        }
        return defValue;
    }


}
