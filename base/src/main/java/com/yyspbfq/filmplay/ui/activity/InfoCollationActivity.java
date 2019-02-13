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
import com.wei.wlib.pullrefresh.PullToRefreshBase;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.CollationListAdapter;
import com.yyspbfq.filmplay.bean.DiscoverDataBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;
import com.yyspbfq.filmplay.utils.tools.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoCollationActivity extends BaseActivity implements WLibHttpListener{



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_collation);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("我的收藏");

        initViews();
        initData();
    }

    private TextView tv_right;
    private PullToRefreshListView plv;
    private ListView lv;
    private CollationListAdapter adapter;
    private List<VideoEntity> mData;
    private TextView tv_select_all, tv_delete;
    private View v_edit, v_no_data;
    private void initViews() {

        v_no_data = findViewById(R.id.v_no_data);

        tv_delete = findViewById(R.id.tv_delete);
        tv_select_all = findViewById(R.id.tv_select_all);
        v_edit = findViewById(R.id.v_edit);
        tv_right = findViewById(R.id.tv_base_right);
        tv_right.setText("编辑");
        tv_right.setVisibility(View.VISIBLE);
        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(new ColorDrawable(Color.TRANSPARENT));
        lv.setDividerHeight(DensityUtils.dp2px(this, 10));
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        mData = new ArrayList<>();
        adapter = new CollationListAdapter(this, mData, new CollationListAdapter.EditModelListener() {
            @Override
            public void modeChangeListener() {
                setEditModel(true);
            }
        });
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
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditMode();
            }
        });
        tv_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isAll()) {
                    tv_select_all.setText("全选");
                } else {
                    tv_select_all.setText("全不选");
                }
                adapter.selectAll();
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getSelects().size()==0) {
                    showToast("没有选中");
                } else {
                    toDelete();
                }
            }
        });
    }

    private int page = 0;
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("size", "12");
        Factory.resp(this, HttpFlag.FLAG_INFO_COLLATION, page, DiscoverDataBean.class).post(map);
    }

    private boolean isLoading = false;

    private void loadData() {
        if (isLoading) return;
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

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_INFO_COLLATION) {
            try {
                DiscoverDataBean bean = (DiscoverDataBean) formatData;
                List<VideoEntity> list = bean.getData();
                if (list==null||list.size()==0) {
                    if (page>0) {
                        page--;
                        plv.setHasMoreData(false);
                    }
                    if (page==0) {
                        plv.setVisibility(View.GONE);
                        v_no_data.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (page==0) {
                        mData.clear();
                        v_no_data.setVisibility(View.GONE);
                        plv.setVisibility(View.VISIBLE);
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
                handleError(flag, tag, WLibHttpFlag.HTTP_ERROR_OTHER, null, null);
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_DELETE_COLLATION) {
            try {
                adapter.deleteSelect();
                showToast("删除成功");
                if (adapter.getCount()==0) {
                    tv_right.setText("编辑");
                    v_edit.setVisibility(View.GONE);
                    plv.setVisibility(View.GONE);
                    v_no_data.setVisibility(View.VISIBLE);
                    adapter.setMODEL(0);
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
        if (flag == HttpFlag.FLAG_INFO_COLLATION) {
            try {
                if (page>0) {
                    page--;
                    if (errorType== WLibHttpFlag.HTTP_ERROR_DATA_EMPTY) plv.setHasMoreData(false);
                }
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_DELETE_COLLATION) {
            if (!TextUtils.isEmpty(hint)) showToast(hint);
        }
    }

    @Override
    public void handleAfter(int flag, Object tag) {
        if (flag == HttpFlag.FLAG_INFO_COLLATION) {
            isLoading = false;
            if ((int) tag == 0) {
                plv.setLastUpdatedLabel(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE_2));
                plv.onPullDownRefreshComplete();
            }
        } else if (flag == HttpFlag.FLAG_DELETE_COLLATION) {
            isDeleting = false;
        }
    }

    private boolean isDeleting = false;
    private void toDelete() {
        if (isDeleting) return;
        isDeleting = true;
        Map<String, String> map = new HashMap<>();
        map.put("vids", adapter.getDeleteParams());
        Factory.resp(InfoCollationActivity.this, HttpFlag.FLAG_DELETE_COLLATION, null, null).post(map);
    }

    private void changeEditMode() {
        if (adapter!=null&&adapter.getMODEL()!=0) {
            setEditModel(false);
        } else {
            setEditModel(true);
        }
    }

    private void setEditModel(boolean isEdit) {
        try {
            if (isEdit) {
                plv.setPullRefreshEnabled(false);
                plv.setScrollLoadEnabled(false);
                tv_right.setText("取消编辑");
                v_edit.setVisibility(View.VISIBLE);
                adapter.update(1);
            } else {
                tv_right.setText("编辑");
                v_edit.setVisibility(View.GONE);
                tv_select_all.setText("全选");
                adapter.update(0);
                plv.setPullRefreshEnabled(true);
                plv.setScrollLoadEnabled(true);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    @Override
    public void onBackPressed() {
        if (adapter!=null&&adapter.getMODEL()!=0) {
            setEditModel(false);
        } else {
            super.onBackPressed();
        }
    }
}
