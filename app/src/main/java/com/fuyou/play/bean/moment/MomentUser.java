package com.fuyou.play.bean.moment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-08-17.
 */

public class MomentUser extends MomentSender implements Serializable{

    @SerializedName("profile-image")
    private String profile_image;

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
