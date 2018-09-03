package com.fuyou.play.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fuyou.play.R;
import com.fuyou.play.bean.BaseBean;
import com.fuyou.play.bean.UserInfo;
import com.fuyou.play.bean.login.LoginPhoneBean;
import com.fuyou.play.biz.Factory;
import com.fuyou.play.biz.http.HttpFlag;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.biz.xmpp.XmppConnection;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/5/11 0011.
 * 手机登录注册
 *
 * @author wwei
 */
public class MobileLoginActivity extends BaseActivity implements View.OnClickListener, HttpRepListener {

    private Context mContext;
    private static final int UPDATA_TIME = 1;
    private static final int UPDATA_STOP = 2;
    private ProgressDialog dialog;
    private MyHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mobile_login);
        initStatusBar(findViewById(R.id.status_bar));
        setStatusBarFontIconDark(true);
        mContext = this;
        initViews();
        initData();
    }

    //手机注册按钮
    private TextView tv_code, tv_login;
    private EditText et_phone, et_code;

    private void initViews() {
        dialog = new ProgressDialog(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
        tv_code = findViewById(R.id.tv_code);
        tv_code.setAlpha(0.5f);
        tv_code.setOnClickListener(this);
        et_phone = findViewById(R.id.et_mobile);
        et_code = findViewById(R.id.et_code);
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (checkBtnSelect(s, et_code.getText())) {
                    tv_login.setSelected(true);
                    tv_login.setClickable(true);
                } else {
                    tv_login.setSelected(false);
                    tv_login.setClickable(false);
                }
                if (isPhoneNum(s.toString().trim()) && tills == 0) {
                    tv_code.setAlpha(1f);
                    tv_code.setClickable(true);
                }
            }
        });
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (checkBtnSelect(et_phone.getText(), s)) {
                    tv_login.setSelected(true);
                    tv_login.setClickable(true);
                } else {
                    tv_login.setSelected(false);
                    tv_login.setClickable(false);
                }
            }
        });
    }

    private void initData() {
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, String> map = new HashMap<>();
        if (flag == HttpFlag.LOGIN) {
            map.put("type", "2");
            map.put("PhoneNum", et_phone.getText().toString().trim());
            map.put("Code", et_code.getText().toString().trim());
        } else if (flag == HttpFlag.PHONE_CODE) {
            map.put("phone", et_phone.getText().toString().trim());
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {
        if (flag == HttpFlag.LOGIN) {
            LoginPhoneBean bean = (LoginPhoneBean) response;
            if (bean.getStatus() == 0 && bean.getData() != null) {
                //手机登录成功
                UserDataUtil.saveUserData(this, bean.getData());
                registerWithLogin(bean.getData().getId()+"", "123456", bean.getData());
                /*if (bean.getData().getChat()==0) {
                    registerWithLogin(bean.getData().getId()+"", "123456", bean.getData());
                } else {

                    *//*startActivity(MainActivity.class);
                    setResult(67);
                    finish();*//*
                }*/
            } else {
                showBaseError(bean);
            }
            tv_login.setClickable(true);
        } else if (flag == HttpFlag.PHONE_CODE) {
            BaseBean bean = (BaseBean) response;
            if (bean.getStatus() == 0) {
                showToast("验证码已发送，请注意查收!");
            } else {
                showBaseError(bean);
            }
        }
    }

    private void registerWithLogin(final String account, final String password, final UserInfo userInfo) {
        LogCustom.show("account:"+account+",pw:"+password);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        // 群聊，加入聊天室，并且监听聊天室消息

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
                startActivity(MainActivity.class);
                setResult(67);
                finish();
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int threadId = (int) Thread.currentThread().getId();
                    long appStartTime = System.currentTimeMillis();
                    if (!XmppConnection.getInstance().isConnection()||!XmppConnection.getInstance().getConnection().isConnected()) {
                        for (int i = 0; i < 10; i++) {
                            LogCustom.i("connectChatServer", "i=" + i);
                            XmppConnection.getInstance().openConnection();
                            if (XmppConnection.getInstance().getConnection()!=null&&XmppConnection.getInstance().getConnection().isConnected()) {
                                break;
                            }
                        }
                        long connectOverTime = System.currentTimeMillis();
                        LogCustom.show("connectChatServer", "connect thread " + threadId + "time=" + (connectOverTime - appStartTime));
                        LogCustom.show("connectChatServer", "连接服务器结果" + XmppConnection.getInstance().getConnection().isConnected());
                    }
                    if (userInfo.getChat()==0) {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", userInfo.getNickName());
                        map.put("email", "");
                        map.put("icon", userInfo.getIcon());
                        if (XmppConnection.getInstance().register(account, password, map)) {
                            LogCustom.show("注册成功");
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        } else {
                            LogCustom.show("注册失败");
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    } else {
                        Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);
                    }
                } catch (Exception e){
                    ExceptionUtils.ExceptionSend(e);
                    LogCustom.show("注册失败");
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }

            }
        }).start();
    }

    @Override
    public void showLoading(int flag, Object obj) {
        if (flag == HttpFlag.LOGIN) {
            isShowLoadDialog(true);
        }
    }

    @Override
    public void hideLoading(int flag, Object obj) {
        if (flag == HttpFlag.LOGIN) {
            isShowLoadDialog(false);
        }
    }

    @Override
    public void showError(int flag, Object obj, int errorType) {
        if (flag == HttpFlag.LOGIN) {
            tv_login.setClickable(true);
            isShowLoadDialog(false);
        }
    }

    private void isShowLoadDialog(boolean show) {
        if (dialog == null) return;
        if (show) {
            dialog.setMessage("正在登录...");
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_login:
                checkMobileLogin();
                break;
            case R.id.tv_code:
                getCode();
                break;
        }
    }

    private void getCode() {
        if (tv_code.getAlpha() != 1f) return;
        String NNum = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(NNum)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isPhoneNum(NNum)) {
            Toast.makeText(this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        tv_code.setAlpha(0.5f);
        setPinHint();
        Factory.getHttpRespBiz(this, HttpFlag.PHONE_CODE, null).post();
    }

    private void checkMobileLogin() {
        if (!tv_login.isSelected()) return;
        String NNum = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(NNum)) {
            Toast.makeText(this, "请输入手机号，并获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isPhoneNum(NNum)) {
            Toast.makeText(this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        String mNum = et_code.getText().toString().trim();
        if (TextUtils.isEmpty(mNum)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isPin(mNum)) {
            Toast.makeText(this, "验证码为6位数字", Toast.LENGTH_SHORT).show();
            return;
        }
        tv_login.setClickable(false);
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);
        map.put("phone_num", NNum);
        map.put("code", mNum);
        Factory.getHttpRespBiz(MobileLoginActivity.this, HttpFlag.LOGIN, map).post();
    }

    private static final String REGEX_PHONE = "^[1][0-9]{10}$";
    private static final String REGEX_PIN = "^[0-9]{6}$";

    private boolean isPhoneNum(String str) {
        Pattern p = Pattern.compile(REGEX_PHONE);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private boolean isPin(String str) {
        Pattern p = Pattern.compile(REGEX_PIN);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private void setPinHint() {
        if (mHandler == null) {
            mHandler = new MyHandler(this);
        }
        tills = 60;
        mHandler.post(mPinRunnable);
    }

    private void updataTime(int flag) {
        if (flag == UPDATA_TIME) {
            tv_code.setText(getString(R.string.login_release_v_code)+"(" + tills + "秒)");
        } else {
            tv_code.setClickable(true);
            tv_code.setAlpha(1f);
            tv_code.setText(getString(R.string.login_release_v_code));
        }
    }

    private int tills = 0;
    private Runnable mPinRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mBaseExit) {
                tills--;
                if (tills > 0) {
                    mHandler.sendEmptyMessage(UPDATA_TIME);
                    mHandler.postDelayed(mPinRunnable, 1000);
                } else if (tills == 0) {
                    mHandler.sendEmptyMessage(UPDATA_STOP);
                }
            }
        }
    };

    private boolean checkBtnSelect(Editable e1, Editable e2) {
        try {
            if (!TextUtils.isEmpty(e1.toString().trim()) && !TextUtils.isEmpty(e2.toString().trim())) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MobileLoginActivity> mActivity;

        public MyHandler(MobileLoginActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        private MobileLoginActivity activity;

        @Override
        public void handleMessage(Message msg) {
            activity = mActivity.get();
            if (msg.what == UPDATA_TIME) {
                activity.updataTime(UPDATA_TIME);
            } else if (msg.what == UPDATA_STOP) {
                activity.updataTime(UPDATA_STOP);
            }
        }
    }

}
