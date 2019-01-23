package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.db.VideoRecordBean;
import com.yyspbfq.filmplay.ui.activity.VideoPlayActivity;

import java.util.ArrayList;
import java.util.List;

public class WatchRecordAdapter extends  RecyclerView.Adapter<WatchRecordAdapter.ViewHolder>{

    private Context context;
    private List<VideoRecordBean> list;

    public WatchRecordAdapter(Context context, List<VideoRecordBean> list) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_like_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoEntity entity = list.get(position).getVideoData();
        holder.tv.setText(entity.getName()==null?"":entity.getName());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayActivity.actionStart(context, list.get(position).getVid());
            }
        });
        Glide.with(context).load(entity.getVideo_thump()).crossFade().into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<VideoRecordBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        View v;
        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.tv);
            v = itemView;
        }

    }
}
