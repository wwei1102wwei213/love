package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        int w = (CommonUtils.getDeviceWidth(context) - DensityUtils.dp2px(context, 8))/2;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_channel_recommend, parent, false);
            LinearLayout temp = (LinearLayout) convertView;
            vh.left = (LinearLayout) temp.getChildAt(0);
            vh.right = (LinearLayout) temp.getChildAt(1);
            vh.iv_left = (ImageView) vh.left.getChildAt(0);
            vh.tv_left = (TextView) vh.left.getChildAt(1);
            vh.iv_right = (ImageView) vh.right.getChildAt(0);
            vh.tv_right = (TextView) vh.right.getChildAt(1);
            setViewHeight(vh.iv_left, mHeight);
            setViewHeight(vh.iv_right, mHeight);
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
        LinearLayout left, right;
        ImageView iv_left, iv_right;
        TextView tv_left, tv_right;
    }

}
