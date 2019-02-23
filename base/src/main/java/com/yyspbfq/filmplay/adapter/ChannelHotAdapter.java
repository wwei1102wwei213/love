package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wei.wlib.widget.CircleImageView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.ChannelDefaultBean;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.ArrayList;
import java.util.List;

public class ChannelHotAdapter extends BaseAdapter{

    private Context context;
    private List<ChannelDefaultBean> list;

    public ChannelHotAdapter(Context context, List<ChannelDefaultBean> list) {
        this.context = context;
        if (list == null) list = new ArrayList<>();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position).getId();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_channel_recommend_gv, parent, false);
            vh.iv = convertView.findViewById(R.id.civ);
            vh.tv = convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            String title = list.get(position).getTitle();
            vh.tv.setText(title==null?"":title);
            Glide.with(context).load(list.get(position).getThumb()).crossFade().into(vh.iv);
        } catch (Exception e){
            BLog.e(e);
        }
        return convertView;
    }

    public void update(List<ChannelDefaultBean> list) {
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        CircleImageView iv;
        TextView tv;
    }
}
