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
import com.yyspbfq.filmplay.db.VideoRecordBean;
import com.yyspbfq.filmplay.ui.activity.VideoPlayActivity;

import java.util.ArrayList;
import java.util.List;

public class RecordListAdapter extends BaseAdapter{


    private Context context;
    private List<VideoRecordBean> list;
    private EditModelListener listener;
    private int MODEL = 0;

    public interface EditModelListener {
        void modeChangeListener();
    }

    public RecordListAdapter(Context context, List<VideoRecordBean> list, EditModelListener listener) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_record_list_lv, parent, false);
            vh.time = convertView.findViewById(R.id.tv_video_time);
            vh.title = convertView.findViewById(R.id.tv_name);
            vh.v = convertView;
            vh.check = convertView.findViewById(R.id.v_check);
            vh.iv = convertView.findViewById(R.id.iv);
            vh.space = convertView.findViewById(R.id.v_space);
            vh.progress = convertView.findViewById(R.id.tv_watch_time);
            vh.tv_record_time = convertView.findViewById(R.id.tv_record_time);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final VideoRecordBean bean = list.get(position);
        final VideoEntity entity = list.get(position).getVideoData();
        Glide.with(context).load(entity.getVideo_thump()).crossFade().into(vh.iv);
        vh.time.setText(entity.getVideo_time()==null?"":entity.getVideo_time());
        vh.title.setText(entity.getName()==null?"":entity.getName());
        vh.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODEL==0) {
                    VideoPlayActivity.actionStart(context, bean.getVid());
                } else {
                    if (selects.contains(bean)) {
                        selects.remove(bean);
                    } else {
                        selects.add(bean);
                    }
                    notifyDataSetChanged();
                }
            }
        });
        int pr = 0;
        if (entity.getVideoSize()!=0) {
            try {
                long t = (bean.getLast_progress()*100)/entity.getVideoSize();
                t = t>100?100:t;
                pr = Integer.parseInt(t+"");
            } catch (Exception e){

            }
        }
        vh.progress.setText("观看至"+pr+"%");
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
            if (selects.contains(bean)) {
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
        if (position==TIME_LINE_1) {
            vh.tv_record_time.setVisibility(View.VISIBLE);
            vh.tv_record_time.setText("今天");
        } else if (position==TIME_LINE_2) {
            vh.tv_record_time.setVisibility(View.VISIBLE);
            vh.tv_record_time.setText("七天内");
        } else if (position==TIME_LINE_3) {
            vh.tv_record_time.setVisibility(View.VISIBLE);
            vh.tv_record_time.setText("更早");
        } else {
            vh.tv_record_time.setVisibility(View.GONE);
        }
        return convertView;
    }

    private List<VideoRecordBean> selects;
    public void update(int model) {
        this.MODEL = model;
        selects = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setMODEL(int MODEL) {
        this.MODEL = MODEL;
    }

    public List<VideoRecordBean> getSelects() {
        return selects;
    }

    public int getMODEL() {
        return MODEL;
    }

    public String getDeleteParams() {
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<selects.size();i++) {
            if (i!=0) sb.append(",");
            sb.append(selects.get(i).getVid());
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
        TIME_LINE_1 = -1;
        TIME_LINE_2 = -1;
        TIME_LINE_3 = -1;
        if (list.size()>0) {
            for (int i=0;i<this.list.size();i++) {
                int type = list.get(i).getTodayType();
                if (type==1&&TIME_LINE_1==-1) TIME_LINE_1 = i;
                if (type==2&&TIME_LINE_2==-1) TIME_LINE_2 = i;
                if (type==3&&TIME_LINE_3==-1) TIME_LINE_3 = i;
            }
        }
        notifyDataSetChanged();
    }

    private int TIME_LINE_1 = -1;
    private int TIME_LINE_2 = -1;
    private int TIME_LINE_3 = -1;
    public void update(List<VideoRecordBean> list) {
        if (list==null) list = new ArrayList<>();
        this.list = list;
        TIME_LINE_1 = -1;
        TIME_LINE_2 = -1;
        TIME_LINE_3 = -1;
        for (int i=0;i<this.list.size();i++) {
            int type = list.get(i).getTodayType();
            if (type==1&&TIME_LINE_1==-1) TIME_LINE_1 = i;
            if (type==2&&TIME_LINE_2==-1) TIME_LINE_2 = i;
            if (type==3&&TIME_LINE_3==-1) TIME_LINE_3 = i;
        }
        notifyDataSetChanged();
    }

    private void getParent(String c, String d) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

    }

    class ViewHolder {
        ImageView iv;
        TextView time, title, progress, tv_record_time;
        View v, check, space;
    }
}
