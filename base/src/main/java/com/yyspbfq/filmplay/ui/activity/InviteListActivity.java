package com.yyspbfq.filmplay.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.ListInviteeAdapter;
import com.yyspbfq.filmplay.bean.InviteListBean;
import com.yyspbfq.filmplay.bean.UserInvitee;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InviteListActivity extends BaseActivity implements WLibHttpListener{

    private ListView lv;
    private List<UserInvitee> mData;
    private ProgressBar pb;
    private ListInviteeAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_invite_list);
        initStatusBar(findViewById(R.id.status_bar));
        initViews();
        initData();
    }

    private View v_no_data;
    private void initViews() {
        ((TextView) findViewById(R.id.tv_base_title)).setText("我的推广");
        setBackViews(R.id.iv_base_back);
        lv = findViewById(R.id.lv);
        View v = LayoutInflater.from(this).inflate(R.layout.head_my_invite, lv, false);
        lv.addHeaderView(v);
        mData = new ArrayList<>();
        mAdapter = new ListInviteeAdapter(this, mData);
        lv.setAdapter(mAdapter);
        pb = (ProgressBar) findViewById(R.id.pb);
        v_no_data = findViewById(R.id.v_no_data);
        TextView tv_right = (TextView) findViewById(R.id.tv_base_right);
        tv_right.setText(getString(R.string.invite_friend));
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInvite();
            }
        });
        findViewById(R.id.tv_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInvite();
            }
        });
    }

    private void toInvite() {

    }

    private void initData() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("page", "0");
        map.put("size", "100");
        Factory.resp(this, HttpFlag.FLAG_INVITE_LIST, null, InviteListBean.class).post(map);
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_INVITE_LIST) {
            try {
                InviteListBean bean = (InviteListBean) formatData;
                mData = bean.getData();
                if (mData==null||mData.size()==0) {
                    lv.setVisibility(View.GONE);
                    v_no_data.setVisibility(View.VISIBLE);
                } else {
                    v_no_data.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
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
