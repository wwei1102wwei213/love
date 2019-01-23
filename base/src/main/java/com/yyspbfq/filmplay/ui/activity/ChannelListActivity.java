package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.ChannelHotAdapter;
import com.yyspbfq.filmplay.bean.ChannelDefaultBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelListActivity extends BaseActivity implements WLibHttpListener{

    private int type;
    private ChannelHotAdapter adapter;

    public static void startAction(Context context, int type){
        Intent intent = new Intent(context, ChannelListActivity.class);
        intent.putExtra("channel_type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_channel_list);
        initStatusBar();
        type = getIntent().getIntExtra("channel_type", 1);
        initViews();
        initData();
    }

    private void initViews() {
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText(getString(type==1?R.string.channel_menu_hot:R.string.channel_menu_human));
        GridView gv = findViewById(R.id.gv);
        adapter = new ChannelHotAdapter(this, null);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItem(position)!=null)
                ChannelDetailActivity.actionStart(ChannelListActivity.this, adapter.getItem(position));
            }
        });
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("type", type+"");
        Factory.resp(this, HttpFlag.FLAG_CHANNEL_LIST, null, null).post(map);
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_CHANNEL_LIST) {
            try {
                List<ChannelDefaultBean> data = new Gson().fromJson(formatData.toString(), new TypeToken<List<ChannelDefaultBean>>(){}.getType());
                adapter.update(data);
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
