package com.fuyou.play.util.HORUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class HORChartResult implements Serializable {

    private List<HORAspect> aspectDatas;
    private List<HORPlanetData> planetDatas;
    private List<HORHouseData> houseDatas;

    private HorosNormalEntity sunSign;
    private HorosNormalEntity moonSign;
    private HorosNormalEntity ascSign;

    public List<HORAspect> getAspectDatas() {
        return aspectDatas;
    }

    public void setAspectDatas(List<HORAspect> aspectDatas) {
        this.aspectDatas = aspectDatas;
    }

    public List<HORPlanetData> getPlanetDatas() {
        return planetDatas;
    }

    public void setPlanetDatas(List<HORPlanetData> planetDatas) {
        this.planetDatas = planetDatas;
    }

    public List<HORHouseData> getHouseDatas() {
        return houseDatas;
    }

    public void setHouseDatas(List<HORHouseData> houseDatas) {
        this.houseDatas = houseDatas;
    }

    public HorosNormalEntity getSunSign() {
        if (planetDatas!=null&&planetDatas.size()!=0){
            return planetDatas.get(0).getSign();
        }
        return sunSign;
    }

    public void setSunSign(HorosNormalEntity sunSign) {
        this.sunSign = sunSign;
    }

    public HorosNormalEntity getMoonSign() {
        if (planetDatas!=null&&planetDatas.size()>1){
            return planetDatas.get(1).getSign();
        }
        return moonSign;
    }

    public void setMoonSign(HorosNormalEntity moonSign) {
        this.moonSign = moonSign;
    }

    public HorosNormalEntity getAscSign() {
        if (houseDatas!=null&&houseDatas.size()>1){
            return houseDatas.get(0).getSign();
        }
        return ascSign;
    }

    public void setAscSign(HorosNormalEntity ascSign) {
        this.ascSign = ascSign;
    }

    @Override
    public String toString() {
        return "{" +
                "\"aspectDatas\":" + aspectDatas +
                ",\" planetDatas\":" + planetDatas +
                ",\" houseDatas\":" + houseDatas +
                ", \"sunSign\":" + getSunSign() +
                ", \"moonSign\":" + getMoonSign() +
                ", \"ascSign\":" + getAscSign() +
                '}';
    }
}
