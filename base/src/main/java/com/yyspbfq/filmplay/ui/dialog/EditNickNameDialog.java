package com.yyspbfq.filmplay.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;


/**
 * 修改昵称浮层
 */

public class EditNickNameDialog extends Dialog {

    private Context mContext;
    private EditText editNickName;
    private ImageView imgClean;
    private TextView tvCancle;
    private TextView tvOk;
    private TextView tvTitle;

    public EditNickNameDialog(@NonNull Context context) {
        this(context, R.style.dialog_base_style);
    }

    public EditNickNameDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_edit_nickname, null);
        setContentView(view);
        tvTitle = (TextView)findViewById(R.id.dialog_tv_title);
        editNickName = (EditText)findViewById(R.id.dialog_edit_nickname);
        imgClean = (ImageView)findViewById(R.id.dialog_img_clean);
        tvCancle = (TextView)findViewById(R.id.dialog_tv_cancle);
        tvOk = (TextView)findViewById(R.id.dialog_tv_ok);

        tvCancle.setOnClickListener(v -> EditNickNameDialog.this.dismiss());

        imgClean.setOnClickListener(v -> editNickName.setText(""));
    }

    public EditNickNameDialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public EditNickNameDialog setNickName(String content) {
        editNickName.setText(content);
        return this;
    }

    public EditNickNameDialog setOnOkListener(View.OnClickListener listener) {
        if(listener != null) {
            tvOk.setOnClickListener(listener);
        }
        return this;
    }

    public EditNickNameDialog setOnCancleListener(View.OnClickListener listener) {
        if(listener != null) {
            tvCancle.setOnClickListener(listener);
        }
        return this;
    }

    public String getNickName() {
        return editNickName.getText().toString().trim();
    }

}
