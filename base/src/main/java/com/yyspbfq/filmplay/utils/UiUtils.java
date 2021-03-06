package com.yyspbfq.filmplay.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wei.wlib.service.DownloadService;
import com.wei.wlib.widget.CircleImageView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.HomeLikeAdapter;
import com.yyspbfq.filmplay.bean.AdvertBean;
import com.yyspbfq.filmplay.bean.ChannelDefaultBean;
import com.yyspbfq.filmplay.bean.HomeClassifyBean;
import com.yyspbfq.filmplay.bean.HomeColumnBean;
import com.yyspbfq.filmplay.bean.InfoMessageEntity;
import com.yyspbfq.filmplay.bean.SlideBean;
import com.yyspbfq.filmplay.bean.UpdateConfig;
import com.yyspbfq.filmplay.bean.VideoShortBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.activity.ChannelDetailActivity;
import com.yyspbfq.filmplay.ui.activity.ShowWebActivity;
import com.yyspbfq.filmplay.ui.activity.VideoClassifyActivity;
import com.yyspbfq.filmplay.ui.activity.VideoPlayActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                        VideoClassifyActivity.actionStart(context, list.get(index).getId(), 2);
                    }
                });
                ImageView iv = (ImageView) item.getChildAt(0);
                if (!TextUtils.isEmpty(list.get(i).getThumb())) {
                    Glide.with(context).load(list.get(i).getThumb()).placeholder(R.mipmap.base_icon_default).crossFade().into(iv);
                } else {
                    Glide.with(context).load(R.mipmap.base_icon_default).placeholder(R.mipmap.base_icon_default).into(iv);
                }
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
            name.setText("全部栏目");
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
                RelativeLayout rl = (RelativeLayout) item.getChildAt(0);
                setViewHeight(rl, ivHeight);
                ImageView iv = (ImageView) rl.getChildAt(0);
                Glide.with(context).load(list.get(i).getVideo_thump()).crossFade().into(iv);
                TextView quality = (TextView) rl.getChildAt(1);
                String qualityStr = list.get(i).getQuality();
                if (!TextUtils.isEmpty(qualityStr)) {
                    quality.setText(qualityStr);
                    quality.setVisibility(View.VISIBLE);
                }
                TextView name = (TextView) item.getChildAt(1);
                String nameStr = list.get(i).getName();
                name.setText(TextUtils.isEmpty(nameStr)?"":nameStr);
                TextView subHead = (TextView) item.getChildAt(2);
                String subStr = list.get(i).getSubheading();
                if (!TextUtils.isEmpty(subStr)) {
                    subHead.setText(subStr);
                    subHead.setVisibility(View.VISIBLE);
                }
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
                RelativeLayout rl = (RelativeLayout) item.getChildAt(0);
                setViewHeight(rl, ivHeight);
                ImageView iv = (ImageView) rl.getChildAt(0);
                Glide.with(context).load(list.get(i).getVideo_thump()).crossFade().into(iv);
                TextView quality = (TextView) rl.getChildAt(1);
                String qualityStr = list.get(i).getQuality();
                if (!TextUtils.isEmpty(qualityStr)) {
                    quality.setText(qualityStr);
                    quality.setVisibility(View.VISIBLE);
                }
                TextView name = (TextView) item.getChildAt(1);
                String nameStr = list.get(i).getName();
                name.setText(TextUtils.isEmpty(nameStr)?"":nameStr);
                TextView subHead = (TextView) item.getChildAt(2);
                String subStr = list.get(i).getSubheading();
                if (!TextUtils.isEmpty(subStr)) {
                    subHead.setText(subStr);
                    subHead.setVisibility(View.VISIBLE);
                }
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
                            VideoClassifyActivity.actionStart(context, bean.getId(), 2);
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
                    item = (LinearLayout)bottom.getChildAt(i-2);
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChannelDetailActivity.actionStart(context, list.get(index).getId());
                    }
                });
                ImageView iv = (ImageView) item.getChildAt(0);
                setViewHeight(iv, ivHeight);
                Glide.with(context).load(list.get(i).getPic()).crossFade().into(iv);
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
                Glide.with(context).load(list.get(i).getThumb()).crossFade().into(iv);
                item.findViewById(R.id.v_human_top).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChannelDetailActivity.actionStart(context, list.get(index).getId());
                    }
                });
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
                HomeLikeAdapter adapter = new HomeLikeAdapter(context, list.get(i).getVideos());
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
                if (!TextUtils.isEmpty(bean.getUrl())) {
                    ShowWebActivity.actionStart(context, bean.getUrl());
                }
            } else if ("2".equals(bean.getOpenType())) {
                if (!TextUtils.isEmpty(bean.getUrl())&&bean.getUrl().startsWith("http")) {
                    try {
                        String url = bean.getUrl();
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            String apkName = url.substring(url.lastIndexOf("/"));
                            Intent downloadIntent = new Intent(context, DownloadService.class);
                            downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_NAME, apkName);
                            downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_URL, url);
                            context.startService(downloadIntent);
                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.not_find_sdcard), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e){
                        BLog.e(e);
                    }
                }
            } else if ("3".equals(bean.getOpenType())) {
                if (!TextUtils.isEmpty(bean.getVid())) {
                    VideoPlayActivity.actionStart(context, bean.getVid());
                }
            } else if ("4".equals(bean.getOpenType())) {

            }
            if (TextUtils.isEmpty(bean.getId())) return;
            Map<String, String> map = new HashMap<>();
            map.put("id", bean.getId());
            Factory.resp(null, HttpFlag.FLAG_CLICK_SLIDE, null, null).post(map);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    public static void handleAdvert(Context context, AdvertBean advertBean) {
        if (advertBean==null||TextUtils.isEmpty(advertBean.getUrl())) return;
        String url = advertBean.getUrl();
        if (TextUtils.isEmpty(url)||!url.startsWith("http")) return;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", advertBean.getId());
            if (advertBean.getType()==2) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    BLog.s("downloadApk");
                    String apkName = url.substring(url.lastIndexOf("/"));
                    Intent downloadIntent = new Intent(context, DownloadService.class);
                    downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_NAME, apkName);
                    downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_URL, url);
                    context.startService(downloadIntent);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.not_find_sdcard), Toast.LENGTH_SHORT).show();
                }
            } else {
                ShowWebActivity.actionStart(context, url);
            }
            Factory.resp(null, HttpFlag.FLAG_INVITE_CLICK_ADS, null, null).post(map);
        } catch (Exception e){
            BLog.e(e);
        }
    }
    /*//操作类型，1:打开url，2:应用下载，3:打开视频 4:活动页面*/
    public static void handleInfoMessage(Context context, InfoMessageEntity entity) {
        if (entity==null||TextUtils.isEmpty(entity.getOpenType())) return;
        if ("1".equals(entity.getOpenType())) {
            if (!TextUtils.isEmpty(entity.getUrl())) {
                ShowWebActivity.actionStart(context, entity.getUrl());
            }
        } else if ("2".equals(entity.getOpenType())) {
            if (!TextUtils.isEmpty(entity.getUrl())&&entity.getUrl().startsWith("http")) {
                try {
                    String url = entity.getUrl();
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        String apkName = url.substring(url.lastIndexOf("/"));
                        Intent downloadIntent = new Intent(context, DownloadService.class);
                        downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_NAME, apkName);
                        downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_URL, url);
                        context.startService(downloadIntent);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.not_find_sdcard), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    BLog.e(e);
                }
            }
        } else if ("3".equals(entity.getOpenType())) {
            if (!TextUtils.isEmpty(entity.getVid())) {
                VideoPlayActivity.actionStart(context, entity.getVid());
            }
        } else if ("4".equals(entity.getOpenType())) {

        }
    }

    public static boolean handleMessageRedPoint(String newTime, String lastTime){
        try {
            long n = Long.parseLong(newTime);
            long l = Long.parseLong(lastTime);
            if (n>l) return true;
        } catch (Exception e){

        }
        return false;
    }

    public static String handlePlayNum(String watch) {
        if (TextUtils.isEmpty(watch)) return "0次播放";
        return watch+"次播放";
    }

    public static boolean checkUpdate(UpdateConfig config) {
        if (config==null) return false;
        try {
            int now = Integer.parseInt(CommonUtils.getVersionCodeStr());
            int sever = Integer.parseInt(config.versionCode);
            if (sever>now) {
                return true;
            }
        } catch (Exception e){

        }
        return false;
    }
}
