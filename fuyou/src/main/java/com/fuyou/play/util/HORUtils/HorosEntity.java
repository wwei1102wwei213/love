package com.fuyou.play.util.HORUtils;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class HorosEntity {

    private int key;
    private String sname;
    private String name;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HorosEntity{" +
                "key=" + key +
                ", sname='" + sname + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
