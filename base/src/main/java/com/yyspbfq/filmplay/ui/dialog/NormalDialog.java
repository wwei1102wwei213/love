package com.yyspbfq.filmplay.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.ui.activity.InfoInviteActivity;

public class NormalDialog extends Dialog {

    private Context mContext;

    private TextView tvCancle;
    private TextView tvOk;
    private TextView tvTitle;
    private RadioGroup radiogroupSex;


    public NormalDialog(@NonNull Context context) {
        this(context, R.style.dialog_base_style);
    }

    public NormalDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_base_normal, null);
        setContentView(view);
        tvTitle = (TextView) findViewById(R.id.dialog_tv_title);
        tvCancle = (TextView) findViewById(R.id.dialog_tv_cancle);
        tvOk = (TextView) findViewById(R.id.dialog_tv_ok);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoInviteActivity.actionStart(mContext);
                dismiss();
            }
        });
    }

    public NormalDialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }
}