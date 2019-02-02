package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wei.wlib.http.WLibHttpFlag;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.wei.wlib.widget.FlowLayout;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.VideoListAdapter;
import com.yyspbfq.filmplay.bean.SearchBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoSearchActivity extends BaseActivity implements WLibHttpListener{

    private PullToRefreshListView plv;
    private ListView lv;
    private List<VideoEntity> mData;
    private VideoListAdapter adapter;
    private final String nodataFormat = "没有找到与“<font color=#AA8969>%s</font>”，相关的结果\n换个词搜搜看吧~";

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, VideoSearchActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_search);
        initStatusBar();
        initViews();
        initHistoryView();
        initData();
    }


    private EditText et;
    private TextView tv_search_btn;
    private View v_clear;
    private View ll_history, ll_hot, v_result, v_no_data;
    private TextView tv_delete, tv_hint_search, tv_hint_no_data;
    private FlowLayout fl_history, fl_hot;
    private void initViews() {

        et = findViewById(R.id.et);
        tv_search_btn = findViewById(R.id.tv_search_btn);
        v_clear = findViewById(R.id.v_clear);

        ll_history = findViewById(R.id.ll_history);
        ll_hot = findViewById(R.id.ll_hot);
        v_result = findViewById(R.id.v_result);
        v_no_data = findViewById(R.id.v_no_data);

        tv_delete = findViewById(R.id.tv_delete);
        tv_hint_search = findViewById(R.id.tv_hint_search);
        tv_hint_no_data = findViewById(R.id.tv_hint_no_data);

        fl_history = findViewById(R.id.fl_search_history);
        fl_hot = findViewById(R.id.fl_search_hot);

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
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getList();
                    return true;
                }
                return false;
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (TextUtils.isEmpty(s)) {
                        v_clear.setVisibility(View.GONE);
                    } else {
                        v_clear.setVisibility(View.VISIBLE);
                    }
                    changeSearchText();
                } catch (Exception e){
                    BLog.e(e);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        tv_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("取消".equals(tv_search_btn.getText().toString())) {
                    finish();
                } else {
                    getList();
                }
            }
        });

        v_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
    }

    private String keyword = "";
    private int page = 0;
    private void getList() {
        if (TextUtils.isEmpty(et.getText())) {
            showToast("请输入搜索内容");
            return;
        }
        keyword = et.getText().toString().trim();
        if (TextUtils.isEmpty(keyword)) {
            showToast("搜索内容不能为空");
            return;
        }
        page = 0;
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("keyword", keyword);
        Factory.resp(this, HttpFlag.FLAG_SEARCH_KEYWORD, keyword, SearchBean.class).post(map);
    }

    private void getList(String label) {
        if (TextUtils.isEmpty(label)) {
            return;
        }
        keyword = label;
        page = 0;
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("keyword", keyword);
        Factory.resp(this, HttpFlag.FLAG_SEARCH_KEYWORD, keyword, SearchBean.class).post(map);
    }

    private boolean isLoading = false;
    private void scrollLoadMore() {
        if (isLoading) return;
        if (TextUtils.isEmpty(keyword)) return;
        isLoading = true;
        page++;
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("keyword", keyword);
        Factory.resp(this, HttpFlag.FLAG_SEARCH_KEYWORD, keyword, SearchBean.class).post(map);
    }

    private void changeSearchText() {
        if (TextUtils.isEmpty(et.getText())) {
            tv_search_btn.setText("取消");
        } else {
            tv_search_btn.setText("搜索");
        }
    }

    private void initHistoryView() {
        List<String> temp = SPLongUtils.getSearchRecord(this);
        if (temp.size()>0) {
            setLabelView(temp, fl_history);
        }
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fl_history.removeAllViews();
                    SPLongUtils.clearSearchRecord(VideoSearchActivity.this);
                } catch (Exception e){
                    BLog.e(e);
                }
            }
        });
    }

    private void initData() {
        Factory.resp(this, HttpFlag.FLAG_SEARCH_HOT, null, null).post(null);
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_SEARCH_HOT) {
            try {
                List<String> data = new Gson().fromJson(formatData.toString(), new TypeToken<List<String>>(){}.getType());
                setLabelView(data, fl_hot);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag==HttpFlag.FLAG_SEARCH_KEYWORD) {
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
                        SPLongUtils.saveSearchRecord(this, tag.toString());
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
                if (page==0) {
                    hideHeyBoard();
                }
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                hideKeyboard(ev, view);//调用方法判断是否需要隐藏键盘
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setLabelView(List<String> labels, FlowLayout flowLayout) {
        flowLayout.removeAllViews();
        if (labels==null||labels.size()==0) return;
        try {
            for (String label:labels) {
                if (!TextUtils.isEmpty(label)) {
                    View view = LayoutInflater.from(this).inflate(R.layout.layout_video_label_item_large, flowLayout, false);
                    ((TextView) view).setText(label);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getList(label);
                        }
                    });
                    flowLayout.addView(view);
                }
            }
            fl_hot.setVisibility(View.VISIBLE);
        } catch (Exception e){
            BLog.e(e);
        }

    }

    public void hideKeyboard(MotionEvent event, View view) {
        try {
            if (view != null && view instanceof EditText) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth() + DensityUtils.dp2px(this, 80), bootom = top + view.getHeight();//right加30dp,包含清空图标
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) this
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideHeyBoard() {
        try {// 隐藏键盘
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    public void showSoftKeyboard(Context mContext, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
