package com.yyspbfq.filmplay.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.wlib.http.WLibHttpFlag;
import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends BaseActivity implements WLibHttpListener{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
//        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("意见反馈");
        initViews();
    }

    private TextView tv_option_1, tv_option_2, tv_option_3, tv_option_4, tv_option_5, tv_option_6;
    private EditText et;
    private EditText et_mobile;
    private View submit;
    private TextView[] options;
    private int selectIndex = 0;
    private void initViews() {
        tv_option_1 = findViewById(R.id.feedback_option1);
        tv_option_2 = findViewById(R.id.feedback_option2);
        tv_option_3 = findViewById(R.id.feedback_option3);
        tv_option_4 = findViewById(R.id.feedback_option4);
        tv_option_5 = findViewById(R.id.feedback_option5);
        tv_option_6 = findViewById(R.id.feedback_option6);
        et = findViewById(R.id.feedback_edit_content);
        et_mobile = findViewById(R.id.feedback_edit_contact);
        submit = findViewById(R.id.feedback_submit);
        options = new TextView[]{tv_option_1, tv_option_2, tv_option_3, tv_option_4, tv_option_5, tv_option_6};
        for (int i=0; i<options.length; i++) {
            final int temp = i;
            options[temp].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectIndex = temp;
                    chooseOption(options[temp]);
                }
            });
        }
        options[selectIndex].setSelected(true);
        submit.setSelected(true);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSubmit();
            }
        });
    }

    private void chooseOption(TextView tv) {
        if (tv.isSelected()) return;
        tv_option_1.setSelected(false);
        tv_option_2.setSelected(false);
        tv_option_3.setSelected(false);
        tv_option_4.setSelected(false);
        tv_option_5.setSelected(false);
        tv_option_6.setSelected(false);
        tv.setSelected(true);
    }

    private boolean isLoading = false;
    private void toSubmit() {
        try {
            if (isLoading) return;
            String content = et.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                showToast("请输入描述");
                return;
            }
            isLoading = true;
            String contact = et_mobile.getText().toString().trim();
            Map<String, String> map = new HashMap<>();
            map.put("type", (selectIndex+1)+"");
            map.put("content", content);
            if (!TextUtils.isEmpty(contact)) {
                map.put("contact", contact);
            }
            Factory.resp(this, HttpFlag.FLAG_FEEDBACK_SEND, null, null).post(map);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private boolean isSend = false;
    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_FEEDBACK_SEND) {
            try {
                et.setText("");
                et_mobile.setText("");
                isSend = true;
                if (!TextUtils.isEmpty(hint)) showToast(hint);
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    public void handleLoading(int flag, Object tag, boolean isShow) {

    }

    @Override
    public void handleError(int flag, Object tag, int errorType, String response, String hint) {
        if (flag==HttpFlag.FLAG_FEEDBACK_SEND&&!TextUtils.isEmpty(hint)) {
            if (errorType== WLibHttpFlag.HTTP_ERROR_CODE) {
                showToast(hint);
            }
        }
    }

    @Override
    public void handleAfter(int flag, Object tag) {
        if (flag==HttpFlag.FLAG_FEEDBACK_SEND) {
            isLoading = false;
            if (isSend) {
                finish();
            }
        }
    }
}
