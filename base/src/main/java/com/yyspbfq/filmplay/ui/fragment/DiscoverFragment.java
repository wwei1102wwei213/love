package com.yyspbfq.filmplay.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wei.wlib.http.WLibHttpFlag;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshBase;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.DiscoverAdapter;
import com.yyspbfq.filmplay.bean.DiscoverDataBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.ui.BaseFragment;
import com.yyspbfq.filmplay.ui.activity.VideoSearchActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.tools.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.Jzvd;

public class DiscoverFragment extends BaseFragment implements WLibHttpListener{

    private PullToRefreshListView plv;
    private ListView lv;
    private DiscoverAdapter adapter;
    private List<VideoEntity> mData;

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
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        mData = new ArrayList<>();
        adapter = new DiscoverAdapter(context, mData);
        lv.setAdapter(adapter);
        //关闭下拉
        plv.setPullRefreshEnabled(true);
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
                try {
                    if (!adapter.isPlayed()) return;
                    if (adapter.getINDEX()==-1) return;
                    if (adapter.getINDEX()<firstVisibleItem||adapter.getINDEX()>(firstVisibleItem+visibleItemCount)) {
                        Jzvd.releaseAllVideos();
                        adapter.setPlayed(false);
                        adapter.setINDEX(-1);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e){
                    BLog.e(e);
                }

            }
        });

        plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSearchActivity.actionStart(context);
            }
        });
    }

    private boolean isLoading = false;

    private void loadData() {
        if (isLoading) return;
        Jzvd.releaseAllVideos();

        isLoading = true;
        page = 0;
        initData();
    }

    private void scrollLoadMore(){
        if (isLoading) return;
        isLoading = true;
        page++;
        initData();
    }

    private int page = 0;
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("size", "10");
        Factory.resp(this, HttpFlag.FLAG_MAIN_DISCOVER, page, DiscoverDataBean.class).post(map);
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_MAIN_DISCOVER) {
            try {
                DiscoverDataBean bean = (DiscoverDataBean) formatData;
                List<VideoEntity> list = bean.getData();
                if (list==null||list.size()==0) {
                    if (page>0) {
                        page--;
                        plv.setHasMoreData(false);
                    }
                } else {
                    adapter.update(list, page);
                    if (list.size()<bean.getSize()) {
                        plv.setHasMoreData(false);
                    } else {
                        plv.setHasMoreData(true);
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
        if (flag == HttpFlag.FLAG_MAIN_DISCOVER) {
            try {
                if (page>0) {
                    page--;
                    if (errorType== WLibHttpFlag.HTTP_ERROR_DATA_EMPTY) plv.setHasMoreData(false);
                }
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    public void handleAfter(int flag, Object tag) {
        if (flag == HttpFlag.FLAG_MAIN_DISCOVER) {
            isLoading = false;
            if ((int) tag == 0) {
                plv.setLastUpdatedLabel(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE_2));
                plv.onPullDownRefreshComplete();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Jzvd.releaseAllVideos();
        }
    }
}
