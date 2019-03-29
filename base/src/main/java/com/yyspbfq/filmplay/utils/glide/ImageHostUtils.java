package com.yyspbfq.filmplay.utils.glide;

import android.text.TextUtils;

import com.yyspbfq.filmplay.biz.http.HttpFlag;

public class ImageHostUtils {

    public static String contact(String suffix) {
        if (!TextUtils.isEmpty(suffix)&&!suffix.startsWith(HttpFlag.BASE_IMAGE_URL)&&!suffix.startsWith("http")) {
            return HttpFlag.BASE_IMAGE_URL + suffix;
        }
        return suffix;
    }

}
