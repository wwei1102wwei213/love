package com.yyspbfq.filmplay.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.ChannelHotAdapter;
import com.yyspbfq.filmplay.bean.ChannelDefaultBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseFragment;
import com.yyspbfq.filmplay.ui.activity.ChannelDetailActivity;
import com.yyspbfq.filmplay.ui.activity.ChannelListActivity;
import com.yyspbfq.filmplay.ui.activity.VideoSearchActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.UiUtils;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;

import java.util.List;

public class ChannelFragment extends BaseFragment implements WLibHttpListener{

    private LinearLayout ll_recommend;
    private LinearLayout ll_human;
    private GridView gv_hot;
    private View more_hot, more_human;
    private ChannelHotAdapter hotAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.base_fragment_channel, container, false);
        initStatusBar(findViewById(R.id.status_bar));
        initScreenSize();
        initViews();
        initData();
        return rootView;
    }

    private int mItemW, mItemH;
    private void initScreenSize() {
        mItemW = (CommonUtils.getDeviceWidth(context) - DensityUtils.dp2px(context, 8))/2;
        mItemH = mItemW*9/16;
    }

    private void initViews() {
        ll_recommend = (LinearLayout) findViewById(R.id.ll_recommend);
        ll_human = (LinearLayout) findViewById(R.id.ll_human);
        more_hot = findViewById(R.id.more_hot);
        more_human = findViewById(R.id.more_human);
        more_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelListActivity.startAction(context, 1);
            }
        });
        more_human.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelListActivity.startAction(context, 2);
            }
        });
        gv_hot = (GridView) findViewById(R.id.gv_hot);
        hotAdapter = new ChannelHotAdapter(context, null);
        gv_hot.setAdapter(hotAdapter);
        gv_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (hotAdapter.getItem(position)!=null)
                    ChannelDetailActivity.actionStart(context, hotAdapter.getItem(position));
            }
        });
        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSearchActivity.actionStart(context);
            }
        });
    }

    private void initData() {
        Factory.resp(this, HttpFlag.FLAG_CHANNEL_RECOMMEND, null, null).post(null);
        Factory.resp(this, HttpFlag.FLAG_CHANNEL_HOT, null, null).post(null);
        Factory.resp(this, HttpFlag.FLAG_CHANNEL_HUMAN, null, null).post(null);
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_CHANNEL_RECOMMEND) {
            try {
                List<ChannelDefaultBean> data = new Gson().fromJson(formatData.toString(), new TypeToken<List<ChannelDefaultBean>>(){}.getType());
                UiUtils.handleChannelRecommend(context, ll_recommend, data, mItemH);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag==HttpFlag.FLAG_CHANNEL_HOT) {
            try {
                List<ChannelDefaultBean> data = new Gson().fromJson(formatData.toString(), new TypeToken<List<ChannelDefaultBean>>(){}.getType());
                hotAdapter.update(data);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag==HttpFlag.FLAG_CHANNEL_HUMAN) {
            try {
                List<ChannelDefaultBean> data = new Gson().fromJson(formatData.toString(), new TypeToken<List<ChannelDefaultBean>>(){}.getType());
                UiUtils.handleChannelHuman(context, ll_human, data);
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
