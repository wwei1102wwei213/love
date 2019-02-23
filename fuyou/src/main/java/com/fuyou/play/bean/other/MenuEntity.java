package com.fuyou.play.bean.other;

public class MenuEntity {

    private int res;
    private String name;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuEntity(int res, String name) {
        this.res = res;
        this.name = name;
    }
}
