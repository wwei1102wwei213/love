package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wei.wlib.http.WLibHttpFlag;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.ChannelDetailAdapter;
import com.yyspbfq.filmplay.bean.ChannelDefaultBean;
import com.yyspbfq.filmplay.bean.ChannelSimilarityBean;
import com.yyspbfq.filmplay.bean.VideoShortBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChannelDetailActivity extends BaseActivity implements WLibHttpListener{

    private PullToRefreshListView plv;
    private ListView lv;
    private ChannelDetailAdapter adapter;
    private List<VideoShortBean> mData;
    private View mHeaderView;
    private String mId;
    private int page = 0;
    private final int size = 12;
    private int SORT_TYPE = 1;//排序 1:最后更新 2:最多播放

    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, ChannelDetailActivity.class);
        intent.putExtra("channel_id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_detail);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        mId = getIntent().getStringExtra("channel_id");
        if (TextUtils.isEmpty(mId)) return;
        initViews();
        initData();
    }

    private void initViews() {
        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(null);
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        mData = new ArrayList<>();
        adapter = new ChannelDetailAdapter(this, null);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.layout_header_channel_detail, lv, false);
        initHeaderView();
        lv.addHeaderView(mHeaderView);
        lv.setAdapter(adapter);
        //关闭下拉
        plv.setPullRefreshEnabled(false);
        //关闭加载更多
        plv.setScrollLoadEnabled(true);
        //设置滑动加载监听
        plv.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    private ImageView iv_top;
    private TextView tv_name, tv_remark, tv_sort_newest, tv_sort_most;
    private void initHeaderView(){
        iv_top = mHeaderView.findViewById(R.id.iv_top);
        tv_name = mHeaderView.findViewById(R.id.tv_name);
        tv_remark = mHeaderView.findViewById(R.id.tv_remark);
        tv_sort_most = mHeaderView.findViewById(R.id.tv_sort_most);
        tv_sort_newest = mHeaderView.findViewById(R.id.tv_sort_newest);
        tv_sort_newest.setSelected(true);
        tv_sort_newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSort(1);
            }
        });
        tv_sort_most.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSort(2);
            }
        });
    }

    private void setHeaderView(ChannelDefaultBean bean) {
        if (bean==null) return;
        try {
            Glide.with(this).load(bean.getThumb()).crossFade().into(iv_top);
            tv_remark.setText(bean.getRemark()==null?"没有介绍":bean.getRemark());
            tv_name.setText(bean.getTitle()==null?"未知专题":bean.getTitle());
            adapter.notifyDataSetChanged();
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void initData() {
        getDetail();
        getList();
    }

    private void getDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("id", mId);
        Factory.resp(this, HttpFlag.FLAG_CHANNEL_DETAIL, null, ChannelDefaultBean.class).post(map);
    }

    private void getList() {
        if (page==0) plv.setHasMoreData(true);
        Map<String, String> map = new HashMap<>();
        map.put("tid", mId);
        map.put("page", page+"");
        map.put("sort", SORT_TYPE+"");
        map.put("size", size+"");
        Factory.resp(this, HttpFlag.FLAG_CHANNEL_DETAIL_LIST, null, ChannelSimilarityBean.class).post(map);
    }

    private boolean isLoading = false;
    private void scrollLoadMore() {
        if (isLoading) return;
        isLoading = true;
        page++;
        getList();
    }

    private void clickSort(int type) {
        if (type==1&&tv_sort_newest.isSelected()) return;
        if (type==2&&tv_sort_most.isSelected()) return;
        tv_sort_newest.setSelected(type==1);
        tv_sort_most.setSelected(type==2);
        SORT_TYPE = type;
        page = 0;
        getList();
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_CHANNEL_DETAIL_LIST) {
            try {
                ChannelSimilarityBean bean = (ChannelSimilarityBean) formatData;
                List<VideoShortBean> list = bean.getList();
                if (list==null||list.size()==0) {
                    if (page>0) {
                        page--;
                        plv.setHasMoreData(false);
                    }
                } else {
                    if (page==0) {
                        mData.clear();
                    }
                    mData.addAll(list);
                    adapter.update(mData);
                    if (list.size()<size) {
                        plv.setHasMoreData(false);
                    }
                }
            } catch (Exception e){
                handleError(flag, tag, WLibHttpFlag.HTTP_ERROR_OTHER, null, null);
                BLog.e(e);
            }
        } else if (flag==HttpFlag.FLAG_CHANNEL_DETAIL) {
            try {
                ChannelDefaultBean bean = (ChannelDefaultBean) formatData;
                setHeaderView(bean);
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    public void handleLoading(int flag, Object tag, boolean isShow) {
        if (flag==HttpFlag.FLAG_CHANNEL_DETAIL_LIST) {

        } else if (flag==HttpFlag.FLAG_CHANNEL_DETAIL) {

        }
    }

    @Override
    public void handleError(int flag, Object tag, int errorType, String response, String hint) {
        if (flag==HttpFlag.FLAG_CHANNEL_DETAIL_LIST) {
            if (page>0) page--;
        } else if (flag==HttpFlag.FLAG_CHANNEL_DETAIL) {

        }
    }

    @Override
    public void handleAfter(int flag, Object tag) {
        if (flag==HttpFlag.FLAG_CHANNEL_DETAIL_LIST) {
            isLoading = true;
        } else if (flag==HttpFlag.FLAG_CHANNEL_DETAIL) {

        }
    }
}
