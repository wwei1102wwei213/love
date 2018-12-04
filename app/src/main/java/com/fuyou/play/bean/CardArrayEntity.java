package com.fuyou.play.bean;

/**
 * Created by Administrator on 2018/5/23 0023.
 */

public class CardArrayEntity {

    /*{site: '位置1', 'back_url': '背景图', 'img_url': '正面图', 'card_name': '牌的名字', ‘describe’:'牌的描述'}*/
    private String site, back_url, img_url, card_name, describe;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getBack_url() {
        return back_url;
    }

    public void setBack_url(String back_url) {
        this.back_url = back_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
