package com.yyspbfq.filmplay.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.FeedbackMessageBean;
import com.yyspbfq.filmplay.bean.FeedbackMessageEntity;
import com.yyspbfq.filmplay.bean.InfoMessageBean;
import com.yyspbfq.filmplay.bean.InfoMessageEntity;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.UiUtils;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMessageActivity extends BaseActivity implements WLibHttpListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("我的消息");
        initViews();
        initData();
    }

    private void initViews() {
        findViewById(R.id.v_system).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(InfoMessageActivity.class);
                findViewById(R.id.v_red_system).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.v_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FeedbackMessageActivity.class);
                findViewById(R.id.v_red_feedback).setVisibility(View.GONE);
            }
        });
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", "0");
        map.put("size", "1");
        Factory.resp(this, HttpFlag.FLAG_MESSAGE_SHOW, null, InfoMessageBean.class).post(map);
        Factory.resp(this, HttpFlag.FLAG_FEEDBACK_SHOW, null, FeedbackMessageBean.class).post(map);
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_MESSAGE_SHOW) {
            try {
                InfoMessageBean bean = (InfoMessageBean) formatData;
                List<InfoMessageEntity> list = bean.getData();
                if (list!=null&&list.size()>0) {
                    String newLastTime = list.get(0).getCtime();
                    boolean isRed = UiUtils.handleMessageRedPoint(newLastTime, UserDataUtil.getMessageLastTime(this));
                    if (isRed) {
                        findViewById(R.id.v_red_system).setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_FEEDBACK_SHOW) {
            try {
                FeedbackMessageBean bean = (FeedbackMessageBean) formatData;
                List<FeedbackMessageEntity> list = bean.getData();
                if (list!=null&&list.size()>0) {
                    String newLastTime = list.get(0).getRetime();
                    boolean isRed = UiUtils.handleMessageRedPoint(newLastTime, UserDataUtil.getFeedbackLastTime(this));
                    if (isRed) {
                        findViewById(R.id.v_red_feedback).setVisibility(View.VISIBLE);
                    }
                }
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

    }

    @Override
    public void handleAfter(int flag, Object tag) {

    }
}
