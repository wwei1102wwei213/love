package com.yyspbfq.filmplay.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.widget.SoftKeyLayout;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import java.util.HashMap;
import java.util.Map;

public class CommentDialog extends Dialog{

    private Context context;

    public CommentDialog(@NonNull Context context) {
        this(context, R.style.dialog_comment_style);
    }

    public CommentDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
    }

    private EditText et;
    private TextView sent;
    private SoftKeyLayout skl;
//    private View v_space;
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_comment_sent, null);
        setContentView(view);
        et = view.findViewById(R.id.et);
        sent = view.findViewById(R.id.send_message);
//        v_space = view.findViewById(R.id.v_space);

        skl = view.findViewById(R.id.skl);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    sent.setSelected(false);
                } else {
                    sent.setSelected(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sent();
            }
        });
        skl.setOnSoftKeyboardListener(new SoftKeyLayout.OnSoftKeyboardListener() {
            @Override
            public void onShown() {
                try {
                    flag = true;
                    showSpace();
                } catch (Exception e) {
                    BLog.e(e);
                }
            }

            @Override
            public void onHidden() {
                try {
                    if (flag) {
                        hideInput();
                    }
                } catch (Exception e) {
                    BLog.e(e);
                }
            }
        });
        try {

            WindowManager m = ((Activity) context).getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
            p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.95
            getWindow().setAttributes(p);
            getWindow().setGravity(Gravity.BOTTOM);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private String vid = "";
    public void setVid(String vid) {
        this.vid = vid;
    }

    private void sent() {
        if (sent.isSelected()) {
            String str = et.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
                ToastUtils.showToast("请输入评论内容");
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("vid", vid);
            map.put("content",str);
            Factory.resp(new WLibHttpListener() {
                @Override
                public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
                    try {
                        if (listener!=null) listener.onCommentSuccessListener();
                        et.setText("");
                        ToastUtils.showToast(hint);
                        dismiss();
                    } catch (Exception e){
                        BLog.e(e);
                    }
                }

                @Override
                public void handleLoading(int flag, Object tag, boolean isShow) {

                }

                @Override
                public void handleError(int flag, Object tag, int errorType, String response, String hint) {
                    if (!TextUtils.isEmpty(hint)) {
                        ToastUtils.showToast(hint);
                    }
                }

                @Override
                public void handleAfter(int flag, Object tag) {

                }
            }, HttpFlag.FLAG_COMMENT_SENT, null, null).post(map);
        }
    }

    private void showSoftInput() {
        try {
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            et.setSelection(et.getSelectionEnd());
            InputMethodManager inputManager =
                    (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.showSoftInput(et, 5);
            inputManager.showSoftInput(et,InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            BLog.e(e);
        }
    }

    private boolean isFirst = false;
    private void showSpace() {
        /*try {
            if (isFirst) {
                ViewGroup.LayoutParams params = v_space.getLayoutParams();
                params.height = ((BaseActivity) context).getStatusBarHeight();
                v_space.setLayoutParams(params);
                isFirst = false;
            }
            v_space.setVisibility(View.VISIBLE);
        } catch (Exception e){
            BLog.e(e);
        }*/
    }

    private boolean flag = false;// 这个控制隐藏键盘的时候 回调只被调用一次
    private void hideInput() {
        try {
//            v_space.setVisibility(View.GONE);
            flag = false;
        } catch (Exception e) {
            BLog.e(e);
        }
    }

    private String content = "";

    private void hideKeyboard(IBinder token) {
        try {
            if (token != null) {
                content = et.getText().toString().trim();
                InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
//                Animation a = AnimationUtils.loadAnimation(context, R.anim.dialog_enter_anim);
            }
        } catch (Exception e) {
            BLog.e(e);
        }
    }

    @Override
    public void show() {

        super.show();
        if (et!=null) {
            showSoftInput();
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 300);*/
        }
    }

    private CommentSuccessListener listener;

    public void setListener(CommentSuccessListener listener) {
        this.listener = listener;
    }

    public interface CommentSuccessListener {
        void onCommentSuccessListener();
    }
}
