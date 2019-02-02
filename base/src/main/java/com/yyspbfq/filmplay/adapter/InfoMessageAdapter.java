package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.InfoMessageEntity;
import com.yyspbfq.filmplay.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class InfoMessageAdapter extends BaseAdapter{

    private Context context;
    private List<InfoMessageEntity> list;

    public InfoMessageAdapter(Context context, List<InfoMessageEntity> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_info_message_lv, parent, false);
            vh.tv = convertView.findViewById(R.id.tv_title);
            vh.v = convertView;
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final InfoMessageEntity entity = list.get(position);
        vh.tv.setText(entity.getTitle()==null?"":entity.getTitle());
        vh.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.handleInfoMessage(context, entity);
            }
        });

        return convertView;
    }

    public void update(List<InfoMessageEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tv;
        View v;
    }
}
