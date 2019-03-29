package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.VideoShortBean;
import com.yyspbfq.filmplay.ui.activity.VideoPlayActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class ChannelDetailAdapter extends BaseAdapter{

    private Context context;
    private List<VideoShortBean> list;
    private int mHeight;

    public ChannelDetailAdapter (Context context, List<VideoShortBean> list) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        int w = (CommonUtils.getDeviceWidth(context) - DensityUtils.dp2px(context, 10))/2;
        mHeight = w*9/16;
    }

    @Override
    public int getCount() {
        if (list==null||list.size()==0) return 0;
        if (list.size()%2==0){
            return list.size()/2;
        } else {
            return list.size()/2 + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView==null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_channel_recommend_lv, parent, false);
            LinearLayout temp = (LinearLayout) convertView;
            vh.left = (LinearLayout) temp.getChildAt(0);
            vh.right = (LinearLayout) temp.getChildAt(1);
            vh.rl_left = (RelativeLayout) vh.left.getChildAt(0);
            vh.iv_left = (ImageView) vh.rl_left.getChildAt(0);
            vh.tv_left_dpi = (TextView) vh.rl_left.getChildAt(1);
            vh.tv_left = (TextView) vh.left.getChildAt(1);
            vh.rl_right = (RelativeLayout) vh.right.getChildAt(0);
            vh.iv_right = (ImageView) vh.rl_right.getChildAt(0);
            vh.tv_right_dpi = (TextView) vh.rl_right.getChildAt(1);
            vh.tv_right = (TextView) vh.right.getChildAt(1);
            vh.tv_left_sub = (TextView) vh.left.getChildAt(2);
            vh.tv_right_sub = (TextView) vh.right.getChildAt(2);
            setViewHeight(vh.rl_left, mHeight);
            setViewHeight(vh.rl_right, mHeight);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            final VideoShortBean leftData = list.get(position*2);
            Glide.with(context).load(leftData.getVideo_thump()).crossFade().into(vh.iv_left);
            vh.tv_left.setText(leftData.getName()==null?"未知影片":leftData.getName());
            vh.left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoPlayActivity.actionStart(context, leftData.getId());
                }
            });
            if (TextUtils.isEmpty(leftData.getQuality())) {
                vh.tv_left_dpi.setVisibility(View.GONE);
            } else {
                vh.tv_left_dpi.setText(leftData.getQuality());
                vh.tv_left_dpi.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(leftData.getSubheading())) {
                vh.tv_left_sub.setVisibility(View.GONE);
            } else {
                vh.tv_left_sub.setText(leftData.getSubheading());
                vh.tv_left_sub.setVisibility(View.VISIBLE);
            }
            vh.left.setVisibility(View.VISIBLE);
            if (list.size()<((position+1)*2)) {
                vh.right.setVisibility(View.INVISIBLE);
            } else {
                final VideoShortBean rightData = list.get(position*2+1);
                Glide.with(context).load(rightData.getVideo_thump()).crossFade().into(vh.iv_right);
                vh.tv_right.setText(rightData.getName()==null?"未知影片":rightData.getName());
                vh.right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoPlayActivity.actionStart(context, rightData.getId());
                    }
                });
                if (TextUtils.isEmpty(rightData.getQuality())) {
                    vh.tv_right_dpi.setVisibility(View.GONE);
                } else {
                    vh.tv_right_dpi.setText(rightData.getQuality());
                    vh.tv_right_dpi.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(rightData.getSubheading())) {
                    vh.tv_right_sub.setVisibility(View.GONE);
                } else {
                    vh.tv_right_sub.setText(rightData.getSubheading());
                    vh.tv_right_sub.setVisibility(View.VISIBLE);
                }
                vh.right.setVisibility(View.VISIBLE);
            }

        } catch (Exception e){
            BLog.e(e);
        }
        return convertView;
    }

    public void update(List<VideoShortBean> list) {
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    private void setViewHeight(View v, int h) {
        ViewGroup.LayoutParams params=v.getLayoutParams();
        params.height=h;
        v.setLayoutParams(params);
    }

    class ViewHolder {
        RelativeLayout rl_left, rl_right;
        LinearLayout left, right;
        ImageView iv_left, iv_right;
        TextView tv_left, tv_right;
        TextView tv_left_dpi, tv_right_dpi;
        TextView tv_left_sub, tv_right_sub;
    }

}
