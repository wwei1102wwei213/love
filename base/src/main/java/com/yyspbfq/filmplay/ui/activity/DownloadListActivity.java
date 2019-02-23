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
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;
import com.yyspbfq.filmplay.utils.tools.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.jzvd.Jzvd;

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
    private View v_edit, headView, v_no_data, v_head;
    private TextView tv_download_num;
    private TextView tv_sdcard_detail;
    private void initViews() {

        tv_right = findViewById(R.id.tv_base_right);
        tv_right.setText("编辑");
        tv_right.setVisibility(View.VISIBLE);

        tv_delete = findViewById(R.id.tv_delete);
        tv_select_all = findViewById(R.id.tv_select_all);
        v_edit = findViewById(R.id.v_edit);
        v_no_data = findViewById(R.id.v_no_data);
        tv_sdcard_detail = findViewById(R.id.tv_sdcard_detail);

        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(new ColorDrawable(Color.TRANSPARENT));
        lv.setDividerHeight(DensityUtils.dp2px(this, 10));
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        headView = LayoutInflater.from(this).inflate(R.layout.head_download_list_lv, lv, false);

        tv_download_num = headView.findViewById(R.id.tv_download_num);

        v_head = headView.findViewById(R.id.v_head);
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
        tv_download_num.setText("同时缓存个数 "+ SPLongUtils.getInt(this, "film_download_count", 3));
        //CommonUtils.getFreeSpace();
        tv_download_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DownloadCountActivity.class);
            }
        });
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
                            if (v1.getCreate_time()<v2.getCreate_time()) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    }
                });
                boolean all = true;
                for (VideoDownloadBean bean : list) {
                    if (bean.getState()!=99) {
                        all = false;
                        break;
                    }
                }
                if (!all) {
                    /*if (tv_download_num!=null) {
                        tv_download_num.setText("同时缓存个数 "+ DownloadTaskManager.getInstance().getCurrentCount());
                    }*/
                    v_head.setVisibility(View.VISIBLE);
                } else {
                    v_head.setVisibility(View.GONE);
                }
                adapter.update(list);
                if (plv.getVisibility()!=View.VISIBLE) {
                    plv.setVisibility(View.VISIBLE);
                }
                if (v_no_data.getVisibility()!=View.GONE) {
                    v_no_data.setVisibility(View.GONE);
                }
                tv_sdcard_detail.setVisibility(adapter.getMODEL()==0?View.VISIBLE:View.GONE);
                if (tv_sdcard_detail.getVisibility()==View.VISIBLE) {
                    setSdcardDetail();
                }
            } else {
                plv.setVisibility(View.GONE);
                v_no_data.setVisibility(View.VISIBLE);
                tv_right.setText("编辑");
                v_edit.setVisibility(View.GONE);
                adapter.setMODEL(0);
                tv_sdcard_detail.setVisibility(View.GONE);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void toDelete() {
        DownloadTaskManager.getInstance().cancelTask(adapter.getSelects());
    }

    private void changeEditMode() {
        if (adapter==null||adapter.getCount()==0) {
            showToast("没有可编辑内容");
            return;
        }
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
                tv_sdcard_detail.setVisibility(View.GONE);
            } else {
                tv_right.setText("编辑");
                v_edit.setVisibility(View.GONE);
                tv_select_all.setText("全选");
                adapter.update(0);
                tv_sdcard_detail.setVisibility(View.VISIBLE);
                setSdcardDetail();
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void setSdcardDetail() {
        try {
            tv_sdcard_detail.setText("已占用 "+ FileUtils.getVideoFileSize()+"，可用空间 "+ CommonUtils.getFreeSpace());
        } catch (Exception e){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
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
//                Logger.e("handleEvent:"+messageEvent.getFlag());
                if (messageEvent.getFlag() == DownloadTask.DOWNLOAD_HANDLE_COMPLETED ||
                        messageEvent.getFlag() == DownloadTask.DOWNLOAD_HANDLE_CANCEL) {
                    initData();
                } else if (messageEvent.getFlag() == DownloadTask.DOWNLOAD_HANDLE_CHANGE_COUNT ||
                        messageEvent.getFlag() == DownloadTask.DOWNLOAD_HANDLE_RESTART ||
                        messageEvent.getFlag() == DownloadTask.DOWNLOAD_HANDLE_PAUSE) {
                    if (adapter!=null) adapter.notifyDataSetChanged();
                } else if (messageEvent.getFlag() == DownloadTask.DOWNLOAD_HANDLE_PROGRESS){
                    if (System.currentTimeMillis() - longSpace > 100) {
                        longSpace = System.currentTimeMillis();
                        if (adapter!=null) adapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (tv_download_num!=null)
                        tv_download_num.setText("同时缓存个数 "+ SPLongUtils.getInt(this, "film_download_count", 3));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }
}
