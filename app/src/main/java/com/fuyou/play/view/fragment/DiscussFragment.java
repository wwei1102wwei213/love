package com.fuyou.play.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.fuyou.play.R;
import com.fuyou.play.adapter.DiscussListAdapter;
import com.fuyou.play.bean.discuss.DiscussBaseBean;
import com.fuyou.play.bean.discuss.DiscussEntity;
import com.fuyou.play.biz.Factory;
import com.fuyou.play.biz.http.HttpFlag;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseFragment;
import com.fuyou.play.view.activity.DiscussPostActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-07-25.
 */
public class DiscussFragment extends BaseFragment implements HttpRepListener{

    private XRefreshView xrv;
    private RecyclerView rv;
    private DiscussListAdapter adapter;
    private List<DiscussEntity> list;

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
        xrv = (XRefreshView) findViewById(R.id.xrv);
        rv = (RecyclerView) findViewById(R.id.rv);
        try {
            rv.setHasFixedSize(true);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);
            list = new ArrayList<>();
            adapter = new DiscussListAdapter(context, list);
//            adapter.setLikeListener(this);
//            headerView = adapter.setHeaderView(R.layout.layout_post_list_category, rv);

            rv.setAdapter(adapter);
            xrv.setPinnedTime(1000);
            xrv.setPullLoadEnable(true);
            xrv.setMoveForHorizontal(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(context));
            xrv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                @Override
                public void onRefresh(boolean isPullDown) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (!mBaseExit) {
                                refresh = true;
                                LastID = "";
                                page = 0;
                                initData();
                            }
                        }
                    }, 1200);
                }
                @Override
                public void onLoadMore(boolean isSilence) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (!mBaseExit) {
                                loading = true;
                                page++;
                                initData();
                            }
                        }
                    }, 1200);
                }
            });
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "DiscussFragment initView");
        }
    }

    private void initData() {
        Factory.getHttpRespBiz(this, HttpFlag.DISCUSS_QUERY, null).post();
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
                    /*if (page>0) {
                        adapter.update(list, true);
                    } else {

                    }*/
                    adapter.update(list);
                    if (refresh||loading) {
                        overLoading(0);
                    }
                } else {
                    if (page==0) {
                        xrv.setVisibility(View.GONE);
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

    }

    private boolean refresh = false;
    private boolean loading = false;
    private int page = 0;
    private int pageSize = 15;
    private void overLoading(final int tag) {
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
        try {
            if (!mBaseExit) {
                if (tag == 0) {
                    if (refresh) {
                        xrv.stopRefresh();
                        xrv.setLoadComplete(false);
                    }
                    if (loading) {
                        xrv.stopLoadMore();
                    }
                } else if (tag == 1) {
                    if (refresh) {
                        xrv.stopRefresh(false);
                        xrv.setLoadComplete(false);
                    }
                    if (loading) {
                        page--;
                        xrv.stopLoadMore();
                    }
                } else if (tag == 2) {
                    if (refresh) {
                        xrv.stopRefresh();
                        xrv.setLoadComplete(false);
                    }
                    if (loading) {
                        page--;
                        xrv.stopLoadMore();
                        xrv.setLoadComplete(true);
                    }
                }
                if (refresh) refresh = false;
                if (loading) loading = false;
            }
        } catch (Exception e){

        }
//            }
//        }, 1400);
    }

    private boolean mBaseExit = false;
    @Override
    public void onDestroy() {
        mBaseExit = true;
        super.onDestroy();
    }
}
