package com.fuyou.play.util.tarot;

/**
 * Created by Creacc on 2017/10/18.
 */

public class TarotCard {

    private String mName;

    private String mUprightContent;

    private String mReversedContent;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUprightContent() {
        return mUprightContent;
    }

    public void setUprightContent(String uprightContent) {
        mUprightContent = uprightContent;
    }

    public String getReversedContent() {
        return mReversedContent;
    }

    public void setReversedContent(String reversedContent) {
        mReversedContent = reversedContent;
    }
}
