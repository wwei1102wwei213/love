package com.fuyou.play.util.HORUtils;

import android.content.Context;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class HORDataManger {

    private HORDataManger() {
    }

    private static HORDataManger instance = null;



    /***
     * 单例模式，注意要加同步锁
     *
     * @return DBHelper
     */
    public static HORDataManger getInstance() {
        if (instance == null) {
            synchronized (HORDataManger.class) {
                if (instance == null) {
                    instance = new HORDataManger();
                }
            }
        }
        return instance;
    }

    //用户星盘数据
    private HORChartResult result;
    //About Me第一TAB标识
    private boolean HOR_MAIN_FLAG;
    //About Me第二TAB标识
    private boolean HOR_PERSONALTY_FLAG;

    public boolean getHOR_MAIN_FLAG() {
        return HOR_MAIN_FLAG;
    }

    public boolean isHOR_PERSONALTY_FLAG() {
        return HOR_PERSONALTY_FLAG;
    }

    public void setHOR_MAIN_FLAG(boolean flag) {
        this.HOR_MAIN_FLAG = flag;
    }

    public HORChartResult getUserHORData(Context context){
        if (result==null){
            result = HORFunction.getHORChartResult(context);
        }
        return result;
    }

    public void clearUserHORData(){
        result = null;
        HOR_MAIN_FLAG = true;
        HOR_PERSONALTY_FLAG = true;
    }

    public HorosNormalEntity getUserHORUP(Context context){
        return getUserHORData(context).getAscSign();
    }

    public int getUserSignID(Context context){
        return getUserHORData(context).getPlanetDatas().get(0).getSign().getId();
    }

}
