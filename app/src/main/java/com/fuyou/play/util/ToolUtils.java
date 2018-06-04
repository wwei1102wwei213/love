package com.fuyou.play.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/10/13 0013.
 */

public class ToolUtils {


    public static String getTimeStringForTills(String tills) {
        if (TextUtils.isEmpty(tills) || "0".equals(tills)) {
            return "1970-01-01";
        }
        tills = tills.concat("000");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String result = format.format(new Date(Long.parseLong(tills)));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1970-01-01";
    }

    public static String getTimeStringForTills(String tills, String reg) {
        if (TextUtils.isEmpty(tills) || "0".equals(tills)) {
            return "";
        }
        tills = tills.concat("000");
        SimpleDateFormat format = new SimpleDateFormat(reg);
        try {
            String result = format.format(new Date(Long.parseLong(tills)));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //首字母大写
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    //https://h37ohezyj1.execute-api.us-east-1.amazonaws.com/dev/moonphrase

    public static void main(String[] args){
        //这里的数后面加“D”是表明它是Double类型，否则相除的话取整，无法正常使用
        double percent = 34D / 150D;

        //输出一下，确认你的小数无误
        //System.out.println("小数：" + percent);
        //System.out.println("小数：" + (int)(percent*100));

        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();

        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);

        //最后格式化并输出
        //System.out.println("百分数：" + nt.format(percent));
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String printKeyHash(Activity context) {
        // Add code to print out the key hash
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
//                 String key = new String(Base64.encodeBytes(md.digest()));
                LogCustom.show("Key Hash="+key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

}
