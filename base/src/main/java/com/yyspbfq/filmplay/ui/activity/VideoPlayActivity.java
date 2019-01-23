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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullToRefreshListView;
import com.wei.wlib.widget.FlowLayout;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.player.MyJzvdStd;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.Const;
import com.yyspbfq.filmplay.utils.tools.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayActivity extends BaseActivity implements MyJzvdStd.PlayerListener, WLibHttpListener{

    private MyJzvdStd jvd;
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
    }

    private PullToRefreshListView plv;
    private ListView lv;
    private ArrayAdapter<String> adapter;
    private View mHead;
    private void initViews() {
        jvd = findViewById(R.id.jvd);
        jvd.setFull(true);

        plv = (PullToRefreshListView) findViewById(R.id.plv);
        lv = plv.getRefreshableView();
        lv.setDivider(null);
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setVerticalScrollBarEnabled(false);
        List<String> list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        mHead = LayoutInflater.from(this).inflate(R.layout.layout_video_detail, lv, false);
        initHeadView(mHead);
        lv.addHeaderView(mHead);
        lv.setAdapter(adapter);
        //关闭下拉
        plv.setPullRefreshEnabled(false);
        //关闭加载更多
        plv.setScrollLoadEnabled(false);

    }

    private TextView tv_video_name, tv_video_intro, tv_time_and_num;
    private ImageView iv_adv, iv_zan, iv_hate;
    private FlowLayout fl_label;
    private List<VideoEntity> likeList;
    private LinearLayout ll_like;
    private TextView tv_zan_summary;
    private ProgressBar pb_zan;
    private void initHeadView(View view) {
        iv_adv = view.findViewById(R.id.iv_adv);
        tv_video_name = view.findViewById(R.id.tv_video_name);
        tv_video_intro = view.findViewById(R.id.tv_video_intro);
        tv_time_and_num = view.findViewById(R.id.tv_time_and_num);
        fl_label = view.findViewById(R.id.fl_label);
        iv_adv.setImageResource(R.mipmap.default_error_img);
        ll_like = view.findViewById(R.id.v_parent_like);
        iv_zan = view.findViewById(R.id.iv_zan);
        iv_hate = view.findViewById(R.id.iv_hate);
        tv_zan_summary = view.findViewById(R.id.tv_zan_summary);
        pb_zan = view.findViewById(R.id.pb_comment_hot);
        pb_zan.setProgress(0);
    }

    //标签UI
    private void setLabelView(String labels) {
        if (TextUtils.isEmpty(labels)) return;
        labels = "test,哈哈哈,呵呵呵,啊啊啊,oooooooooooooo,大大大阿双方高度是否";
        String[] args = labels.split(",");
        fl_label.removeAllViews();
        for (String label:args) {
            if (!TextUtils.isEmpty(label)) {
                View view = LayoutInflater.from(this).inflate(R.layout.layout_video_label_item, fl_label, false);
                ((TextView) view).setText(label);
                fl_label.addView(view);
            }
        }
    }

    //喜欢列表 标签UI
    private void setLabelView(String labels, FlowLayout flowLayout) {
        if (TextUtils.isEmpty(labels)) return;
        labels = "test,哈哈哈,呵呵呵,啊啊啊,oooooooooooooo,大大大阿双方高度是否";
        String[] args = labels.split(",");
        flowLayout.removeAllViews();
        for (String label:args) {
            if (!TextUtils.isEmpty(label)) {
                View view = LayoutInflater.from(this).inflate(R.layout.layout_video_label_item, fl_label, false);
                ((TextView) view).setText(label);
                flowLayout.addView(view);
            }
        }
    }

    //猜你喜欢UI
    private void setLikeView(boolean isMore) {
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
        }
        adapter.notifyDataSetChanged();
    }

    private void addLikeItem(VideoEntity entity){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_video_like_item, ll_like, false);
        TextView temp_title = view.findViewById(R.id.tv_title);
        TextView temp_watch = view.findViewById(R.id.tv_watch);
        FlowLayout temp_fl = view.findViewById(R.id.fl_like_item);
        temp_title.setText(entity.getName());
        temp_watch.setText(entity.getWatch_num()+"次播放");
        setLabelView(entity.getLabel(), temp_fl);
        ll_like.addView(view);
    }

    private VideoEntity mVideoData;
    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag==HttpFlag.FLAG_VIDEO_DETAIL) {
            try {
                VideoEntity bean = (VideoEntity) formatData;
                mVideoData = bean;
                tv_video_name.setText(TextUtils.isEmpty(bean.getName())?"":bean.getName());
                tv_time_and_num.setText(Html.fromHtml(TimeUtils.getVideoCreateTime(bean.getCdate())+ "  "+
//                        "  <font color=\"#23B382\">"+bean.getWatch_num()+"</font>次播放"));
                        "  <span style=\"color: rgb(255, 0, 0);\">"+bean.getWatch_num()+"</span>次播放"));
                /*<span style="color: rgb(255, 0, 0);">sadfasdfasdf</span><br/>*/
                setLabelView(bean.getLabel());
                VideoEntity test = new VideoEntity();
                test.setId("1");
                test.setVideo_url("https://v1.bugogames.com/20181228/DzsBvBMv/index.m3u8");
                test.setName(bean.getName());
                jvd.setUp(test.getVideo_url()
                        , bean.getName(), test,  JzvdStd.SCREEN_WINDOW_NORMAL);
                Glide.with(this).
                        load("http://base.jzvd-pic.nathen.cn/base.jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").
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
        }
    }

    @Override
    public void handleLoading(int flag, Object tag, boolean isShow) {

    }

    @Override
    public void handleError(int flag, Object tag, int errorType, String response, String hint) {
        if (!TextUtils.isEmpty(hint)) {
            showToast(hint);
        }
        if (flag==HttpFlag.FLAG_VIDEO_DETAIL) {

        } else if (flag== HttpFlag.FLAG_VIDEO_DETAIL_LIKE) {

        } else if (flag== HttpFlag.FLAG_VIDEO_SET_LIKE) {

        }
    }

    @Override
    public void handleAfter(int flag, Object tag) {

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
