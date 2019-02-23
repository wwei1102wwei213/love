package com.fuyou.play.util.HORUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class HorosNormalEntity implements Serializable {

    private int id;
    private String name;

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

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"name\":" + '\"'+name + '\"' +
                '}';
    }
}
