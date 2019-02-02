package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wei.wlib.widget.FlowLayout;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.ui.activity.VideoLabelActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends BaseAdapter{

    private Context context;
    private List<VideoEntity> list;

    public VideoListAdapter(Context context, List<VideoEntity> list) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_video_list_default, parent, false);
            vh.fl = convertView.findViewById(R.id.fl_like_item);
            vh.iv = convertView.findViewById(R.id.iv);
            vh.tv_title = convertView.findViewById(R.id.tv_title);
            vh.tv_watch = convertView.findViewById(R.id.tv_watch);
            vh.space = convertView.findViewById(R.id.space);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        VideoEntity entity = list.get(position);
        vh.tv_watch.setText(UiUtils.handlePlayNum(entity.getWatch_num()));
        vh.tv_title.setText(TextUtils.isEmpty(entity.getName())?"未知影片":entity.getName());
        Glide.with(context).load(entity.getVideo_thump()).crossFade().into(vh.iv);
        setLabelView(entity.getLabel(), vh.fl);
        if (position==list.size()-1) {
            vh.space.setVisibility(View.VISIBLE);
        } else {
            vh.space.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void update(List<VideoEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private void setLabelView(String labels, FlowLayout flowLayout) {
        flowLayout.removeAllViews();
        if (TextUtils.isEmpty(labels)) return;
        try {
            String[] args = labels.split(",");
            for (String label:args) {
                if (!TextUtils.isEmpty(label)) {
                    View view = LayoutInflater.from(context).inflate(R.layout.layout_video_label_item, flowLayout, false);
                    ((TextView) view).setText(label);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VideoLabelActivity.actionStart(context, label);
                        }
                    });
                    flowLayout.addView(view);
                }
            }
        } catch (Exception e){
            BLog.e(e);
        }

    }

    class ViewHolder {
        TextView tv_title, tv_watch;
        ImageView iv;
        FlowLayout fl;
        View space;
    }
}
