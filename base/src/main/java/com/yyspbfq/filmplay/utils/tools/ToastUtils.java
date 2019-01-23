package com.yyspbfq.filmplay.utils.tools;

import android.text.TextUtils;
import android.widget.Toast;

import com.yyspbfq.filmplay.BaseApplication;

public class ToastUtils {

    public static void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

}
