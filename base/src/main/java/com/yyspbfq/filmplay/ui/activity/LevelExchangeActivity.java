package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.wlib.http.WLibHttpFlag;
import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.biz.login.UserHelper;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.HashMap;
import java.util.Map;

public class LevelExchangeActivity extends BaseActivity implements WLibHttpListener{


    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, LevelExchangeActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_rechange);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("VIP等级兑换");

        initViews();
    }

    private EditText et;
    private TextView tv_btn;
    private void initViews() {
        et = findViewById(R.id.et);
        tv_btn = findViewById(R.id.tv_btn);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (TextUtils.isEmpty(s)) {
                        tv_btn.setSelected(false);
                    } else {
                        tv_btn.setSelected(true);
                    }
                } catch (Exception e){
                    BLog.e(e);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toExchange();
            }
        });
    }

    private boolean isLoading  = false;
    private void toExchange() {
        if (!tv_btn.isSelected()) return;
        try {
            String exchangeCode = et.getText().toString().trim();
            if (TextUtils.isEmpty(exchangeCode)) {
                showToast("请输入兑换码");
                return;
            }
            if (isLoading) {
                showToast("正在兑换中");
                return;
            }
            isLoading = true;
            Map<String, String> map = new HashMap<>();
            map.put("code", exchangeCode);
            Factory.resp(this, HttpFlag.FLAG_LEVEL_EXCHANGE, null, null).post(map);
        } catch (Exception e){
            BLog.e(e);
        }

    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_LEVEL_EXCHANGE) {
            try {
                showToast("兑换成功");
                et.setText("");
                UserHelper.getInstance().getUserInfo(this);
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
        if (flag==HttpFlag.FLAG_LEVEL_EXCHANGE&&!TextUtils.isEmpty(hint)) {
            if (errorType== WLibHttpFlag.HTTP_ERROR_CODE) {
                showToast(hint);
            }
        }
    }

    @Override
    public void handleAfter(int flag, Object tag) {
        if (flag==HttpFlag.FLAG_LEVEL_EXCHANGE) {
            isLoading = false;
        }
    }
}
