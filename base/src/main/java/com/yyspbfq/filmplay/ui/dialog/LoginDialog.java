package com.yyspbfq.filmplay.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.biz.login.RegexUtils;
import com.yyspbfq.filmplay.biz.login.UserHelper;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 登陆浮层
 */
public class LoginDialog extends Dialog {

    private Context mContext;
    private EditText phone_number_edit;
    private EditText code_edit;
    private TextView code_button;
    private Button loginBtn;

    public LoginDialog(Context context) {
        this(context, R.style.dialog_login_style);
    }

    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_login_mobile);
        ((TextView) findViewById(R.id.tv_base_title)).setText("请登录");

        findViewById(R.id.iv_base_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog.this.dismiss();
            }
        });

        phone_number_edit = (EditText) findViewById(R.id.phone_number_edit);
        phone_number_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 11) {
                    if(timer == null) {
                        enableCodeBtn(true);
                    }
                } else {
                    enableCodeBtn(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        code_edit = (EditText) findViewById(R.id.code_edit);
        code_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >= 4) {
                    loginBtn.setEnabled(true);
                }else {
                    loginBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code_button = (TextView) findViewById(R.id.code_button);
        code_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });

        loginBtn = (Button) findViewById(R.id.mobile_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phone_number_edit.getText().toString().trim();
                String code = code_edit.getText().toString().trim();
                if (!RegexUtils.checkMobile(phone)) {
                    ToastUtils.showToast("请输入正确手机号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showToast("请输入验证码");
                    return;
                }
                UserHelper.getInstance().loginMobile(mContext, LoginDialog.this, phone, code, null);
            }
        });

        enableCodeBtn(false);
        loginBtn.setEnabled(false);
        ((BaseActivity) mContext).setStatusBarColor();
        try {
            WindowManager m = ((Activity) mContext).getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
            p.width = (int) (d.getWidth());
            p.height = (int) (d.getHeight() + ((BaseActivity) mContext).getStatusBarHeight());
            getWindow().setAttributes(p);
            getWindow().setGravity(Gravity.TOP);

        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void loginMobile() {

    }

    private boolean fullModel = false;
    public void setFullModel() {
        fullModel = true;
        ((BaseActivity) mContext).setStatusBarTranslucent(R.color.base_title_background);
//        initStatusBar(findViewById(R.id.status_bar));
    }

    //设置状态栏
    public void initStatusBar(View v){
        try {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
                ViewGroup.LayoutParams params=v.getLayoutParams();
                params.height= getStatusBarHeight();
                v.setLayoutParams(params);
            }
        }catch (Exception e){
            BLog.e(e, "initStatusBar");
        }
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private static final long MAX_TIME = 60;
    private CountDownTimer timer;

    //todo
    private void getCode() {

        String phone = phone_number_edit.getText().toString().trim();

        if (!RegexUtils.checkMobile(phone)) {
            ToastUtils.showToast("请输入正确手机号");
            return;
        }
        if (timer == null) {
            timer = new CountDownTimer(MAX_TIME * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    code_button.setText("已发送 " + (millisUntilFinished + 15) / 1000);
                }
                @Override
                public void onFinish() {
                    enableCodeBtn(true);
                }
            };
        }
        timer.start();
        enableCodeBtn(false);
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", phone);
        Factory.resp(new WLibHttpListener() {
            @Override
            public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {

            }

            @Override
            public void handleLoading(int flag, Object tag, boolean isShow) {

            }

            @Override
            public void handleError(int flag, Object tag, int errorType, String response, String hint) {
                if (errorType == HttpFlag.HTTP_ERROR_CODE&&!TextUtils.isEmpty(hint)) {
                    ToastUtils.showToast(hint);
                }
                enableCodeBtn(true);
            }

            @Override
            public void handleAfter(int flag, Object tag) {

            }
        }, HttpFlag.FLAG_MOBILE_CODE, null, null).post(map);
        /*Factory.resp(new HttpRepListener() {
            @Override
            public void handleResp(Object data, int flag, Object obj, String response, String hint) {}
            @Override
            public void showLoading(int flag, Object obj) {}
            @Override
            public void hideLoading(int flag, Object obj) {}
            @Override
            public void handleError(int flag, Object obj, int errorType, String response, String hint) {
                if (errorType == HttpFlag.HTTP_ERROR_CODE&&!TextUtils.isEmpty(hint)) {
                    ToastUtils.showToast(hint);
                }
                enableCodeBtn(true);
            }

            @Override
            public void handleAfter(int flag, Object obj) {}
        }, HttpFlag.FLAG_LOGIN_MOBILE_CODE, null, null).get(map);*/

    }

    private void enableCodeBtn(boolean enable) {
        if (enable) {
            code_button.setTextColor(Color.parseColor("#E3B88E"));
            code_button.setText("获取验证码");
            code_button.setEnabled(true);
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } else {
            code_button.setTextColor(Color.parseColor("#999999"));
            code_button.setEnabled(false);
        }
    }

    @Override
    public void dismiss() {
        try {
            if (fullModel) {
                ((BaseActivity) mContext).clearLoginFullModel();
            } else {
                ((BaseActivity) mContext).clearStatusBarColor();
            }
        } catch (Exception e){

        }
        super.dismiss();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void show() {
        super.show();
    }

}
