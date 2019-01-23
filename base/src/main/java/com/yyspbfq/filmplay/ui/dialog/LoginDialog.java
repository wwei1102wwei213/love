package com.yyspbfq.filmplay.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.biz.login.RegexUtils;
import com.yyspbfq.filmplay.biz.login.UserHelper;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 登陆浮层
 */
public class LoginDialog extends Dialog {

    private Context mContext;
    private TextView dialog_title;
    private EditText phone_number_edit;
    private EditText code_edit;
    private TextView code_button;
    private Button loginBtn;

    public LoginDialog(Context context) {
        this(context, R.style.dialog_base_style);
    }

    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_login_mobile);
        dialog_title = (TextView) findViewById(R.id.dialog_title);
        dialog_title.setTextSize(18f);
        dialog_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        dialog_title.setText("请登录");

        ImageView close = (ImageView) findViewById(R.id.dialog_close);
        close.setOnClickListener(new View.OnClickListener() {
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
    }

    private void loginMobile() {

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
            code_button.setTextColor(Color.parseColor("#f98700"));
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


    /*private Context mContext;
    private TextView dialog_title;
    private LayoutInflater inflater;
    private View view;

    public LoginDialog(Context context) {
        this(context,R.style.dialog_base_style);
    }

    public LoginDialog(final Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.custom_dialog_login, null);
        setContentView(view);

        dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        dialog_title.setTextSize(18f);
        dialog_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        dialog_title.setText("请登录");

        ImageView close = (ImageView) view.findViewById(R.id.dialog_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog.this.dismiss();
            }
        });

        LinearLayout wxLogin = (LinearLayout) view.findViewById(R.id.login_ll_wx);
        wxLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里不能关闭浮层
                LoginThirdUtil.wxLogin(mContext);
            }
        });

        LinearLayout qqLogin = (LinearLayout) view.findViewById(R.id.login_ll_qq);
        qqLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里不能关闭浮层
                LoginThirdUtil.qqLogin(mContext);
            }
        });

        LinearLayout mobileLogin = (LinearLayout) view.findViewById(R.id.login_ll_mobile);
        mobileLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog.this.dismiss();
                new LoginMoblieDialog(mContext).show();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {

        //第三方授权回调接收
        if (messageEvent.getMessage() == MessageEvent.MSG_PLATFORM_AUTHORIZE) {
            LoginDialog.this.dismiss();//关闭dialog
            if (messageEvent.getAuthorizePlatform() == MessageEvent.PLATFORM_WECHAT && messageEvent.getAuthorizeStatus() == MessageEvent.AUTHORIZE_OK) {
                Logger.d("微信授权回调接收");
                UserHelper.getInstance().loginWechat(mContext, messageEvent.getMsg());
            } else if (messageEvent.getAuthorizePlatform() == MessageEvent.PLATFORM_QQ && messageEvent.getAuthorizeStatus() == MessageEvent.AUTHORIZE_OK) {
                Logger.d("QQ授权回调接收");
                UserHelper.getInstance().loginQQ(mContext, messageEvent.getMsg());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }*/

}
