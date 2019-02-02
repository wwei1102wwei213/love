package com.yyspbfq.filmplay.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;


/**
 * 修改性别浮层
 */

public class SexDialog extends Dialog {

    private Context mContext;

    private TextView tvCancle;
    private TextView tvOk;
    private TextView tvTitle;
    private RadioGroup radiogroupSex;

    private int curSexId = 0;
//    private Sex curSex;

    public SexDialog(@NonNull Context context) {
        this(context, R.style.dialog_base_style);
    }

    public SexDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_edit_sex, null);
        setContentView(view);
        tvTitle = (TextView) findViewById(R.id.dialog_tv_title);
        radiogroupSex = (RadioGroup) findViewById(R.id.dialog_radiogroup_sex);
        tvCancle = (TextView) findViewById(R.id.dialog_tv_cancle);
        tvOk = (TextView) findViewById(R.id.dialog_tv_ok);

        tvCancle.setOnClickListener(v -> SexDialog.this.dismiss());

        radiogroupSex.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.dialog_rbtn_sex_boy:
//                    curSex = getSex(0);
                    curSexId = 1;
                    break;
                case R.id.dialog_rbtn_sex_girl:
//                    curSex = getSex(1);
                    curSexId = 2;
                    break;
            }
        });
    }

    public SexDialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    /**
     * 传入用户的性别id
     *
     * @param sexId
     * @return
     */
    public SexDialog setChooseSex(String sexId) {

        if ("1".equals(sexId)) {
            curSexId = 1;
            ((RadioButton) radiogroupSex.getChildAt(0)).setChecked(true);
        } else if ("2".equals(sexId)) {
            curSexId = 2;
            ((RadioButton) radiogroupSex.getChildAt(1)).setChecked(true);
        }

        return this;
    }


    public SexDialog setOnOkListener(View.OnClickListener listener) {
        if (listener != null) {
            tvOk.setOnClickListener(listener);
        }
        return this;
    }

    public SexDialog setOnCancleListener(View.OnClickListener listener) {
        if (listener != null) {
            tvCancle.setOnClickListener(listener);
        }
        return this;
    }

    public int getSex() {
        return curSexId;
    }

    private static final Sex[] sexs = {
             new Sex(1, "男", R.mipmap.sex_gender_boy)
            , new Sex(2, "女", R.mipmap.sex_gender_girl)};

    public static Sex getSex(int index) {
        if (index < 0 || index >= sexs.length) index = 0;
        return sexs[index];
    }
}
