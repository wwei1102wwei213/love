package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wei.wlib.http.WLibHttpFlag;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.wei.wlib.widget.FlowLayout;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.CommentListAdapter;
import com.yyspbfq.filmplay.bean.AdvertBean;
import com.yyspbfq.filmplay.bean.CommentDataBean;
import com.yyspbfq.filmplay.bean.CommentEntity;
import com.yyspbfq.filmplay.bean.DeductionCoinBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.download.DownloadTaskManager;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.biz.login.UserHelper;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.player.MyJzvdStd;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.ui.dialog.CommentDialog;
import com.yyspbfq.filmplay.ui.dialog.NormalDialog;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.Const;
import com.yyspbfq.filmplay.utils.SystemUtils;
import com.yyspbfq.filmplay.utils.UiUtils;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;
import com.yyspbfq.filmplay.utils.tools.TimeUtils;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayActivity extends BaseActivity implements MyJzvdStd.PlayerListener, WLibHttpListener{

    private JzvdStd jvd;
    private String mVid;

    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(Const.INTENT_KEY_VIDEO_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setFullScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        mVid = getIntent().getStringExtra(Const.INTENT_KEY_VIDEO_ID);
        initViews();
        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("vid", mVid);
        Factory.resp(this, HttpFlag.FLAG_VIDEO_DETAIL, null, VideoEntity.class).post(map);
        Factory.resp(this, HttpFlag.FLAG_VIDEO_DETAIL_LIKE, null, null).post(map);
        Factory.resp(this, HttpFlag.FLAG_ADVERT_VIDEO_DETAIL, null, AdvertBean.class).post(map);
        getComments();
    }

    private List<CommentEntity> mData;
    private PullToRefreshListView plv;
    private ListView lv;
    private CommentListAdapter adapter;
    private View mHead;

    private ImageView iv_collection;
    private void initViews() {
        jvd = findViewById(R.id.jvd);
        jvd.setFull(true);

        iv_collection = (ImageView) findViewById(R.id.iv_collection);

        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(new ColorDrawable(getResources().getColor(R.color.base_line_gray)));
        lv.setDividerHeight(DensityUtils.dp2px(this, 1));
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        mData = new ArrayList<>();
        adapter = new CommentListAdapter(this, mData);
        mHead = LayoutInflater.from(this).inflate(R.layout.layout_video_detail, lv, false);
        initHeadView(mHead);
        lv.addHeaderView(mHead);
        lv.setAdapter(adapter);
        //关闭下拉
        plv.setPullRefreshEnabled(false);
        //加载更多
        plv.setScrollLoadEnabled(true);
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
        findViewById(R.id.iv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toShare();
            }
        });
        findViewById(R.id.iv_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDownload();

            }
        });
        iv_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCollection();
            }
        });
        findViewById(R.id.v_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog();
            }
        });
    }

    private CommentDialog dialog;
    private void showCommentDialog() {
        if (dialog ==null) {
            dialog = new CommentDialog(this);
            dialog.setVid(mVid);
            dialog.setListener(new CommentDialog.CommentSuccessListener() {
                @Override
                public void onCommentSuccessListener() {
                    page = 0;
                    getComments(true);
                }
            });
        }
        dialog.show();
    }

    private TextView tv_comments_hot, tv_comment_all;
    private TextView tv_video_name, tv_time_and_num;
    private ImageView iv_adv, iv_zan, iv_hate;
    private FlowLayout fl_label;
    private List<VideoEntity> likeList;
    private LinearLayout ll_like;
    private TextView tv_zan_summary;
    private TextView tv_intro;
    private ProgressBar pb_zan;
    private View v_video_intro;
    private ImageView iv_intro;
    private TextView tv_sort_newest, tv_sort_hot;
    private void initHeadView(View view) {
        iv_adv = view.findViewById(R.id.iv_adv);
        tv_video_name = view.findViewById(R.id.tv_video_name);
        v_video_intro = view.findViewById(R.id.v_video_intro);
        tv_intro = view.findViewById(R.id.tv_intro);
        tv_time_and_num = view.findViewById(R.id.tv_time_and_num);
        fl_label = view.findViewById(R.id.fl_label);

        tv_comments_hot = view.findViewById(R.id.tv_comments_hot);
        tv_comment_all = view.findViewById(R.id.tv_comment_all);

        iv_intro = view.findViewById(R.id.iv_js);
        ll_like = view.findViewById(R.id.v_parent_like);
        iv_zan = view.findViewById(R.id.iv_zan);
        iv_hate = view.findViewById(R.id.iv_hate);
        tv_zan_summary = view.findViewById(R.id.tv_zan_summary);
        pb_zan = view.findViewById(R.id.pb_comment_hot);
        pb_zan.setProgress(0);

        iv_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.handleAdvert(VideoPlayActivity.this, mAdvertBean);
            }
        });
        v_video_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIntro();
            }
        });

        tv_sort_newest = view.findViewById(R.id.tv_sort_newest);
        tv_sort_hot = view.findViewById(R.id.tv_sort_hot);
        tv_sort_newest.setSelected(true);
        tv_sort_newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSort(1);
            }
        });
        tv_sort_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSort(2);
            }
        });

    }

    private void showIntro() {
        if (tv_intro.getVisibility()==View.VISIBLE) {
            tv_intro.setVisibility(View.GONE);
            iv_intro.setRotation(0);
        } else {
            String temp = "暂无介绍";
            if (mVideoData!=null&&!TextUtils.isEmpty(mVideoData.getIntro())) {
                temp = mVideoData.getIntro();
            }
            tv_intro.setText(temp);
            iv_intro.setRotation(180);
            tv_intro.setVisibility(View.VISIBLE);
        }
    }

    //标签UI
    private void setLabelView(String labels) {
        if (TextUtils.isEmpty(labels)) return;
        String[] args = labels.split(",");
        fl_label.removeAllViews();
        for (String label:args) {
            if (!TextUtils.isEmpty(label)) {
                View view = LayoutInflater.from(this).inflate(R.layout.layout_video_label_item_large, fl_label, false);
                ((TextView) view).setText(label);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoLabelActivity.actionStart(VideoPlayActivity.this, label);
                    }
                });
                fl_label.addView(view);
            }
        }
    }

    //喜欢列表 标签UI
    private void setLabelView(String labels, FlowLayout flowLayout) {
        try {
            if (TextUtils.isEmpty(labels)) return;
            String[] args = labels.split(",");
            flowLayout.removeAllViews();
            for (String label:args) {
                if (!TextUtils.isEmpty(label)) {
                    View view = LayoutInflater.from(this).inflate(R.layout.layout_video_label_item, fl_label, false);
                    ((TextView) view).setText(label);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VideoLabelActivity.actionStart(VideoPlayActivity.this, label);
                        }
                    });
                    flowLayout.addView(view);
                }
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    //猜你喜欢UI
    private void setLikeView(boolean isMore) {
        try {
            ll_like.removeAllViews();
            if (likeList==null||likeList.size()==0) return;
            if (isMore) {
                for (VideoEntity entity:likeList) {
                    addLikeItem(entity);
                }
            } else {
                if (likeList.size()>3) {
                    for (int i=0;i<3;i++) {
                        addLikeItem(likeList.get(i));
                    }
                } else {
                    for (VideoEntity entity:likeList) {
                        addLikeItem(entity);
                    }
                }
                addLikeMore();
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void addLikeItem(VideoEntity entity){
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_video_like_item, ll_like, false);
            TextView temp_title = view.findViewById(R.id.tv_title);
            TextView temp_watch = view.findViewById(R.id.tv_watch);
            ImageView iv = view.findViewById(R.id.iv);
            Glide.with(this).load(entity.getVideo_thump()).crossFade().into(iv);
            FlowLayout temp_fl = view.findViewById(R.id.fl_like_item);
            temp_title.setText(entity.getName());
            temp_watch.setText(entity.getWatch_num()+"次播放");
            setLabelView(entity.getLabel(), temp_fl);
            ll_like.addView(view);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void addLikeMore(){
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_video_like_more, ll_like, false);
            View tv = view.findViewById(R.id.tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLikeView(true);
                }
            });
            ll_like.addView(view);
        } catch (Exception e){
            BLog.e(e);
        }

    }

    private boolean isLoading = false;
    private int page = 0;
    private void loadData() {
        if (isLoading) return;
        isLoading = true;
        page = 0;
        getComments();
    }

    private void scrollLoadMore(){
        if (isLoading) return;
        isLoading = true;
        page++;
        getComments();
    }

    private int mSort = 1;//排序 1:最新 2:热门

    private void changeSort(int sort) {
        if (mSort!=sort) {
            mSort = sort;
            tv_sort_newest.setSelected(mSort==1);
            tv_sort_hot.setSelected(mSort==2);
            page = 0;
            getComments();
        }
    }

    private void getComments() {
        Map<String, String> map = new HashMap<>();
        map.put("vid", mVid);
        map.put("page", page+"");
        map.put("size", "10");
        map.put("sort", mSort+"");
        Factory.resp(this, HttpFlag.FLAG_COMMENT_LIST, false, CommentDataBean.class).post(map);
    }

    private void getComments(boolean isComment) {
        Map<String, String> map = new HashMap<>();
        map.put("vid", mVid);
        map.put("page", page+"");
        map.put("size", "10");
        map.put("sort", mSort+"");
        Factory.resp(this, HttpFlag.FLAG_COMMENT_LIST, true, CommentDataBean.class).post(map);
    }

    private void toDownload(){
        Map<String, String> map = new HashMap<>();
        map.put("vid", mVid);
        map.put("type", "2");
        Factory.resp(this, HttpFlag.FLAG_DEDUCTION_COIN, null, DeductionCoinBean.class).post(map);
    }

    private void toCollection() {
        if (canColl==0) {
            showToast("已收藏过该视频");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("vid", mVid);
            Factory.resp(this, HttpFlag.FLAG_VIDEO_COLLECTION, null, null).post(map);
        }
    }

    private VideoEntity mVideoData;
    private int canColl = 1;
    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_VIDEO_DETAIL) {
            try {
                VideoEntity bean = (VideoEntity) formatData;
                mVideoData = bean;
                tv_video_name.setText(TextUtils.isEmpty(bean.getName())?"":bean.getName());
                tv_time_and_num.setText(Html.fromHtml(TimeUtils.getVideoCreateTime(bean.getCdate())+ "  "+
                        "  <font color=\"#E3B88E\">"+bean.getWatch_num()+"</font>次播放"));
//                        "  <span style=\"color: rgb(255, 0, 0);\">"+bean.getWatch_num()+"</span>次播放"));

                setLabelView(bean.getLabel());
                jvd.setUp(bean.getVideo_url()
                        , bean.getName(), bean,  JzvdStd.SCREEN_WINDOW_NORMAL);
                Glide.with(this).
                        load(bean.getVideo_thump()).
                        into(jvd.thumbImageView);

                int p = getPercentHot(bean.getLove_num(), bean.getHate_num());
                pb_zan.setProgress(p);
                tv_zan_summary.setText(p+"%觉得很赞");
                iv_zan.setSelected(false);
                iv_hate.setSelected(false);
                if (bean.getCanLoveOrHate()==1) {
                    iv_zan.setSelected(true);
                } else if (bean.getCanLoveOrHate()==2) {
                    iv_hate.setSelected(true);
                }
                iv_zan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickZanOrHate(false);
                    }
                });
                iv_hate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickZanOrHate(true);
                    }
                });
                canColl = bean.getCanCollection();
                if (canColl==0) {
                    iv_collection.setImageResource(R.mipmap.icon_ilike_select);
                }
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_VIDEO_DETAIL_LIKE) {
            try {
                likeList = new Gson().fromJson(formatData.toString(), new TypeToken<List<VideoEntity>>(){}.getType());
                setLikeView(false);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_VIDEO_SET_LIKE) {
            try {
                if ((boolean) tag) {
                    iv_hate.setSelected(true);
                    refreshPercentHot(mVideoData.getLove_num(), mVideoData.getHate_num(), true);
                } else {
                    iv_zan.setSelected(true);
                    refreshPercentHot(mVideoData.getLove_num(), mVideoData.getHate_num(), false);
                }
                showToast("操作成功");
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_ADVERT_VIDEO_DETAIL) {
            try {
                mAdvertBean = (AdvertBean) formatData;
                if (mAdvertBean.getThumb()!=null) {
                    iv_adv.setVisibility(View.VISIBLE);
                    Glide.with(this).load(mAdvertBean.getThumb()+"").crossFade().into(iv_adv);
                } else {
                    iv_adv.setVisibility(View.GONE);
                }
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_COMMENT_LIST) {
            try {
                CommentDataBean bean = (CommentDataBean) formatData;
                List<CommentEntity> list = bean.getData();
                if (list==null||list.size()==0) {
                    if (page>0) {
                        page--;
                        plv.setHasMoreData(false);
                    }
                } else {
                    if (page==0) {
                        mData.clear();
                        tv_comments_hot.setText(bean.getCount()+"条评论");
                        tv_comment_all.setText("全部评论("+bean.getCount()+")");
                    }
                    mData.addAll(list);
                    adapter.update(mData);
                    if (page==0&&((boolean) tag)) {
                        try {
                            lv.smoothScrollToPosition(1);
                        } catch (Exception e){
                            BLog.e(e);
                        }
                    }
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
        } else if (flag == HttpFlag.FLAG_VIDEO_COLLECTION) {
            try {
                iv_collection.setImageResource(R.mipmap.icon_ilike_select);
                if (!TextUtils.isEmpty(hint)) showToast(hint);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_DEDUCTION_COIN) {
            try {
                DeductionCoinBean bean = (DeductionCoinBean) formatData;
                if (bean.getCanHandle()==1) {
                    DownloadTaskManager.getInstance().addTask(mVid, mVideoData.getDown_url(), mVid, mVideoData);
                    if (bean.getDeduction()==1) {
                        UserHelper.getInstance().getUserInfo();
                    }
                } else {
                    NormalDialog normalDialog = new NormalDialog(this);
                    normalDialog.show();
                }
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    private void toShare() {
        try {
            SystemUtils.copyTextToClip(this, SPLongUtils.getInviteCodeUrl(this));
            ToastUtils.showToast("复制成功，快去分享吧");
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private AdvertBean mAdvertBean;
    @Override
    public void handleLoading(int flag, Object tag, boolean isShow) {

    }

    @Override
    public void handleError(int flag, Object tag, int errorType, String response, String hint) {
        if (!TextUtils.isEmpty(hint)) {
            showToast(hint);
        }
        if (flag == HttpFlag.FLAG_VIDEO_DETAIL) {

        } else if (flag == HttpFlag.FLAG_VIDEO_DETAIL_LIKE) {

        } else if (flag == HttpFlag.FLAG_VIDEO_SET_LIKE) {

        } else if (flag == HttpFlag.FLAG_COMMENT_LIST) {
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
        if (flag == HttpFlag.FLAG_COMMENT_LIST) {
            isLoading = false;
        }
    }

    private void clickZanOrHate(boolean isHate) {
        if (iv_zan.isSelected()||iv_hate.isSelected()) {
            showToast("您已经"+(iv_zan.isSelected()?"赞":"踩")+"过了");
            return;
        }
        Factory.resp(this, HttpFlag.FLAG_VIDEO_SET_LIKE, isHate, null).post(getParamsMap(isHate));
    }

    private int getPercentHot(String zan, String hate) {
        try {
            int z = Integer.parseInt(zan);
            if (z==0) return 0;
            int h = Integer.parseInt(hate);
            if (h==0) return 100;
            return  (100*z)/(z+h);
        } catch (Exception e){
            BLog.e(e);
        }
        return 0;
    }

    private void refreshPercentHot(String zan, String hate, boolean isHate) {
        try {
            int z = Integer.parseInt(zan);
            int h = Integer.parseInt(hate);
            if (isHate) {
                h++;
            } else {
                z++;
            }
            int p = (100*z)/(z+h);
            pb_zan.setProgress(p);
            tv_zan_summary.setText(p+"%觉得很赞");
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private Map<String, String> getParamsMap(boolean isHate) {
        Map<String, String> map = new HashMap<>();
        map.put("vid", mVid);
        map.put("type", isHate?"2":"1");
        return map;
    }

    @Override
    public void OnPlayerListener(int flag) {
        setFullScreen();
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
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (jvd!=null) {
            jvd.setFull(false);
        }
    }
}
