package com.yyspbfq.filmplay.utils.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.utils.BLog;

public class CustomToast extends Toast{

    private TextView tv;

    public CustomToast(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            tv = (TextView)inflater.inflate(R.layout.custom_toast_layout, null);
            setDuration(LENGTH_SHORT);
            setView(tv);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    public void setText(String str) {
        try {
            tv.setText(str);
        } catch (Exception e){
            BLog.e(e);
        }
    }
}
