package com.fuyou.play.util.HORUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class HORPlanetData implements Serializable {

    private HorosNormalEntity sign;
    private double offsetAngle;
    private HorosNormalEntity planet;
    private int planetIdx;
    private int house;
    private int synastryHouse;

    public int getSynastryHouse() {
        return synastryHouse;
    }

    public void setSynastryHouse(int synastryHouse) {
        this.synastryHouse = synastryHouse;
    }

    public HorosNormalEntity getSign() {
        return sign;
    }

    public void setSign(HorosNormalEntity sign) {
        this.sign = sign;
    }

    public double getOffsetAngle() {
        return offsetAngle;
    }

    public void setOffsetAngle(double offsetAngle) {
        this.offsetAngle = offsetAngle;
    }

    public HorosNormalEntity getPlanet() {
        return planet;
    }

    public void setPlanet(HorosNormalEntity planet) {
        this.planet = planet;
    }

    public double absAngle()
    {
        return 30 * sign.getId() + offsetAngle;
    }

    public int getPlanetIdx() {
        return planetIdx;
    }

    public void setPlanetIdx(int planetIdx) {
        this.planetIdx = planetIdx;
    }

    public int getHouse() {
        return house;
    }

    public void setHouse(int house) {
        this.house = house;
    }

    @Override
    public String toString() {
        return "{" +
                "\"sign\":" + sign +
                ", \"offsetAngle\":" + offsetAngle +
                ", \"planet\":" + planet +
                ", \"planetIdx\":" + planetIdx +
                ", \"house\":" + house +
                ", \"synastryHouse\":" + synastryHouse +
                '}';
    }
}
