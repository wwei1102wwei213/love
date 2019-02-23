package com.fuyou.play.util.HORUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class HORHouseData implements Serializable {

    private int index;
    private double offsetAngle;
    private HorosNormalEntity sign;
    private List<HorosNormalEntity> planets;

    public double getOffsetAngle() {
        return offsetAngle;
    }

    public void setOffsetAngle(double offsetAngle) {
        this.offsetAngle = offsetAngle;
    }

    public HorosNormalEntity getSign() {
        return sign;
    }

    public void setSign(HorosNormalEntity sign) {
        this.sign = sign;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<HorosNormalEntity> getPlanets() {
        return planets;
    }

    public void setPlanets(List<HorosNormalEntity> planets) {
        this.planets = planets;
    }

   public double startAbsAngle()
    {
        return sign.getId() * 30 + offsetAngle;
    }

    @Override
    public String toString() {
        return "{" +
                "\"index\":" + index +
                ", \"offsetAngle\":" + offsetAngle +
                ", \"sign\":" + sign +
                ", \"planets\":" + planets +
                '}';
    }
}
