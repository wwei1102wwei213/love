package com.slove.play.util.sp;

import android.content.Context;
import android.text.TextUtils;

import com.slove.play.util.ExceptionUtils;

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

    private static String UserID;   //用户ID
    private static String UserName; //用户昵称 ps:如果标示用户名请用新的变量
    private static String Avatar;   //用户头像
    private static String BornTime; //出生具体时间
    private static String DayLight; //daylight
    private static String Gender;   //性别
    private static String Lat;      //纬度
    private static String Lon;      //经度
    private static String DetailAddress;    //详细地址
    private static String Country;  //国家
    private static String City;     //城市
    private static String State;    //省份
    private static String Email;    //Email
    private static String TimeZoneOffset;   //时区偏移量
    private static String Credit;      //积分
    private static String AccessToken; //服务端Token
    private static String RyToken; //融云Token
    private static String Password; //用户密码
    private static String CreateTime; //创建时间
    private static String LastLoginTime; //最后登陆时间
    private static String IsHaveCreateBirthInfo; //是否创建过生日信息
    private static String ThreeLoginToken; //第三方登陆id
    private static String Role;    //用户角色,为空表示普通用户，否则高级用户
    private static int display_my_zodiac_sign = -1;     // 隐私设置，是否显示我的星座信息
    private static int sign_age_gender = -1;            // 隐私设置，是否可以通过这些信息搜索我

    private static int SubscribeType = -1; //积分订阅类型
    private static int SubscribeExpireTime = -1; //积分订阅到期时间
    private static int MoneyCreditType = -1; //订阅类型
    private static int CreateBirthInfoTime = -1; //是否创建弹出时间点
    private static int LoginSpaceTime = -1; //上次登陆时间点

    private static double LocationLat = -300;
    private static double LocationLon = -300;

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

    public static String getIsHaveCreateBirthInfo(Context context) {
        if (TextUtils.isEmpty(IsHaveCreateBirthInfo)){
            IsHaveCreateBirthInfo = SharePrefUtil.getString(context, "config_IsHaveCreateBirthInfo", "0");
        }
        return IsHaveCreateBirthInfo;
    }

    public static void setIsHaveCreateBirthInfo(Context context, String isHaveCreateBirthInfo) {
        if (!TextUtils.isEmpty(isHaveCreateBirthInfo)){
            SharePrefUtil.saveString(context, "config_IsHaveCreateBirthInfo", isHaveCreateBirthInfo);
        }
        IsHaveCreateBirthInfo = isHaveCreateBirthInfo;
    }

    public static String getAccessToken(Context context) {
        if (TextUtils.isEmpty(AccessToken)){
            AccessToken = SharePrefUtil.getString(context, "config_AccessToken", "");
        }
        return AccessToken;
    }

    public static void saveAccessToken(Context context, String accessToken) {
        SharePrefUtil.saveString(context, "config_AccessToken", accessToken);
        AccessToken = accessToken;
    }

    public static String getRyToken(Context context) {
        if (TextUtils.isEmpty(RyToken)){
            RyToken = SharePrefUtil.getString(context, "config_RyToken", "");
        }
        return RyToken;
    }

    public static void saveRyToken(Context context, String ryToken) {
        if (!TextUtils.isEmpty(ryToken)){
            SharePrefUtil.saveString(context, "config_RyToken", ryToken);
        }
        RyToken = ryToken;
    }

    public static String getPassword(Context context) {
        if (TextUtils.isEmpty(Password)){
            Password = SharePrefUtil.getString(context, "config_Password", "");
        }
        return Password;
    }

    public static void setPassword(Context context, String password) {
        if (!TextUtils.isEmpty(password)){
            SharePrefUtil.saveString(context, "config_Password", password);
        }
        Password = password;
    }

    public static String getCreateTime(Context context) {
        if (TextUtils.isEmpty(CreateTime)){
            CreateTime = SharePrefUtil.getString(context, "config_CreateTime", "");
        }
        return CreateTime;
    }

    public static void saveCreateTime(Context context, String createTime) {
        if (!TextUtils.isEmpty(createTime)){
            SharePrefUtil.saveString(context, "config_CreateTime", createTime);
        }
        CreateTime = createTime;
    }

    public static String getLastLoginTime(Context context) {
        if (TextUtils.isEmpty(LastLoginTime)){
            LastLoginTime = SharePrefUtil.getString(context, "config_LastLoginTime", "");
        }
        return LastLoginTime;
    }

    public static void saveLastLoginTime(Context context, String lastLoginTime) {
        if (!TextUtils.isEmpty(lastLoginTime)){
            SharePrefUtil.saveString(context, "config_LastLoginTime", lastLoginTime);
        }
        LastLoginTime = lastLoginTime;
    }

    public static String getUserID(Context context) {
        if (TextUtils.isEmpty(UserID)){
            UserID = SharePrefUtil.getString(context, "config_userid", "");
        }
        return UserID;
    }

    public static void setUserID(Context context, String userID) {
        if (!TextUtils.isEmpty(userID)){
            SharePrefUtil.saveString(context, "config_userid", userID);
        }
        UserID = userID;
    }

    public static String getLoginType(Context context){
        if (TextUtils.isEmpty(LoginType)){
            LoginType = SharePrefUtil.getString(context, "config_login_type_s", "1");
        }
        return LoginType;
    }

    /**
     * loginType: 1为账号登陆、2为google登陆、3为facebook登陆、4为游客登陆
     */
    public static void setLoginType(Context context, String loginType){
        if (TextUtils.isEmpty(LoginType)){
            SharePrefUtil.saveString(context, "config_login_type_s", loginType);
        }
        LoginType = loginType;
    }

    public static String getUserName(Context context) {
        if (TextUtils.isEmpty(UserName)){
            UserName = SharePrefUtil.getString(context, "config_username", "");
        }
        return UserName;
    }

    public static void setUserName(Context context, String userName) {
        if (!TextUtils.isEmpty(userName)){
            SharePrefUtil.saveString(context, "config_username", userName);
        }
        UserName = userName;
    }

    public static String getAvatar(Context context) {
        if (TextUtils.isEmpty(Avatar)){
            Avatar = SharePrefUtil.getString(context, "config_acatar", "");
        }
        return Avatar;
    }

    public static void setAvatar(Context context, String avatar) {
        if (!TextUtils.isEmpty(avatar)){
            SharePrefUtil.saveString(context , "config_acatar" , avatar);
        }
        Avatar = avatar;
    }

    public static String getBornTime(Context context) {
        if (TextUtils.isEmpty(BornTime)){
            BornTime = SharePrefUtil.getString(context, "config_BornTime", "");
        }
        return BornTime;
    }

    public static void setBornTime(Context context , String bornTime) {
        if (!TextUtils.isEmpty(bornTime)){
            SharePrefUtil.saveString(context , "config_BornTime" , bornTime);
        }
        BornTime = bornTime;
    }

    public static String getDayLight(Context context) {
        if (TextUtils.isEmpty(DayLight)){
            DayLight = SharePrefUtil.getString(context, "config_DayLight", "0");
        }
        return DayLight;
    }

    public static void setDayLight(Context context , String dayLight) {
        if (!TextUtils.isEmpty(dayLight)){
            SharePrefUtil.saveString(context , "config_DayLight" , dayLight);
        }
        DayLight = dayLight;
    }

    public static String getGender(Context context) {
        if (TextUtils.isEmpty(Gender)){
            Gender = SharePrefUtil.getString(context, "config_Gender", "0");
        }
        return Gender;
    }

    public static void setGender(Context context, String gender) {
        if (!TextUtils.isEmpty(gender)){
            SharePrefUtil.saveString(context , "config_Gender" , gender);
        }
        Gender = gender;
    }

    public static String getLat(Context context) {
        if (TextUtils.isEmpty(Lat)){
            Lat = SharePrefUtil.getString(context, "config_Lat", "0");
        }
        return Lat;
    }

    public static void setLat(Context context, String lat) {
        if (!TextUtils.isEmpty(lat)){
            SharePrefUtil.saveString(context , "config_Lat" , lat);
        }
        Lat = lat;
    }

    public static String getLon(Context context) {
        if (TextUtils.isEmpty(Lon)){
            Lon = SharePrefUtil.getString(context, "config_Lon", "0");
        }
        return Lon;
    }

    public static void setLon(Context context, String lon) {
        if (!TextUtils.isEmpty(lon)){
            SharePrefUtil.saveString(context , "config_Lon" , lon);
        }
        Lon = lon;
    }

    public static double getLocationLat(Context context) {
        if (LocationLat == -300){
            String temp = SharePrefUtil.getString(context, "config_LocationLat", "0");
            LocationLat = Double.valueOf(temp);
        }
        return LocationLat;
    }

    public static void setLocationLat(Context context, double locationLat) {
        SharePrefUtil.saveString(context , "config_LocationLat" , locationLat+"");
        LocationLat = locationLat;
    }

    public static double getLocationLon(Context context) {
        if (LocationLon == -300){
            String temp = SharePrefUtil.getString(context, "config_LocationLon", "0");
            LocationLon = Double.valueOf(temp);
        }
        return LocationLon;
    }

    public static void setLocationLon(Context context, double locationLon) {
        SharePrefUtil.saveString(context , "config_LocationLon" , locationLon+"");
        LocationLon = locationLon;
    }

    public static String getDetailAddress(Context context) {
        if (TextUtils.isEmpty(DetailAddress)){
            DetailAddress = SharePrefUtil.getString(context, "config_DetailAddress", "0");
        }
        return DetailAddress;
    }

    public static void setDetailAddress(Context context, String detailAddress) {
        if (!TextUtils.isEmpty(detailAddress)){
            SharePrefUtil.saveString(context , "config_DetailAddress" , detailAddress);
        }
        DetailAddress = detailAddress;
    }

    public static String getCountry(Context context) {
        if (TextUtils.isEmpty(Country)){
            Country = SharePrefUtil.getString(context, "config_Country", "");
        }
        return Country;
    }

    public static void setCountry(Context context, String country) {
        if (!TextUtils.isEmpty(country)){
            SharePrefUtil.saveString(context , "config_Country" , country);
        }
        Country = country;
    }

    public static String getCity(Context context) {
        if (TextUtils.isEmpty(City)){
            City = SharePrefUtil.getString(context, "config_City", "");
        }
        return City;
    }

    public static void setCity(Context context, String city) {
        if (!TextUtils.isEmpty(city)){
            SharePrefUtil.saveString(context , "config_City" , city);
        }
        City = city;
    }

    public static String getState(Context context) {
        if (TextUtils.isEmpty(State)){
            State = SharePrefUtil.getString(context, "config_State", "");
        }
        return State;
    }

    public static void setState(Context context, String state) {
        if (!TextUtils.isEmpty(state)){
            SharePrefUtil.saveString(context , "config_State" , state);
        }
        State = state;
    }

    public static String getEmail(Context context) {
        if (TextUtils.isEmpty(Email)){
            Email = SharePrefUtil.getString(context, "config_Email", "");
        }
        return Email;
    }

    public static void setEmail(Context context, String email) {
        if (!TextUtils.isEmpty(email)){
            SharePrefUtil.saveString(context , "config_Email" , email);
        }
        Email = email;
    }

    public static String getTimeZoneOffset(Context context) {
        if (TextUtils.isEmpty(TimeZoneOffset)){
            TimeZoneOffset = SharePrefUtil.getString(context, "config_TimeZoneOffset", "0");
        }
        return TimeZoneOffset;
    }

    public static void setTimeZoneOffset(Context context, String timeZoneOffset) {
        if (!TextUtils.isEmpty(timeZoneOffset)){
            SharePrefUtil.saveString(context , "config_TimeZoneOffset" , timeZoneOffset);
        }
        TimeZoneOffset = timeZoneOffset;
    }

    public static String getCredit(Context context) {
        if (TextUtils.isEmpty(Credit)){
            Credit = SharePrefUtil.getString(context, "config_Credit_String", "0");
        }
        return Credit;
    }

    public static void setCredit(Context context, String credit) {
        SharePrefUtil.saveString(context , "config_Credit_String" , credit);
        Credit = credit;
    }

    /**
     * 第三方登陆ID
     * @return
     */
    public static String getThreeLoginToken(Context context) {
        if (TextUtils.isEmpty(ThreeLoginToken)){
            ThreeLoginToken = SharePrefUtil.getString(context, "config_ThreeLoginToken", "");
        }
        return ThreeLoginToken;
    }

    public static void setThreeLoginToken(Context context, String threeLoginToken) {
        if (!TextUtils.isEmpty(threeLoginToken)){
            SharePrefUtil.saveString(context, "config_ThreeLoginToken", threeLoginToken);
        }
        ThreeLoginToken = threeLoginToken;
    }

    private static int getMoneyCheckForInt(int money, int[] types){
        int moneyType = 0;
        for (int i=0;i<types.length;i++){
            if (types[i]==1){
                moneyType = i+1;
            }
        }
        if (moneyType==1||moneyType==6){
            return 1;
        }
        if (moneyType>0){
            return 2;
        }
        return 0;
    }

    /**
     * 进入创建生日信息页面的最后时间点
     * @param context
     * @return
     */
    public static int getCreateBirthInfoTime(Context context) {
        if (CreateBirthInfoTime == -1){
            CreateBirthInfoTime = SharePrefUtil.getInt(context, "config_CreateBirthInfoTime", 0);
        }
        return CreateBirthInfoTime;
    }

    public static void setCreateBirthInfoTime(Context context, int createBirthInfoTime) {
        SharePrefUtil.saveInt(context , "config_CreateBirthInfoTime" , createBirthInfoTime);
        CreateBirthInfoTime = createBirthInfoTime;
    }

    public static int getLoginSpaceTime(Context context) {
        if (LoginSpaceTime == -1){
            LoginSpaceTime = SharePrefUtil.getInt(context, "config_LoginSpaceTime", 0);
        }
        return LoginSpaceTime;
    }

    public static void setLoginSpaceTime(Context context, int loginSpaceTime) {
        SharePrefUtil.saveInt(context , "config_LoginSpaceTime" , loginSpaceTime);
        LoginSpaceTime = loginSpaceTime;
    }

    public static String getRole(Context context) {
        if (TextUtils.isEmpty(Role)) {
            Role = SharePrefUtil.getString(context, "config_Role_str", "");
        }
        return Role;
    }

    public static void setRole(Context context, List<String> role) {
        if(role != null && role.size() > 0) {
            SharePrefUtil.saveString(context, "config_Role_str", role.toString());
        }
    }

    public static int getDisplayMyZodiacSign(Context context) {
        if (display_my_zodiac_sign == -1) {
            display_my_zodiac_sign = SharePrefUtil.getInt(context, "config_display_my_zodiac_sign", 1);
        }
        return display_my_zodiac_sign;
    }

    public static void setDisplayMyZodiacSign(Context context, int displayStatus) {

        SharePrefUtil.saveInt(context , "config_display_my_zodiac_sign" , displayStatus);
        display_my_zodiac_sign = displayStatus;
    }

    public static int getSignAgeGender(Context context) {
        if (sign_age_gender == -1){
            sign_age_gender = SharePrefUtil.getInt(context, "config_sign_age_gender", 1);
        }
        return sign_age_gender;
    }

    public static void setSignAgeGender(Context context, int signAgeGenderStatus) {
        SharePrefUtil.saveInt(context , "config_sign_age_gender" , signAgeGenderStatus);
        sign_age_gender = signAgeGenderStatus;
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

    public static void clearUserData(Context context){
        UserID = null;
        UserName = null;
        Avatar = null;
        LoginType = null;
        BornTime = null;
        DayLight = null;
        Gender = null;
        Lat = null;
        Lon = null;
        DetailAddress = null;
        Country = null;
        City = null;
        State = null;
        Email = null;
        TimeZoneOffset = null;
        Password = null;
        AccessToken = null;
        RyToken = null;
        CreateTime = null;
        LastLoginTime = null;
        IsHaveCreateBirthInfo = null;
        Credit = null;
        LastLoginTime = null;
        ThreeLoginToken = null;
        Role = null;

        SharePrefUtil.clear(context);

        SubscribeType = -1;
        SubscribeExpireTime = -1;
        MoneyCreditType = -1;
        LoginSpaceTime = -1;
        display_my_zodiac_sign = -1;
        sign_age_gender = -1;
        SkinType = -1;
    }

}
