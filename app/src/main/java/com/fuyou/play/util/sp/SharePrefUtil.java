package com.fuyou.play.util.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fuyou.play.util.ExceptionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * SharePreferences操作工具类
 */
public final class SharePrefUtil {

	private final static String SP_NAME = "milu_info_config";
	private static SharedPreferences sp;

	/**
	 * 保存布尔值
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		try {
			if (sp == null)
				sp = context.getSharedPreferences(SP_NAME, 0);
			sp.edit().putBoolean(key, value).apply();
		}catch (Exception e){
			ExceptionUtils.ExceptionSend(e,"偏好设置出错");
		}
	}

	/**
	 * 清除偏好设置数据
	 *
	 * @param context
	 */
	public static void clear(Context context){
		try {
			if (sp == null)
				sp = context.getSharedPreferences(SP_NAME, 0);
			sp.edit().clear().apply();
		}catch (Exception e){
			ExceptionUtils.ExceptionSend(e,"偏好设置清除出错");
		}
	}

	/**
	 * 保存字符串
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveString(Context context, String key, String value) {
		try {
			if (sp == null)
				sp = context.getSharedPreferences(SP_NAME, 0);
			sp.edit().putString(key, value).apply();
		}catch (Exception e){
			ExceptionUtils.ExceptionSend(e,"偏好设置出错");
		}
	}

	/**
	 * 保存long型
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveLong(Context context, String key, long value) {
		try {
			if (sp == null)
				sp = context.getSharedPreferences(SP_NAME, 0);
			sp.edit().putLong(key, value).apply();
		} catch (Exception e){
			ExceptionUtils.ExceptionSend(e,"偏好设置出错");
		}

	}

	/**
	 * 保存int型
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveInt(Context context, String key, int value) {
		try {
			if (sp == null)
				sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
			sp.edit().putInt(key, value).apply();
		}catch (Exception e){
			ExceptionUtils.ExceptionSend(e,"偏好设置出错");
		}

	}

	/**
	 * 保存float型
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveFloat(Context context, String key, float value) {

		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putFloat(key, value).apply();
	}

	/***
	 *
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveStringSet(Context context, String key, Set<String> value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putStringSet(key, value).apply();
	}

	public static Set<String> getStringSet(Context context, String key){
		if(sp == null) sp = context.getSharedPreferences(SP_NAME,0);
		return sp.getStringSet(key,new LinkedHashSet<String>());
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
			if (sp == null)
				sp = context.getSharedPreferences(SP_NAME, 0);
			return sp.getString(key, defValue);
		}catch (Exception e){
			ExceptionUtils.ExceptionSend(e,"偏好设置出错");
		}
		return defValue;
	}

	/**
	 * 获取int值
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		try {
			if (sp == null)
				sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
			return sp.getInt(key, defValue);
		}catch (Exception e){
			ExceptionUtils.ExceptionSend(e,"偏好设置出错");
		}
		return defValue;
	}

	/**
	 * 获取long值
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getLong(key, defValue);
	}

	/**
	 * 获取float值
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static float getFloat(Context context, String key, float defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);

		return sp.getFloat(key, defValue);
	}

	/**
	 * 获取布尔值
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
		try {
			if (sp == null)
				sp = context.getSharedPreferences(SP_NAME, 0);
			return sp.getBoolean(key, defValue);
		}catch (Exception e){
			ExceptionUtils.ExceptionSend(e,"偏好设置出错");
		}
		return defValue;
	}

	/**
	 * 保存List
	 *
	 * @param key
	 * @param datalist
	 */
	public static <T> void saveDataList(Context context, String key, List<T> datalist) {
		if (null == datalist || datalist.size() <= 0)
			return;
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);

		Gson gson = new Gson();
		//转换成json数据，再保存
		String strJson = gson.toJson(datalist);
		sp.edit().putString(key, strJson).apply();
	}

	/**
	 * 获取List
	 *
	 * @param key
	 * @return
	 */
	public static <T> List<T> getDataList(Context context, String key ) {
		List<T> datalist = new ArrayList<>();
		try {
			if (sp == null)
				sp = context.getSharedPreferences(SP_NAME, 0);
			String strJson = sp.getString(key, null);
			if (TextUtils.isEmpty(strJson)) {
				return datalist;
			}
			Gson gson = new Gson();
			datalist = gson.fromJson(strJson, new TypeToken<List<T>>(){}.getType());
		} catch (Exception e){
			ExceptionUtils.ExceptionSend(e,"偏好设置出错");
		}
		return datalist;
	}

}
