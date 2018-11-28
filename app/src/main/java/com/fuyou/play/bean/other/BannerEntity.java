package com.fuyou.play.bean.other;

public class BannerEntity {

    private int res;
    private String pic;
    private int type;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BannerEntity() {

    }

    public BannerEntity(int res, String pic, int type) {
        this.res = res;
        this.pic = pic;
        this.type = type;
    }
}
