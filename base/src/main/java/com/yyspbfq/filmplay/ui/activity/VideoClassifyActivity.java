package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.wei.wlib.http.WLibHttpFlag;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.ChannelDetailAdapter;
import com.yyspbfq.filmplay.adapter.ClassifyChooseAdapter;
import com.yyspbfq.filmplay.bean.ClassifyTagData;
import com.yyspbfq.filmplay.bean.HomeClassifyBean;
import com.yyspbfq.filmplay.bean.VideoShortBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoClassifyActivity extends BaseActivity implements WLibHttpListener{

    private PullToRefreshListView plv;
    private ListView lv;
    private LinearLayoutManager manager;
    private List<VideoShortBean> mData;
    private ChannelDetailAdapter adapter;
    private View mHeaderView;
    private TextView tv_hint;
    private String tid = "0";
    private int sort = 0;
    private final int size = 12;

    private boolean noDataFlag = false;

    public static void actionStart(Context context, String classifyId) {
        Intent intent = new Intent(context, VideoClassifyActivity.class);
        intent.putExtra("Classify_id", classifyId);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, String classifyId, int sort) {
        Intent intent = new Intent(context, VideoClassifyActivity.class);
        intent.putExtra("Classify_id", classifyId);
        intent.putExtra("Sort_id", sort);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, int sort) {
        Intent intent = new Intent(context, VideoClassifyActivity.class);
        intent.putExtra("Sort_id", sort);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_classify);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("全部影片");
        tid = getIntent().getStringExtra("Classify_id");
        if (TextUtils.isEmpty(tid)) {
            tid = "0";
        }
        sort = getIntent().getIntExtra("Sort_id", 0);
        initViews();
        initData();
    }

    private void initViews() {
        tv_hint = findViewById(R.id.tv_hint);
        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(null);
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        lv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mData = new ArrayList<>();

        adapter = new ChannelDetailAdapter(this, null);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.layout_header_video_classify, lv, false);
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
                if (!noDataFlag && plv.isReadyForPullUp() && plv.hasMoreData()) {
                    scrollLoadMore();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Logger.e("onScrollLv:"+firstVisibleItem+","+visibleItemCount+","+totalItemCount);
                handleHintView(firstVisibleItem);
            }
        });

        findViewById(R.id.iv_base_right).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_base_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSearchActivity.actionStart(VideoClassifyActivity.this);
            }
        });
    }

    private ClassifyChooseAdapter tagAdapter;
    private List<HomeClassifyBean> tadDatas;
    private View v_no_data;
    private void initHeaderView() {

        v_no_data = mHeaderView.findViewById(R.id.v_no_data);

        RecyclerView rv = mHeaderView.findViewById(R.id.rv);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        tadDatas = new ArrayList<>();
        HomeClassifyBean temp = new HomeClassifyBean();
        temp.setId("0");
        temp.setTname("全部");
        tadDatas.add(temp);
        tagAdapter = new ClassifyChooseAdapter(this, tadDatas, new ClassifyChooseAdapter.ClassifyTagSelectListener() {
            @Override
            public void OnClassifyTagSelected(HomeClassifyBean bean, int position) {
                changeTag(bean, position);
            }
        });
        rv.setAdapter(tagAdapter);
        RadioGroup rg = mHeaderView.findViewById(R.id.rg);

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.rb1:
                            changeSort(0);
                            break;
                        case R.id.rb2:
                            changeSort(1);
                            break;
                        case R.id.rb3:
                            changeSort(2);
                            break;
                        case R.id.rb4:
                            changeSort(3);
                            break;
                    }
                }
            }
        };

        for (int i = 0; i < rg.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) rg.getChildAt(i);
            if (i==sort) radioButton.setChecked(true);
            radioButton.setOnCheckedChangeListener(checkedChangeListener);
        }
    }

    private void initData() {
        getAllClassify();
        getList();
    }

    private void changeTag(HomeClassifyBean bean, int position) {
        page = 0;

        tid = bean.getId();
        scrollTag(position);
        getList();
    }

    private void scrollTag(int position) {
        int width = CommonUtils.getDeviceWidth(this);
        int item = 0;
        try {
            item = manager.findViewByPosition(position).getMeasuredWidth();
        } catch (Exception e){
            BLog.e(e);
        }
        if (item==0) item = DensityUtils.dp2px(this, 60);
        manager.scrollToPositionWithOffset(position,(width-item)/2);
    }

    private void changeSort(int sort) {
        if (this.sort == sort) return;
        this.sort = sort;

        page = 0;
        getList();
    }

    private void getAllClassify() {
        Factory.resp(this, HttpFlag.FLAG_ALL_CLASSIFY, null, null).post(null);
    }

    private int page = 0;
    private void getList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("tid", tid+"");
        map.put("sort", sort+"");
        Factory.resp(this, HttpFlag.FLAG_CLASSIFY_LIST, null, ClassifyTagData.class).post(map);
    }

    private boolean isLoading = false;
    private void scrollLoadMore() {
        if (isLoading) return;
        isLoading = true;
        page++;
        getList();
    }

    private void handleHintView(int first) {
        try {
            if (first==0&&tv_hint.getVisibility()==View.VISIBLE) {
                tv_hint.setVisibility(View.GONE);
            }
            if (first>0&&tv_hint.getVisibility()!=View.VISIBLE) {
                String sortText;
                if (sort==1) {
                    sortText = "最多播放";
                } else if (sort==2) {
                    sortText = "最近更新";
                } else if (sort==3) {
                    sortText = "最多喜欢";
                } else {
                    sortText = "全部";
                }
                tv_hint.setText(tagAdapter.getItemText() + " · " + sortText);
                tv_hint.setVisibility(View.VISIBLE);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_ALL_CLASSIFY) {
            try {
                List<HomeClassifyBean> list = new Gson().fromJson(formatData.toString(), new TypeToken<List<HomeClassifyBean>>(){}.getType());
                if (list!=null&&list.size()>0) {
                    tadDatas.addAll(list);
                    tagAdapter.notifyDataSetChanged();
                    for (int i=0;i<list.size();i++) {
                        if (tid.equals(list.get(i).getId())) {
                            tagAdapter.updateSelectPosition(i+1);
                            scrollTag(i+1);
                            break;
                        }
                    }
                }
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag==HttpFlag.FLAG_CLASSIFY_LIST) {
            try {
                noDataFlag = false;
                plv.setScrollLoadEnabled(true);
                v_no_data.setVisibility(View.GONE);
                ClassifyTagData data = (ClassifyTagData) formatData;
                List<VideoShortBean> list = data.getData();
                if (list==null||list.size()==0) {
                    if (page>0) {
                        page--;
                        plv.setHasMoreData(false);
                    } else {
                        v_no_data.setVisibility(View.VISIBLE);
                        adapter.update(null);
                        noDataFlag = true;
                        plv.setScrollLoadEnabled(false);
                    }
                } else {
                    if (page==0) {
                        mData.clear();
                        plv.setHasMoreData(true);
                    }
                    mData.addAll(list);
                    adapter.update(mData);
                    if (list.size()<data.getSize()) {
                        plv.setHasMoreData(false);
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

    @Override
    public void handleAfter(int flag, Object tag) {
        try {
            isLoading = false;
        } catch (Exception e){
            BLog.e(e);
        }
    }

}
