package com.fuyou.play.util.HORUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class HORAspect implements Serializable {

    private HorosNormalEntity _planet1,_planet2;
    private HorosNormalEntity type;
    private double orb;

    public HorosNormalEntity get_planet1() {
        return _planet1;
    }

    public void set_planet1(HorosNormalEntity _planet1) {
        this._planet1 = _planet1;
    }

    public HorosNormalEntity get_planet2() {
        return _planet2;
    }

    public void set_planet2(HorosNormalEntity _planet2) {
        this._planet2 = _planet2;
    }

    public HorosNormalEntity getType() {
        return type;
    }

    public void setType(HorosNormalEntity type) {
        this.type = type;
    }

    public double getOrb() {
        return orb;
    }

    public void setOrb(double orb) {
        this.orb = orb;
    }



    @Override
    public String toString() {
        return "{" +
                "\"_planet1\":" + _planet1 +
                ", \"_planet2\":" + _planet2 +
                ", \"type\":" + type +
                ", \"orb\":" + orb +
                '}';
    }
}
