package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.FeedbackMessageEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedbackMessageAdapter extends BaseAdapter {

    private Context context;
    private List<FeedbackMessageEntity> list;
    private SimpleDateFormat sdf;

    public FeedbackMessageAdapter(Context context, List<FeedbackMessageEntity> list) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_feedback_message_lv, parent, false);
            vh.tv_content = convertView.findViewById(R.id.tv_content);
            vh.tv_video_name = convertView.findViewById(R.id.tv_video_name);
            vh.tv_feedback_type = convertView.findViewById(R.id.tv_feedback_type);
            vh.tv_create_time = convertView.findViewById(R.id.tv_create_time);
            vh.tv_replay = convertView.findViewById(R.id.tv_replay);
            vh.tv_replay_time = convertView.findViewById(R.id.tv_replay_time);
            vh.v_replay = convertView.findViewById(R.id.v_replay);
            vh.v_line = convertView.findViewById(R.id.v_line);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final FeedbackMessageEntity entity = list.get(position);
        vh.tv_content.setText(entity.getOpinion()==null?"":entity.getOpinion());
        vh.tv_video_name.setText(TextUtils.isEmpty(entity.getVideo_name()) ?"无":entity.getVideo_name());
        vh.tv_create_time.setText(getDate(entity.getCretime()));
        vh.tv_content.setText(entity.getOpinion()==null?"":entity.getOpinion());
        String type = entity.getTypestr()==null?"":entity.getTypestr();
        if ("1".equals(entity.getStatus())) {
            vh.tv_feedback_type.setText(Html.fromHtml(type+"<font color=\"#c11f50\"> (已回复)</font>"));
            vh.v_replay.setVisibility(View.VISIBLE);
            vh.tv_replay_time.setText(getDate(entity.getRetime()));
            vh.tv_replay.setText(TextUtils.isEmpty(entity.getReply()) ?"无回复内容":entity.getReply());
        } else {
            vh.tv_feedback_type.setText(Html.fromHtml(type+"<font color=\"#666666\"> (未回复)</font>"));
            vh.v_replay.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void update(List<FeedbackMessageEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private String getDate(String time) {
        if (TextUtils.isEmpty(time)) return "未知创建时间";
        try {
            return sdf.format(new Date(Long.parseLong(time)*1000));
        } catch (Exception e){

        }
        return "未知创建时间";
    }

    private class ViewHolder {
        TextView tv_video_name, tv_feedback_type, tv_create_time, tv_replay, tv_replay_time, tv_content;
        View v_line, v_replay;
    }
}
