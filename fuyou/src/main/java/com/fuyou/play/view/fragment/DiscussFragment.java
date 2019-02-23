package com.fuyou.play.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.adapter.PostListAdapter;
import com.fuyou.play.bean.discuss.DiscussBaseBean;
import com.fuyou.play.bean.discuss.DiscussEntity;
import com.fuyou.play.biz.Factory;
import com.fuyou.play.biz.http.HttpFlag;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseFragment;
import com.fuyou.play.view.activity.DiscussPostActivity;
import com.fuyou.play.widget.pullwidget.elasticity.ElasticityHelper;
import com.fuyou.play.widget.pullwidget.pullrefresh.ElasticityListView;
import com.fuyou.play.widget.pullwidget.pullrefresh.PullElasticityListView;
import com.fuyou.play.widget.pullwidget.pullrefresh.PullToRefreshBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-07-25.
 */
public class DiscussFragment extends BaseFragment implements HttpRepListener{

    private List<DiscussEntity> list;
    private PostListAdapter adapter;
    private PullElasticityListView pv;
    private ElasticityListView lv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_discuss, container, false);
        initStatusBar(findViewById(R.id.status_bar));
        initViews();
        initData();
        return rootView;
    }

    private void initViews(){
        ((TextView) findViewById(R.id.tv_title_base)).setText("社区");
        findViewById(R.id.iv_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(DiscussPostActivity.class);
            }
        });
        pv = (PullElasticityListView) findViewById(R.id.pv);
        lv = pv.getRefreshableView();
        lv.setFadingEdgeLength(0);
        lv.setDivider(null);
        lv.setVerticalScrollBarEnabled(false);
        list = new ArrayList<>();
        adapter = new PostListAdapter(context, list);
        lv.setAdapter(adapter);
        pv.setPullRefreshEnabled(true);
        pv.setScrollLoadEnabled(true);
        pv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ElasticityListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ElasticityListView> pullToRefreshBase) {
                if (!mBaseExit) {
                    refresh = true;
                    pv.onPullUpRefreshComplete();
                    LastID = "";
                    page = 0;
                    initData();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ElasticityListView> pullToRefreshBase) {

            }
        });
        /*pv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!mBaseExit&&pv.isReadyForPullUp() && pv.hasMoreData()) {
                    loading = true;
                    page++;
                    initData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/
        pv.setLoadingListener(new PullElasticityListView.OnLoadingListener() {
            @Override
            public void onPullUpLoading() {
                if (!mBaseExit) {
                    loading = true;
                    page++;
                    initData();
                }
            }
        });
        ElasticityHelper.setUpOverScroll(lv);
    }

    private void initData() {
        Factory.getHttpRespBiz(this, HttpFlag.DISCUSS_QUERY, page).post();
    }

    private int label = 0;
    private int order = 0;
    private String LastID = "";
    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, String> map = new HashMap<>();
        if (flag == HttpFlag.DISCUSS_QUERY) {
            map.put("InfoID", UserDataUtil.getUserID(context));
            map.put("LastID", LastID);
            map.put("order", ""+order);
            map.put("pagesize", "7");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {
        if (flag == HttpFlag.DISCUSS_QUERY) {
            DiscussBaseBean bean = (DiscussBaseBean) response;
            if (bean.getStatus()==0) {
                if (bean.getData()!=null&&bean.getData().size()>0) {
                    if (page==0) {
                        list = new ArrayList<>();
                    }
                    list.addAll(bean.getData());
                    LastID = list.get(list.size()-1).getId()+"";
                    adapter.update(list);
                    if (refresh||loading) {
                        overLoading(0);
                    }
                } else {
                    if (page==0) {
                        if (refresh||loading) {
                            overLoading(1);
                        }
                    } else {
                        if (loading) {
                            overLoading(2);
                        }
                    }
                }
            } else {
                overLoading(1);
                showBaseError(bean);
            }
        }
    }

    @Override
    public void showLoading(int flag, Object obj) {

    }

    @Override
    public void hideLoading(int flag, Object obj) {

    }

    @Override
    public void showError(int flag, Object obj, int errorType) {
        overLoading(1);
    }

    private boolean refresh = false;
    private boolean loading = false;
    private int page = 0;
    private int pageSize = 15;
    private void overLoading(final int tag) {
        if (refresh) pv.onPullDownRefreshComplete();
        if (loading) {
            if (tag==0){
                pv.setHasMoreData(true);
            } else if (tag==1) {
                pv.scrollLoadError();
            } else if (tag==2) {
                pv.setHasMoreData(false);
            }
        }
        refresh = false;
        loading = false;
    }

    private boolean mBaseExit = false;
    @Override
    public void onDestroy() {
        mBaseExit = true;
        super.onDestroy();
    }
}
