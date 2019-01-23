package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.VideoShortBean;
import com.yyspbfq.filmplay.ui.activity.VideoPlayActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeLikeAdapter extends RecyclerView.Adapter<HomeLikeAdapter.ViewHolder>{

    private Context context;
    private List<VideoShortBean> list;
    private boolean titleDark = false;

    public HomeLikeAdapter(Context context, List<VideoShortBean> list) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
    }

    public HomeLikeAdapter(Context context, List<VideoShortBean> list, boolean titleDark) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        this.titleDark = titleDark;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_like_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(list.get(position).getName());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayActivity.actionStart(context, list.get(position).getId());
            }
        });
        Glide.with(context).load(list.get(position).getVideo_thump()).crossFade().into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<VideoShortBean> list) {
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
            if (titleDark) {
                tv.setTextColor(Color.parseColor("#333333"));
            }
        }

    }

}
