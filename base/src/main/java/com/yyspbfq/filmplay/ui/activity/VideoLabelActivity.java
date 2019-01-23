package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.wei.wlib.http.WLibHttpFlag;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.VideoListAdapter;
import com.yyspbfq.filmplay.bean.SearchBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoLabelActivity extends BaseActivity implements WLibHttpListener{

    private PullToRefreshListView plv;
    private ListView lv;
    private List<VideoEntity> mData;
    private VideoListAdapter adapter;
    private final String nodataFormat = "没有找到与“<font color=#AA8969>%s</font>”，相关的结果\n换个词搜搜看吧~";
    private String mLabel = "";

    public static void actionStart(Context context, String label) {
        Intent intent = new Intent(context, VideoLabelActivity.class);
        intent.putExtra("video_label", label);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_label);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("标签筛选");
        mLabel = getIntent().getStringExtra("video_label");
        if (TextUtils.isEmpty(mLabel)) return;
        initViews();
        initData();
    }

    private View v_result, v_no_data;
    private TextView tv_hint_search, tv_hint_no_data;
    private void initViews() {

        v_result = findViewById(R.id.v_result);
        v_no_data = findViewById(R.id.v_no_data);

        tv_hint_search = findViewById(R.id.tv_hint_search);
        tv_hint_no_data = findViewById(R.id.tv_hint_no_data);

        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(new ColorDrawable(Color.TRANSPARENT));
        lv.setDividerHeight(DensityUtils.dp2px(this, 10));
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        mData = new ArrayList<>();
        adapter = new VideoListAdapter(this, mData);
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

    private int page = 0;
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("keyword", mLabel);
        Factory.resp(this, HttpFlag.FLAG_SEARCH_KEYWORD, null, SearchBean.class).post(map);
    }

    private boolean isLoading = false;
    private void scrollLoadMore() {
        if (isLoading) return;
        isLoading = true;
        page++;
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("keyword", mLabel);
        Factory.resp(this, HttpFlag.FLAG_SEARCH_KEYWORD, null, SearchBean.class).post(map);
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_SEARCH_KEYWORD) {
            try {
                SearchBean bean = (SearchBean) formatData;
                List<VideoEntity> list = bean.getSearchData();
                if (list==null||list.size()==0) {
                    if (page==0) {
                        plv.setVisibility(View.GONE);
                        v_result.setVisibility(View.VISIBLE);
                        v_no_data.setVisibility(View.VISIBLE);
                        tv_hint_no_data.setText(Html.fromHtml(String.format(nodataFormat, tag.toString())));
                        tv_hint_search.setText(Html.fromHtml("搜索“<font color=#AA8969>"+tag.toString()+"</font>”,共有0条结果"));
                    } else {
                        page--;
                        plv.setHasMoreData(false);
                    }
                } else {
                    if (page==0) {
                        mData.clear();
                    }
                    mData.addAll(list);
                    adapter.update(mData);
                    if (list.size()<bean.getSize()) {
                        plv.setHasMoreData(false);
                    } else {
                        plv.setHasMoreData(true);
                    }
                    tv_hint_search.setText(Html.fromHtml("搜索“<font color=#AA8969>"+tag.toString()+"</font>”,共有"+bean.getSearchCount()+"条结果"));
                    if (v_result.getVisibility()!=View.VISIBLE) {
                        v_result.setVisibility(View.VISIBLE);
                    }
                    if (v_no_data.getVisibility()!=View.GONE) {
                        v_no_data.setVisibility(View.GONE);
                    }
                    if (plv.getVisibility()!=View.VISIBLE) {
                        plv.setVisibility(View.VISIBLE);
                    }

                }
            } catch (Exception e){
                handleError(flag, tag, WLibHttpFlag.HTTP_ERROR_OTHER, null, null);
                BLog.e(e);
            }
        }
    }

    @Override
    public void handleLoading(int flag, Object tag, boolean isShow) {

    }

    @Override
    public void handleError(int flag, Object tag, int errorType, String response, String hint) {
        if (flag==HttpFlag.FLAG_SEARCH_KEYWORD) {
            try {
                if (errorType== WLibHttpFlag.HTTP_ERROR_DATA_EMPTY) {
                    if (page==0) {
                        plv.setVisibility(View.GONE);
                        v_result.setVisibility(View.VISIBLE);
                        v_no_data.setVisibility(View.VISIBLE);
                        tv_hint_no_data.setText(Html.fromHtml(String.format(nodataFormat, tag.toString())));
                        tv_hint_search.setText(Html.fromHtml("搜索“<font color=#AA8969>"+tag.toString()+"</font>”,共有0条结果"));
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
        if (flag==HttpFlag.FLAG_SEARCH_KEYWORD) {
            try {
                isLoading = false;
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }
}
