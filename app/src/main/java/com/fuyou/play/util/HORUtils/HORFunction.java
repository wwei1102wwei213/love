package com.fuyou.play.util.HORUtils;

import android.content.Context;

import com.fuyou.play.bean.UserInfo;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.util.sp.UserDataUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class HORFunction {

    public static final int ANGLES_DATA = 1;
    public static final int PLANET_DATA = 2;
    public static final int ASPECT_TYPE_DATA = 3;
    public static final int HOROS_DATA = 4;
    public static final int RULERS_DATA = 5;
    public static final String[] AspectType = {
            "con",
            "opp",
            "squ",
            "tri",
            "sex"
    };

    public static final String[] Angles = {
            "Asc",
            "IC",
            "Des",
            "MC"
    };

    public static final String[] Planets = {
            "Sun",
            "Moon",
            "Mercury",
            "Venus",
            "Mars",
            "Jupiter",
            "Saturn",
            "Uranus",
            "Neptune",
            "Pluto"
    };

    public static final String[] Horos = {
            "Ari",
            "Tau",
            "Gem",
            "Can",
            "Leo",
            "Vir",
            "Lib",
            "Sco",
            "Sag",
            "Cap",
            "Aqu",
            "Pis"
    };

    public static final String[] Rulers = {
            "Mars",
            "Venus",
            "Mercury",
            "Moon",
            "Sun",
            "Mercury",
            "Venus",
            "Pluto",
            "Jupiter",
            "Saturn",
            "Uranus",
            "Neptune"
    };

    public static final int[] RulerInts = new int[]{
            4,3,2,1,0,2,3,9,5,6,7,8
    };

    public static final String[] HoroscopeNames = {
            "Aries",
            "Taurus",
            "Gemini",
            "Cancer",
            "Leo",
            "Virgo",
            "Libra",
            "Scorpio",
            "Sagittarius",
            "Capricorn",
            "Aquarius",
            "Pisces"
    };

    public static final List<String> HoroscoperNameList = Arrays.asList(
            "Aries",
            "Taurus",
            "Gemini",
            "Cancer",
            "Leo",
            "Virgo",
            "Libra",
            "Scorpio",
            "Sagittarius",
            "Capricorn",
            "Aquarius",
            "Pisces"
    );


    public static final String[] dates = {
            "3.21 - 4.19",
            "4.20 - 5.20",
            "5.21 - 6.21",
            "6.22 - 7.22",
            "7.23 - 8.22",
            "8.23 - 9.22",
            "9.23 - 10.23",
            "10.24 - 11.22",
            "11.23 - 12.21",
            "12.22 - 1.19",
            "1.20 - 2.18",
            "2.19 - 3.20"
    };


    public static String getLatlonFromat(String lat, String lon){
        String result = "";
        try {
            double la = Double.valueOf(lat);
            double lo = Double.valueOf(lon);
            String we = lo>0?"E":"W";
            String ns = la>0?"N":"S";
            result = we + readableAngle(Math.abs(lo)) + "" + ns + readableAngle(Math.abs(la));
        }catch (Exception e){

        }
        return result;
    }


    public static String readableAngle(double angle){
        int a = (int)angle;
        double b = angle - a;
        double c = 60 * b;
        int d = (int)c;
        return a+"°"+d+"′";
    }

    /**
     * 根据星座ID获取星座名字或者日期范围
     * @param id
     * @param type 1:缩写 2:全名 3：日期范围
     * @return
     */
    public static String getNameForHORSign(int id, int type){
        if (id<0||id>11) return "";
        if (type==1){
            return Horos[id];
        } else if (type==2){
            return HoroscopeNames[id];
        } else if (type==3){
            return dates[id];
        }
        return "";
    }

    public static int getUserSign(Context context){

        return 0;
    }


    public static List<HorosNormalEntity> getResData(int tag){
        List<HorosNormalEntity> list = new ArrayList<>();
        switch (tag){
            case ANGLES_DATA:
                for (int i=0;i<Angles.length;i++){
                    HorosNormalEntity entity = new HorosNormalEntity();
                    entity.setId(i);
                    entity.setName(Angles[i]);
                    list.add(entity);
                }
                break;
            case ASPECT_TYPE_DATA:
                for (int i=0;i<AspectType.length;i++){
                    HorosNormalEntity entity = new HorosNormalEntity();
                    entity.setId(i);
                    entity.setName(AspectType[i]);
                    list.add(entity);
                }
                break;
            case PLANET_DATA:
                for (int i=0;i<Planets.length;i++){
                    HorosNormalEntity entity = new HorosNormalEntity();
                    entity.setId(i);
                    entity.setName(Planets[i]);
                    list.add(entity);
                }
                break;
            case HOROS_DATA:
                for (int i=0;i<Horos.length;i++){
                    HorosNormalEntity entity = new HorosNormalEntity();
                    entity.setId(i);
                    entity.setName(Horos[i]);
                    list.add(entity);
                }
                break;
            case RULERS_DATA:
                for (int i=0;i<Rulers.length;i++){
                    HorosNormalEntity entity = new HorosNormalEntity();
                    entity.setId(RulerInts[i]);
                    entity.setName(Rulers[i]);
                    list.add(entity);
                }
                break;
        }
        return list;
    }

    public static int getFlyHouseIdx(List<HORHouseData> hd, List<HORPlanetData> pd){
        int result = 0;
        List<HorosNormalEntity> ruler = getResData(5);
        HORPlanetData planet = pd.get(ruler.get(hd.get(0).getSign().getId()).getId());
        for (int i=0;i<hd.size();i++){

        }

        return 0;
    }

    /**
     * 获取用户星盘数据
     * @param context
     * @return 星盘数据
     */
    public static HORChartResult getHORChartResult(Context context){
        HORChartResult result = null;
        BornInfo info = new BornInfo();
        String bornTime = UserDataUtil.getBornTime(context);
        if (bornTime.contains(".")) bornTime = bornTime.substring(0,bornTime.indexOf("."));

        long time = Long.parseLong(bornTime);
        int gtm = 0;
        gtm = 0 - gtm/3600;
        String date = getTimeStringForAstrolabe(time);
        String[] args = date.split("-");
        info.setTimezone(gtm);
        info.setMinute(Integer.valueOf(args[4]));
        info.setHour(Integer.valueOf(args[3]));
        info.setDay(Integer.valueOf(args[2]));
        info.setMonth(Integer.valueOf(args[1]));
        info.setYear(Integer.valueOf(args[0]));
        info.setDaylight(0);
        String latStr = "39.55";
        String lotStr = "116.25";
        info.setLat(Double.valueOf(latStr));
        info.setLon(Double.valueOf(lotStr));
        LogCustom.show("用户数据星盘数据："+info.toString());
        result = new HORUtils(info).calculateWithBornInfo();
        LogCustom.show(result.getHouseDatas().toString());
        String data = result.toString();
        String l1 = data.substring(0,data.length()/2);
        LogCustom.show(l1);
        LogCustom.show(data.substring(l1.length()-1,data.length()));
        return result;
    }


    /**
     * 获取SUB用户星盘数据
     * @param subprofileDO
     * @return 星盘数据
     */
    public static HORChartResult getHORChartResult(UserInfo subprofileDO){
        HORChartResult result = null;
        BornInfo info = new BornInfo();
        long time = Long.parseLong(subprofileDO.getBirthday());
        int gtm = Integer.valueOf(subprofileDO.getBirthday());
        gtm = 0-gtm;
        String date = getTimeStringForAstrolabe(time);
        String[] args = date.split("-");
        info.setTimezone(gtm);
        info.setMinute(Integer.valueOf(args[4]));
        info.setHour(Integer.valueOf(args[3]));
        info.setDay(Integer.valueOf(args[2]));
        info.setMonth(Integer.valueOf(args[1]));
        info.setYear(Integer.valueOf(args[0]));
        info.setDaylight(0);
        String latStr = "39.55";
        String lotStr = "116.25";
        info.setLat(Double.valueOf(latStr));
        info.setLon(Double.valueOf(lotStr));
        result = new HORUtils(info).calculateWithBornInfo();
        LogCustom.show("星盘数据："+info.toString());
        String data = result.toString();
        String l1 = data.substring(0,data.length()/2);
        LogCustom.show(l1);
        LogCustom.show(data.substring(l1.length()-1,data.length()));
        return result;
    }

    /**
     * 获取现在时间星盘数据
     * @param context
     * @return 星盘数据
     */
    public static HORChartResult getHORChartResultNow(Context context){
        HORChartResult result = null;
        BornInfo info = new BornInfo();
        int gtm = TimeZone.getDefault().getRawOffset();
        gtm = 0-gtm/3600000;
        String date = getTimeStringForAstrolabe(System.currentTimeMillis()/1000);
        String[] args = date.split("-");
        info.setTimezone(gtm);
        info.setMinute(Integer.valueOf(args[4]));
        info.setHour(Integer.valueOf(args[3]));
        info.setDay(Integer.valueOf(args[2]));
        info.setMonth(Integer.valueOf(args[1]));
        info.setYear(Integer.valueOf(args[0]));
        String latStr = "39.55";
        String lotStr = "116.25";
        info.setLat(Double.valueOf(latStr));
        info.setLon(Double.valueOf(lotStr));
        LogCustom.show("当前时间星盘数据："+info.toString());
        result = new HORUtils(info).calculateWithBornInfo();
        return result;
    }

    public static String getTimeStringForAstrolabe(long tills) {
        tills = tills * 1000;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        try {
            String result = format.format(new Date(tills));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1970-01-01-00-00";
    }

    /**
     * 获取现在时间星盘数据
     * @param context
     * @return 星盘数据
     */
    public static HORChartResult getHORChartResultNow(Context context, int flag){
        HORChartResult result = null;
        BornInfo info = new BornInfo();
        int gtm = TimeZone.getDefault().getRawOffset();
        gtm = 0-gtm/3600000;
        String date = getTimeStringForAstrolabe(System.currentTimeMillis()/1000 + flag*24*3600);
        String[] args = date.split("-");
        info.setTimezone(gtm);
        info.setMinute(Integer.valueOf(args[4]));
        info.setHour(Integer.valueOf(args[3]));
        info.setDay(Integer.valueOf(args[2]));
        info.setMonth(Integer.valueOf(args[1]));
        info.setYear(Integer.valueOf(args[0]));
        String latStr = "39.55";
        String lotStr = "116.25";
        info.setLat(Double.valueOf(latStr));
        info.setLon(Double.valueOf(lotStr));
//        LogCustom.show("当前时间星盘数据："+info.toString());
        result = new HORUtils(info).calculateWithBornInfo();

        return result;
    }

    /**
     * 将用户数据转身份数据
     * @param context
     * @return
     */
    public static UserInfo copySubForUser(Context context){
        UserInfo user = new UserInfo();
        user.setId(Integer.parseInt(UserDataUtil.getUserID(context)));
        user.setNickName(UserDataUtil.getUserName(context));
        user.setBirthday(UserDataUtil.getBornTime(context));
        user.setLocation(UserDataUtil.getCity(context));
        return user;
    }

}
