package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.DownloadListAdapter;
import com.yyspbfq.filmplay.bean.MessageEvent;
import com.yyspbfq.filmplay.biz.download.DownloadTask;
import com.yyspbfq.filmplay.biz.download.DownloadTaskManager;
import com.yyspbfq.filmplay.db.DBHelper;
import com.yyspbfq.filmplay.db.VideoDownloadBean;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DownloadListActivity extends BaseActivity{

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, DownloadListActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("我的缓存");

        initViews();
        initData();
        EventBus.getDefault().register(this);
    }

    private TextView tv_right;
    private PullToRefreshListView plv;
    private ListView lv;
    private DownloadListAdapter adapter;
    private List<VideoDownloadBean> mData;
    private TextView tv_select_all, tv_delete;
    private View v_edit;
    private TextView tv_download_num;
    private void initViews() {

        tv_right = findViewById(R.id.tv_base_right);
        tv_right.setText("编辑");
        tv_right.setVisibility(View.VISIBLE);

        tv_delete = findViewById(R.id.tv_delete);
        tv_select_all = findViewById(R.id.tv_select_all);
        v_edit = findViewById(R.id.v_edit);

        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(new ColorDrawable(Color.TRANSPARENT));
        lv.setDividerHeight(DensityUtils.dp2px(this, 10));
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        View headView = LayoutInflater.from(this).inflate(R.layout.head_download_list_lv, lv, false);
        tv_download_num = headView.findViewById(R.id.tv_download_num);
        lv.addHeaderView(headView);
        mData = new ArrayList<>();
        adapter = new DownloadListAdapter(this, null, new DownloadListAdapter.EditModelListener() {
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
        tv_download_num.setText("同时缓存个数 "+ DownloadTaskManager.getInstance().getCurrentCount());
    }

    private void initData() {
        try {
            List<VideoDownloadBean> list = DBHelper.getInstance().getDownloadRecord(BaseApplication.getInstance());
            if (list!=null&&list.size()>0) {
                Collections.sort(list, new Comparator<VideoDownloadBean>() {
                    @Override
                    public int compare(VideoDownloadBean v1, VideoDownloadBean v2) {
                        if (v1.getState()!=v2.getState()) {
                            return v1.getState() - v2.getState();
                        } else {
                            if (v1.getState()==99) {
                                if (v1.getFinish_time()>v2.getFinish_time()) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            } else {
                                if (v1.getCreate_time()>v2.getCreate_time()) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        }
                    }
                });
            }
            if (tv_download_num!=null)
                tv_download_num.setText("同时缓存个数 "+ DownloadTaskManager.getInstance().getCurrentCount());
            adapter.update(list);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private boolean isDeleting = false;
    private void toDelete() {
        if (isDeleting) return;
        isDeleting = true;
        DownloadTaskManager.getInstance().cancelTask(adapter.getSelects());
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
                tv_right.setText("取消编辑");
                v_edit.setVisibility(View.VISIBLE);
                adapter.update(1);
            } else {
                tv_right.setText("编辑");
                v_edit.setVisibility(View.GONE);
                tv_select_all.setText("全选");
                adapter.update(0);
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

    private long longSpace = 0;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage() == MessageEvent.MSG_DOWNLOAD_VIDEO) {
            try {
                Logger.e("handleEvent:"+messageEvent.getFlag());
                if (messageEvent.getFlag() == DownloadTask.DOWNLOAD_HANDLE_COMPLETED ||
                        messageEvent.getFlag() == DownloadTask.DOWNLOAD_HANDLE_CANCEL) {
                    initData();
                } else if (messageEvent.getFlag() == DownloadTask.DOWNLOAD_HANDLE_CHANGE_COUNT) {
                    if (tv_download_num!=null)
                        tv_download_num.setText("同时缓存个数 "+ DownloadTaskManager.getInstance().getCurrentCount());
                    if (adapter!=null) adapter.notifyDataSetChanged();
                } else {
                    if (messageEvent.getFlag() == DownloadTask.DOWNLOAD_HANDLE_PAUSE) {
                        if (adapter!=null) adapter.notifyDataSetChanged();
                    } else {
                        if (System.currentTimeMillis() - longSpace > 100) {
                            if (adapter!=null) adapter.notifyDataSetChanged();
                        }
                    }
                }

            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }
}
