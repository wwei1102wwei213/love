package com.fuyou.play.util;

import android.content.Context;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.fuyou.play.R;

public class UiUtils {

    public static BitmapTransformation getGideTransformation(Context context) {
        return new GlideRoundTransform(context, context.getResources().getInteger(R.integer.base_radius_integer));
    }

}
