package com.yyspbfq.filmplay.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.DiscoverAdapter;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.ui.BaseFragment;
import com.yyspbfq.filmplay.ui.activity.VideoSearchActivity;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.Jzvd;

public class DiscoverFragment extends BaseFragment implements WLibHttpListener{

    private PullToRefreshListView plv;
    private ListView lv;
    private DiscoverAdapter adapter;
    SensorManager sensorManager;
    Jzvd.JZAutoFullscreenListener sensorEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.base_fragment_discover, container, false);
        initStatusBar(findViewById(R.id.status_bar));
        initViews();
        initData();
        return rootView;
    }

    private void initViews() {

        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(null);
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        List<String> list = new ArrayList<>();
        adapter = new DiscoverAdapter(context, null);
        lv.setAdapter(adapter);
        //关闭下拉
        plv.setPullRefreshEnabled(false);
        //关闭加载更多
        plv.setScrollLoadEnabled(false);

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Jzvd.onScrollAutoTiny(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });

        sensorManager = (SensorManager) (context.getSystemService(Context.SENSOR_SERVICE));
        sensorEventListener = new Jzvd.JZAutoFullscreenListener();

        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSearchActivity.actionStart(context);
            }
        });
    }

    private int page = 0;
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        Factory.resp(this, HttpFlag.FLAG_MAIN_DISCOVER, page, null).post(map);
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_MAIN_DISCOVER) {
            try {
                List<VideoEntity> data = new Gson().fromJson(formatData.toString(), new TypeToken<List<VideoEntity>>(){}.getType());
                if (data!=null&&data.size()>0) {
                    adapter.update(data);
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

    @Override
    public void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        Jzvd.releaseAllVideos();
    }


}
