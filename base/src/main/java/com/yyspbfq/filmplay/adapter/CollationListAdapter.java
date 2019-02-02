package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.ui.activity.VideoPlayActivity;

import java.util.ArrayList;
import java.util.List;

public class CollationListAdapter extends BaseAdapter{

    private Context context;
    private List<VideoEntity> list;
    private EditModelListener listener;
    private int MODEL = 0;

    public interface EditModelListener {
        void modeChangeListener();
    }

    public CollationListAdapter(Context context, List<VideoEntity> list, EditModelListener listener) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        selects = new ArrayList<>();
        this.listener = listener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_collation_lv, parent, false);
            vh.time = convertView.findViewById(R.id.tv_video_time);
            vh.title = convertView.findViewById(R.id.tv_name);
            vh.v = convertView;
            vh.check = convertView.findViewById(R.id.v_check);
            vh.iv = convertView.findViewById(R.id.iv);
            vh.space = convertView.findViewById(R.id.v_space);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final VideoEntity entity = list.get(position);
        Glide.with(context).load(entity.getVideo_thump()).crossFade().into(vh.iv);
        vh.time.setText(entity.getVideo_time()==null?"":entity.getVideo_time());
        vh.title.setText(entity.getName()==null?"":entity.getName());
        vh.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODEL==0) {
                    VideoPlayActivity.actionStart(context, entity.getId());
                } else {
                    if (selects.contains(entity)) {
                        selects.remove(entity);
                    } else {
                        selects.add(entity);
                    }
                    notifyDataSetChanged();
                }
            }
        });
        vh.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (MODEL==0) {
                    if (listener!=null) listener.modeChangeListener();
                }
                return true;
            }
        });
        if (MODEL!=0) {
            vh.check.setVisibility(View.VISIBLE);
            if (selects.contains(entity)) {
                vh.check.setSelected(true);
            } else {
                vh.check.setSelected(false);
            }
        } else {
            vh.check.setVisibility(View.GONE);
        }
        if (position==0){
            vh.space.setVisibility(View.VISIBLE);
        } else {
            vh.space.setVisibility(View.GONE);
        }
        return convertView;
    }

    private List<VideoEntity> selects;
    public void update(int model) {
        this.MODEL = model;
        selects = new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<VideoEntity> getSelects() {
        return selects;
    }

    public int getMODEL() {
        return MODEL;
    }

    public String getDeleteParams() {
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<selects.size();i++) {
            if (i!=0) sb.append(",");
            sb.append(selects.get(i).getId());
        }
        return sb.toString();
    }

    public void selectAll() {
        if (selects.size()==list.size()) {
            selects = new ArrayList<>();
        } else {
            selects = new ArrayList<>();
            selects.addAll(list);
        }
        notifyDataSetChanged();
    }

    public boolean isAll() {
        return selects.size()==list.size();
    }

    public void deleteSelect() {
        this.list.removeAll(selects);
        notifyDataSetChanged();
    }

    public void update(List<VideoEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView iv;
        TextView time, title;
        View v, check, space;
    }
}
