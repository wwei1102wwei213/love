package com.yyspbfq.filmplay.ui.dialog;

/**
 * 性别
 * Created by tankai on 2017/11/10.
 */

public class Sex {

    private int id;
    private String name;
    private int res;

    public Sex(){}

    public Sex(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Sex(int id, String name, int res) {
        this.id = id;
        this.name = name;
        this.res = res;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
