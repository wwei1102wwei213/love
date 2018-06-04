package com.fuyou.play.util.sp;

import android.content.Context;
import android.text.TextUtils;

import com.fuyou.play.util.ExceptionUtils;

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

    private static String PhoneNum; //手机号
    private static String UserID;   //用户ID
    private static String Invite_code;   //邀请码
    private static String UserName; //用户昵称 ps:如果标示用户名请用新的变量
    private static String Avatar;   //用户头像
    private static String BornTime; //出生具体时间
    private static String Gender;   //性别
    private static String Lat;      //纬度
    private static String Lon;      //经度
    private static String DetailAddress;    //详细地址
    private static String Country;  //国家
    private static String City;     //城市
    private static String Credit;      //积分
    private static String AccessToken; //服务端Token
    private static String CreateTime; //创建时间
    private static String LastLoginTime; //最后登陆时间
    private static String Role;    //用户角色,为空表示普通用户，否则高级用户

    private static int CreateBirthInfoTime = -1; //是否创建弹出时间点
    private static int LoginSpaceTime = -1; //上次登陆时间点

    public static int SkinType = -1;

    public static int getSkinType(Context context){
        if (SkinType==-1) {
            SkinType = SharePrefUtil.getInt(context, "skin_type", 0);
        }
        return SkinType;
    }

    public static void saveSkinType(Context context, int value) {
        if (value<0||value==SkinType) return;
        SkinType = value;
        SharePrefUtil.saveInt(context, "skin_type", value);
    }

    public static String getPhoneNum(Context context) {
        if (TextUtils.isEmpty(PhoneNum)){
            PhoneNum = SharePrefUtil.getString(context, "ml_config_PhoneNum", "");
        }
        return PhoneNum;
    }

    public static void savePhoneNum(Context context, String phoneNum) {
        SharePrefUtil.saveString(context, "ml_config_PhoneNum", phoneNum);
        PhoneNum = phoneNum;
    }

    public static String getAccessToken(Context context) {
        if (TextUtils.isEmpty(AccessToken)){
            AccessToken = SharePrefUtil.getString(context, "ml_config_AccessToken", "");
        }
        return AccessToken;
    }

    public static void saveAccessToken(Context context, String accessToken) {
        SharePrefUtil.saveString(context, "ml_config_AccessToken", accessToken);
        AccessToken = accessToken;
    }

    public static String getCreateTime(Context context) {
        if (TextUtils.isEmpty(CreateTime)){
            CreateTime = SharePrefUtil.getString(context, "ml_config_CreateTime", "");
        }
        return CreateTime;
    }

    public static void saveCreateTime(Context context, String createTime) {
        if (!TextUtils.isEmpty(createTime)){
            SharePrefUtil.saveString(context, "ml_config_CreateTime", createTime);
        }
        CreateTime = createTime;
    }

    public static String getLastLoginTime(Context context) {
        if (TextUtils.isEmpty(LastLoginTime)){
            LastLoginTime = SharePrefUtil.getString(context, "ml_config_LastLoginTime", "");
        }
        return LastLoginTime;
    }

    public static void saveLastLoginTime(Context context, String lastLoginTime) {
        if (!TextUtils.isEmpty(lastLoginTime)){
            SharePrefUtil.saveString(context, "ml_config_LastLoginTime", lastLoginTime);
        }
        LastLoginTime = lastLoginTime;
    }

    public static String getUserID(Context context) {
        if (TextUtils.isEmpty(UserID)){
            UserID = SharePrefUtil.getString(context, "ml_config_userid", "");
        }
        return UserID;
    }

    public static void setUserID(Context context, String userID) {
        if (!TextUtils.isEmpty(userID)){
            SharePrefUtil.saveString(context, "ml_config_userid", userID);
        }
        UserID = userID;
    }
    public static void setInvite_code(Context context, String invite_code) {
        if (!TextUtils.isEmpty(invite_code)){
            SharePrefUtil.saveString(context, "ml_config_getInvite_code", invite_code);
        }
        Invite_code = invite_code;
    }

    public static String getInvite_code(Context context) {
        if (TextUtils.isEmpty(Invite_code)){
            Invite_code = SharePrefUtil.getString(context, "ml_config_getInvite_code", "");
        }
        return Invite_code;
    }

    public static String getLoginType(Context context){
        if (TextUtils.isEmpty(LoginType)){
            LoginType = SharePrefUtil.getString(context, "ml_config_login_type_s", "1");
        }
        return LoginType;
    }

    /**
     * loginType: 1为WX登陆、2为Mobile登陆
     */
    public static void setLoginType(Context context, String loginType){
        if (TextUtils.isEmpty(LoginType)){
            SharePrefUtil.saveString(context, "ml_config_login_type_s", loginType);
        }
        LoginType = loginType;
    }

    public static String getUserName(Context context) {
        if (TextUtils.isEmpty(UserName)){
            UserName = SharePrefUtil.getString(context, "ml_config_username", "");
        }
        return UserName;
    }

    public static void setUserName(Context context, String userName) {
        if (!TextUtils.isEmpty(userName)){
            SharePrefUtil.saveString(context, "ml_config_username", userName);
        }
        UserName = userName;
    }

    public static String getAvatar(Context context) {
        if (TextUtils.isEmpty(Avatar)){
            Avatar = SharePrefUtil.getString(context, "ml_config_avatar", "");
        }
        return Avatar;
    }

    public static void setAvatar(Context context, String avatar) {
        if (!TextUtils.isEmpty(avatar)){
            SharePrefUtil.saveString(context , "ml_config_avatar" , avatar);
        }
        Avatar = avatar;
    }

    public static String getBornTime(Context context) {
        if (TextUtils.isEmpty(BornTime)){
            BornTime = SharePrefUtil.getString(context, "ml_config_BornTime", "");
        }
        return BornTime;
    }

    public static void setBornTime(Context context , String bornTime) {
        if (!TextUtils.isEmpty(bornTime)){
            SharePrefUtil.saveString(context , "ml_config_BornTime" , bornTime);
        }
        BornTime = bornTime;
    }

    public static String getGender(Context context) {
        if (TextUtils.isEmpty(Gender)){
            Gender = SharePrefUtil.getString(context, "ml_config_Gender", "0");
        }
        return Gender;
    }

    public static void setGender(Context context, String gender) {
        if (!TextUtils.isEmpty(gender)){
            SharePrefUtil.saveString(context , "ml_config_Gender" , gender);
        }
        Gender = gender;
    }

    public static String getLat(Context context) {
        if (TextUtils.isEmpty(Lat)){
            Lat = SharePrefUtil.getString(context, "ml_config_Lat", "0");
        }
        return Lat;
    }

    public static void setLat(Context context, String lat) {
        if (!TextUtils.isEmpty(lat)){
            SharePrefUtil.saveString(context , "ml_config_Lat" , lat);
        }
        Lat = lat;
    }

    public static String getLon(Context context) {
        if (TextUtils.isEmpty(Lon)){
            Lon = SharePrefUtil.getString(context, "ml_config_Lon", "0");
        }
        return Lon;
    }

    public static void setLon(Context context, String lon) {
        if (!TextUtils.isEmpty(lon)){
            SharePrefUtil.saveString(context , "ml_config_Lon" , lon);
        }
        Lon = lon;
    }

    public static String getDetailAddress(Context context) {
        if (TextUtils.isEmpty(DetailAddress)){
            DetailAddress = SharePrefUtil.getString(context, "ml_config_DetailAddress", "0");
        }
        return DetailAddress;
    }

    public static void setDetailAddress(Context context, String detailAddress) {
        if (!TextUtils.isEmpty(detailAddress)){
            SharePrefUtil.saveString(context , "ml_config_DetailAddress" , detailAddress);
        }
        DetailAddress = detailAddress;
    }

    public static String getCountry(Context context) {
        if (TextUtils.isEmpty(Country)){
            Country = SharePrefUtil.getString(context, "ml_config_Country", "");
        }
        return Country;
    }

    public static void setCountry(Context context, String country) {
        if (!TextUtils.isEmpty(country)){
            SharePrefUtil.saveString(context , "ml_config_Country" , country);
        }
        Country = country;
    }

    public static String getCity(Context context) {
        if (TextUtils.isEmpty(City)){
            City = SharePrefUtil.getString(context, "ml_config_City", "");
        }
        return City;
    }

    public static void setCity(Context context, String city) {
        if (!TextUtils.isEmpty(city)){
            SharePrefUtil.saveString(context , "ml_config_City" , city);
        }
        City = city;
    }



    public static String getCredit(Context context) {
        if (TextUtils.isEmpty(Credit)){
            Credit = SharePrefUtil.getString(context, "ml_config_Credit_String", "0");
        }
        return Credit;
    }

    public static void setCredit(Context context, String credit) {
        SharePrefUtil.saveString(context , "ml_config_Credit_String" , credit);
        Credit = credit;
    }

    /**
     * 进入创建生日信息页面的最后时间点
     * @param context
     * @return
     */
    public static int getCreateBirthInfoTime(Context context) {
        if (CreateBirthInfoTime == -1){
            CreateBirthInfoTime = SharePrefUtil.getInt(context, "ml_config_CreateBirthInfoTime", 0);
        }
        return CreateBirthInfoTime;
    }

    public static void setCreateBirthInfoTime(Context context, int createBirthInfoTime) {
        SharePrefUtil.saveInt(context , "ml_config_CreateBirthInfoTime" , createBirthInfoTime);
        CreateBirthInfoTime = createBirthInfoTime;
    }

    public static int getLoginSpaceTime(Context context) {
        if (LoginSpaceTime == -1){
            LoginSpaceTime = SharePrefUtil.getInt(context, "ml_config_LoginSpaceTime", 0);
        }
        return LoginSpaceTime;
    }

    public static void setLoginSpaceTime(Context context, int loginSpaceTime) {
        SharePrefUtil.saveInt(context , "ml_config_LoginSpaceTime" , loginSpaceTime);
        LoginSpaceTime = loginSpaceTime;
    }

    public static String getRole(Context context) {
        if (TextUtils.isEmpty(Role)) {
            Role = SharePrefUtil.getString(context, "ml_config_Role_str", "");
        }
        return Role;
    }

    public static void setRole(Context context, List<String> role) {
        if(role != null && role.size() > 0) {
            SharePrefUtil.saveString(context, "ml_config_Role_str", role.toString());
        }
    }

    public static <T> void saveDataList(Context context, String key, List<T> list){
        SharePrefUtil.saveDataList(context, key, list);
    }

    public static <T> List<T> getDataList(Context context, String key) {
        List<T> datalist = new ArrayList<>();
        try {
            datalist = SharePrefUtil.getDataList(context, key);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"偏好设置出错");
        }
        return datalist;
    }

    /*public static void saveUserData(Context context, UserInfoBean bean){
        setUserID(context, bean.getId());
        setUserName(context, bean.getNick_name());
        setGender(context, bean.getGender()+"");
        setBornTime(context, bean.getBirthday());
        setCity(context, bean.getLocation());
        if (!TextUtils.isEmpty(bean.getAccess_token())) {
            saveAccessToken(context, bean.getAccess_token());
        }
        saveCreateTime(context, bean.getReg_time());
        saveLastLoginTime(context, bean.getLast_login_time());
        setAvatar(context, bean.getAvatar());
        if (!TextUtils.isEmpty(bean.getPhone_num())) {
            savePhoneNum(context, bean.getPhone_num());
        }

        setInvite_code(context, bean.getInvite_code());
    }*/

    public static void clearUserData(Context context){
        PhoneNum = null;
        UserID = null;
        UserName = null;
        Avatar = null;
        LoginType = null;
        BornTime = null;
        Gender = null;
        Lat = null;
        Lon = null;
        DetailAddress = null;
        Country = null;
        City = null;
        AccessToken = null;
        CreateTime = null;
        LastLoginTime = null;
        Credit = null;
        LastLoginTime = null;
        Role = null;

        SharePrefUtil.clear(context);

        LoginSpaceTime = -1;

        SkinType = -1;
    }

}
