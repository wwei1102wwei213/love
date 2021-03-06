package com.yyspbfq.filmplay.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.wei.wlib.http.WLibHttpFlag;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.FeedbackMessageAdapter;
import com.yyspbfq.filmplay.bean.FeedbackMessageBean;
import com.yyspbfq.filmplay.bean.FeedbackMessageEntity;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackMessageActivity extends BaseActivity implements WLibHttpListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_message);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("客服反馈");
        initViews();
        initData();
    }

    private PullToRefreshListView plv;
    private ListView lv;
    private FeedbackMessageAdapter adapter;
    private List<FeedbackMessageEntity> mData;
    private void initViews(){
        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(null);
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        List<String> list = new ArrayList<>();
        mData = new ArrayList<>();
        adapter = new FeedbackMessageAdapter(this, mData);
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
        TextView tv_title_right = (TextView) findViewById(R.id.tv_base_right);
        tv_title_right.setText("我要反馈");
        tv_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FeedbackActivity.class);
            }
        });
        tv_title_right.setVisibility(View.VISIBLE);
    }

    private int page = 0;
    private int size = 20;
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("size", size+"");
        Factory.resp(this, HttpFlag.FLAG_FEEDBACK_SHOW, null, FeedbackMessageBean.class).post(map);
    }

    private void getList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("size", size+"");
        Factory.resp(this, HttpFlag.FLAG_FEEDBACK_SHOW, null, FeedbackMessageBean.class).post(map);
    }

    private void reList() {
        page=0;
        getList();
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
        if (flag == HttpFlag.FLAG_FEEDBACK_SHOW) {
            try {
                FeedbackMessageBean bean = (FeedbackMessageBean) formatData;
                List<FeedbackMessageEntity> list = bean.getData();
                if (list==null||list.size()==0) {
                    if (page==0) {
                        plv.setVisibility(View.GONE);
                        findViewById(R.id.v_no_data).setVisibility(View.VISIBLE);
                    } else {
                        page--;
                        plv.setHasMoreData(false);
                    }
                } else {
                    if (page==0) {
                        mData.clear();
                        UserDataUtil.saveFeedbackLastTime(this, System.currentTimeMillis()/1000+"");
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

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            reList();
        } catch (Exception e){

        }
    }
}
