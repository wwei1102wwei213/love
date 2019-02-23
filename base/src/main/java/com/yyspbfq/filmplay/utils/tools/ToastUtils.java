package com.yyspbfq.filmplay.utils.tools;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.utils.BLog;

public class ToastUtils {

    private static CustomToast instance;

    public static void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        showToastDefault(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT);
    }

    public static void showToastCenter(Context context, CharSequence text, int duration) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.custom_dialog_loading, null);
            View pb = v.findViewById(R.id.pb_dialog);
            pb.setVisibility(View.GONE);
            TextView tv = (TextView) v.findViewById(R.id.custom_dialog_loading_tv_msg);
            tv.setText(!TextUtils.isEmpty(text)?text : "");
            Toast toast = new Toast(context.getApplicationContext());
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(duration);
            toast.setView(v);
            toast.show();
        } catch (Exception e){
            BLog.e(e);
        }
    }

    public static void showToastDefault(Context context, CharSequence text, int duration) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            TextView tv = (TextView)inflater.inflate(R.layout.custom_toast_layout, null);
            tv.setText(text);
            Toast toast = new Toast(context.getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(tv);
            toast.show();
        } catch (Exception e){
            BLog.e(e);
        }
    }



}
