package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.wei.wlib.http.WLibHttpFlag;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.InfoMessageAdapter;
import com.yyspbfq.filmplay.bean.InfoMessageBean;
import com.yyspbfq.filmplay.bean.InfoMessageEntity;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoMessageActivity extends BaseActivity implements WLibHttpListener{

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, InfoMessageActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("我的消息");
        initViews();
        initData();
    }

    private PullToRefreshListView plv;
    private ListView lv;
    private InfoMessageAdapter adapter;
    private List<InfoMessageEntity> mData;
    private void initViews(){
        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(null);
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        List<String> list = new ArrayList<>();
        mData = new ArrayList<>();
        adapter = new InfoMessageAdapter(this, mData);
        lv.setAdapter(adapter);
        //关闭下拉
        plv.setPullRefreshEnabled(false);
        //关闭加载更多
        plv.setScrollLoadEnabled(true);

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (plv.isReadyForPullUp() && plv.hasMoreData()) {
                    scrollLoadMore();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private int page = 0;
    private int size = 20;
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("size", size+"");
        Factory.resp(this, HttpFlag.FLAG_MESSAGE_SHOW, null, InfoMessageBean.class).post(map);
    }

    private void getList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("size", size+"");
        Factory.resp(this, HttpFlag.FLAG_MESSAGE_SHOW, null, InfoMessageBean.class).post(map);
    }

    private boolean isLoading = false;
    private void scrollLoadMore() {
        if (isLoading) return;
        isLoading = true;
        page++;
        getList();
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_MESSAGE_SHOW) {
            try {
                InfoMessageBean bean = (InfoMessageBean) formatData;
                List<InfoMessageEntity> list = bean.getData();
                if (list==null||list.size()==0) {
                    if (page==0) {

                    } else {
                        page--;
                        plv.setHasMoreData(false);
                    }
                } else {
                    if (page==0) {
                        mData.clear();
                        UserDataUtil.saveMessageLastTime(this, System.currentTimeMillis()/1000+"");
                    }
                    mData.addAll(list);
                    adapter.update(mData);
                    if (list.size()<bean.getSize()) {
                        plv.setHasMoreData(false);
                    } else {
                        plv.setHasMoreData(true);
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
        if (flag == HttpFlag.FLAG_MESSAGE_SHOW) {
            try {
                if (errorType== WLibHttpFlag.HTTP_ERROR_DATA_EMPTY) {
                    if (page==0) {
                        if (!TextUtils.isEmpty(hint)) showToast(hint);
                    } else {
                        page--;
                        plv.setHasMoreData(false);
                    }
                } else {
                    if (page>0) {
                        page--;
                    }
                }
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    public void handleAfter(int flag, Object tag) {
        if (flag == HttpFlag.FLAG_MESSAGE_SHOW) {
            try {
                isLoading = false;
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }
}
