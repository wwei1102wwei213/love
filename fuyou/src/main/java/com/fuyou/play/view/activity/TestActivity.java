package com.fuyou.play.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fuyou.play.R;
import com.fuyou.play.adapter.PostListAdapter;
import com.fuyou.play.bean.discuss.DiscussBaseBean;
import com.fuyou.play.bean.discuss.DiscussEntity;
import com.fuyou.play.biz.http.HttpFlag;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.pullwidget.pullrefresh.ElasticityListView;
import com.fuyou.play.widget.pullwidget.pullrefresh.PullElasticityListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends BaseActivity implements HttpRepListener {

    private List<DiscussEntity> list;
    private PostListAdapter adapter;
    private PullElasticityListView pv;
    private ElasticityListView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initViews();
        initData();
    }


    private void initViews(){

    }

    private void initData() {

    }

    private int order = 0;
    private String LastID = "";
    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, String> map = new HashMap<>();
        if (flag == HttpFlag.DISCUSS_QUERY) {
            map.put("InfoID", UserDataUtil.getUserID(this));
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
