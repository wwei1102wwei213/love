package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.RecordListAdapter;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.DBHelper;
import com.yyspbfq.filmplay.db.VideoRecordBean;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoRecordActivity extends BaseActivity implements WLibHttpListener{

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, VideoRecordActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_collation);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("播放记录");
        initViews();
        initData();
    }

    private TextView tv_right;
    private PullToRefreshListView plv;
    private ListView lv;
    private RecordListAdapter adapter;
    private List<VideoRecordBean> mData;
    private TextView tv_select_all, tv_delete;
    private View v_edit, v_no_data;
    private void initViews() {

        v_no_data = findViewById(R.id.v_no_data);

        ((TextView) findViewById(R.id.tv_hint_no_data)).setText(getString(R.string.record_list_no_data));

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
        adapter = new RecordListAdapter(this, mData, new RecordListAdapter.EditModelListener() {
            @Override
            public void modeChangeListener() {
                setEditModel(true);
            }
        });
        lv.setAdapter(adapter);
        //关闭下拉
        plv.setPullRefreshEnabled(false);
        //关闭加载更多
        plv.setScrollLoadEnabled(false);

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
        List<VideoRecordBean> list = DBHelper.getInstance().getVideoRecord(BaseApplication.getInstance(), 100);
        if (list!=null&&list.size()>0) {
            adapter.update(list);
            Log.e("VideoRecordBean","list:"+new Gson().toJson(list));
        } else {
            plv.setVisibility(View.GONE);
            v_no_data.setVisibility(View.VISIBLE);
        }
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
        if (flag == HttpFlag.FLAG_DELETE_VIDEO_RECORD) {
            try {
                adapter.deleteSelect();
                showToast("删除成功");
                DBHelper.getInstance().deleteVideoRecord(BaseApplication.getInstance(), (List<VideoRecordBean>) tag);
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
        if (flag == HttpFlag.FLAG_DELETE_VIDEO_RECORD) {
            if (!TextUtils.isEmpty(hint)) showToast(hint);
        }
    }

    @Override
    public void handleAfter(int flag, Object tag) {
        if (flag == HttpFlag.FLAG_DELETE_VIDEO_RECORD) {
            isDeleting = false;
        }
    }

    private boolean isDeleting = false;
    private void toDelete() {
        if (isDeleting) return;
        isDeleting = true;
        Map<String, String> map = new HashMap<>();
        map.put("vids", adapter.getDeleteParams());
        Factory.resp(this, HttpFlag.FLAG_DELETE_VIDEO_RECORD, adapter.getSelects(), null).post(map);
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
                plv.setPullRefreshEnabled(false);
                plv.setScrollLoadEnabled(false);
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
