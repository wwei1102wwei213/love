package com.yyspbfq.filmplay.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wei.wlib.widget.CircleImageView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.HomeLikeAdapter;
import com.yyspbfq.filmplay.bean.ChannelDefaultBean;
import com.yyspbfq.filmplay.bean.HomeClassifyBean;
import com.yyspbfq.filmplay.bean.HomeColumnBean;
import com.yyspbfq.filmplay.bean.SlideBean;
import com.yyspbfq.filmplay.bean.VideoShortBean;
import com.yyspbfq.filmplay.ui.activity.ChannelDetailActivity;
import com.yyspbfq.filmplay.ui.activity.ShowWebActivity;
import com.yyspbfq.filmplay.ui.activity.VideoClassifyActivity;
import com.yyspbfq.filmplay.ui.activity.VideoPlayActivity;

import java.util.List;

public class UiUtils {

    public static void handleClassifyView(Context context, LinearLayout ll, final List<HomeClassifyBean> list) {
        if (list==null||list.size()==0) return;
        try {
            ll.removeAllViews();
            LinearLayout top = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_column_home, ll, false);
            LinearLayout bottom = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_column_home, ll, false);
            for (int i=0;i<list.size()&&i<7;i++) {
                final int index = i;
                LinearLayout item;
                if (i<4) {
                    item = (LinearLayout)top.getChildAt(i);
                } else {
                    item = (LinearLayout)bottom.getChildAt(i%4);
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoClassifyActivity.actionStart(context, list.get(index).getId());
                    }
                });
                ImageView iv = (ImageView) item.getChildAt(0);
                Glide.with(context).load(list.get(i).getThumb()).crossFade().into(iv);
                TextView name = (TextView) item.getChildAt(1);
                String nameStr = list.get(i).getTname();
                name.setText(TextUtils.isEmpty(nameStr)?"":nameStr);
                item.setVisibility(View.VISIBLE);
            }
            LinearLayout all;
            if (list.size()>3) {
                all = (LinearLayout)bottom.getChildAt(list.size()-4);
            } else {
                all = (LinearLayout)top.getChildAt(list.size());
            }
            all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoClassifyActivity.actionStart(context, "0");
                }
            });
            ImageView iv = (ImageView) all.getChildAt(0);
            iv.setImageResource(R.mipmap.home_class_pic_all);
            TextView name = (TextView) all.getChildAt(1);
            name.setText("全部");
            all.setVisibility(View.VISIBLE);
            ll.addView(top);
            if (list.size()>3) {
                ll.addView(bottom);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    public static void handleHomeMenuDefault(Context context, LinearLayout ll, final List<VideoShortBean> list, int ivHeight) {
        if (list==null||list.size()==0) return;
        try {
            ll.removeAllViews();
            LinearLayout top = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_home_video_default, ll, false);
            LinearLayout middle = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_home_video_default, ll, false);
            LinearLayout bottom = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_home_video_default, ll, false);
            for (int i=0;i<list.size()&&i<6;i++) {
                final int index = i;
                LinearLayout item;
                if (i<2) {
                    item = (LinearLayout)top.getChildAt(i);
                } else if (i<4){
                    item = (LinearLayout)middle.getChildAt(i-2);
                } else {
                    item = (LinearLayout)bottom.getChildAt(i-4);
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoPlayActivity.actionStart(context, list.get(index).getId());
                    }
                });
                ImageView iv = (ImageView) item.getChildAt(0);
                setViewHeight(iv, ivHeight);
                Glide.with(context).load(list.get(i).getVideo_thump()).crossFade().into(iv);
                TextView name = (TextView) item.getChildAt(1);
                String nameStr = list.get(i).getName();
                name.setText(TextUtils.isEmpty(nameStr)?"":nameStr);
                item.setVisibility(View.VISIBLE);
            }
            ll.addView(top);
            if (list.size()>2) {
                ll.addView(middle);
            }
            if (list.size()>4) {
                ll.addView(bottom);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    public static void handleHomeMenuFour(Context context, LinearLayout ll, final List<VideoShortBean> list, int ivHeight, boolean isMove) {
        if (list==null||list.size()==0) return;
        try {
            if (isMove) {
                ll.removeAllViews();
            }
            LinearLayout top = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_home_video_default, ll, false);
            LinearLayout bottom = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_home_video_default, ll, false);
            for (int i=0;i<list.size()&&i<4;i++) {
                final int index = i;
                LinearLayout item;
                if (i<2) {
                    item = (LinearLayout)top.getChildAt(i);
                } else {
                    item = (LinearLayout)bottom.getChildAt(i-2);
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoPlayActivity.actionStart(context, list.get(index).getId());
                    }
                });
                ImageView iv = (ImageView) item.getChildAt(0);
                setViewHeight(iv, ivHeight);
                Glide.with(context).load(list.get(i).getVideo_thump()).crossFade().into(iv);
                TextView name = (TextView) item.getChildAt(1);
                String nameStr = list.get(i).getName();
                name.setText(TextUtils.isEmpty(nameStr)?"":nameStr);
                item.setVisibility(View.VISIBLE);
            }
            ll.addView(top);
            if (list.size()>2) {
                ll.addView(bottom);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    public static void handleHomeColumn(Context context, LinearLayout ll, final List<HomeColumnBean> list, int ivHeight) {
        if (list==null||list.size()==0) return;
        try {
            ll.removeAllViews();
            for (int i=0;i<list.size();i++) {
                final int index = i;
                LinearLayout item = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_column_item, ll, false);
                TextView more = (TextView) item.findViewById(R.id.tv_column_more);
                TextView title = (TextView) item.findViewById(R.id.tv_column_title);
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                title.setText(list.get(index).getName());
                final HomeColumnBean bean = list.get(index);
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getType()==1) {
                            VideoClassifyActivity.actionStart(context, bean.getId());
                        } else {
                            ChannelDetailActivity.actionStart(context, bean.getId());
                        }
                    }
                });
                handleHomeMenuFour(context, item, list.get(index).getVideos(), ivHeight, false);
                ll.addView(item);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private static void setViewHeight(View v, int h) {
        ViewGroup.LayoutParams params=v.getLayoutParams();
        params.height=h;
        v.setLayoutParams(params);
    }

    public static void handleChannelRecommend(Context context, LinearLayout ll, final List<ChannelDefaultBean> list, int ivHeight) {
        if (list==null||list.size()==0) return;
        try {
            ll.removeAllViews();
            LinearLayout top = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_channel_recommend, ll, false);
            LinearLayout bottom = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_channel_recommend, ll, false);
            for (int i=0;i<list.size()&&i<4;i++) {
                final int index = i;
                LinearLayout item;
                if (i<2) {
                    item = (LinearLayout)top.getChildAt(i);
                } else {
                    item = (LinearLayout)bottom.getChildAt(i-4);
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChannelDetailActivity.actionStart(context, list.get(index).getId());
                    }
                });
                ImageView iv = (ImageView) item.getChildAt(0);
                setViewHeight(iv, ivHeight);
                Glide.with(context).load(list.get(i).getThumb()).crossFade().into(iv);
                TextView name = (TextView) item.getChildAt(1);
                String nameStr = list.get(i).getTitle();
                name.setText(TextUtils.isEmpty(nameStr)?"":nameStr);
                item.setVisibility(View.VISIBLE);
            }
            ll.addView(top);
            if (list.size()>2) {
                ll.addView(bottom);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    public static void handleChannelHuman(Context context, LinearLayout ll, final List<ChannelDefaultBean> list) {
        if (list==null||list.size()==0) return;
        try {
            ll.removeAllViews();
            for (int i=0;i<list.size();i++) {
                final int index = i;
                LinearLayout item;
                item = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_channel_humen_item, ll, false);
                CircleImageView iv = (CircleImageView) item.findViewById(R.id.civ);
                Glide.with(context).load(list.get(i).getThumb()).placeholder(R.mipmap.default_error_img).into(iv);
                TextView name = (TextView) item.findViewById(R.id.tv_name);
                TextView remark = (TextView) item.findViewById(R.id.tv_remark);
                String nameStr = list.get(i).getTitle();
                name.setText(TextUtils.isEmpty(nameStr)?"":nameStr);
                String remarkStr = list.get(i).getRemark();
                remark.setText(TextUtils.isEmpty(remarkStr)?"没有介绍":remarkStr);
                if (i==0) item.findViewById(R.id.line).setVisibility(View.INVISIBLE);
                RecyclerView rv = item.findViewById(R.id.rv_human_item);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv.setLayoutManager(manager);
                HomeLikeAdapter adapter = new HomeLikeAdapter(context, list.get(i).getVideos(), true);
                rv.setAdapter(adapter);
                ll.addView(item);
            }

        } catch (Exception e){
            BLog.e(e);
        }
    }

    public static void handleSlide(Context context, SlideBean bean) {
        try {
            if (bean==null||bean.getOpenType()==null) return;
            if ("1".equals(bean.getOpenType())) {
                ShowWebActivity.actionStart(context, bean.getUrl());
            } else if ("2".equals(bean.getOpenType())) {

            } else if ("3".equals(bean.getOpenType())) {
                VideoPlayActivity.actionStart(context, "1");
            } else if ("4".equals(bean.getOpenType())) {

            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    public static String handlePlayNum(String watch) {
        if (TextUtils.isEmpty(watch)) return "0次播放";
        return watch+"次播放";
    }

}
