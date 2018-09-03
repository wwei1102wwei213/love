package com.fuyou.play.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.fuyou.play.R;
import com.fuyou.play.bean.chat.ChatMessageBean;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.widget.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-07-29.
 */
public class ChatAdapter extends BaseRecyclerAdapter<ChatAdapter.ViewHolder>{

    private Context context;
    private List<ChatMessageBean> list;
    private int InfoID = 0;
    private static final int CHAT_TEXT_LEFT = 1;
    private static final int CHAT_TEXT_RIGHT = 2;
    public ChatAdapter(Context context, List<ChatMessageBean> list) {
        this.context = context;
        try {
            InfoID = Integer.parseInt(UserDataUtil.getUserID(context));
        } catch (Exception e){

        }
        if (list==null) {
            this.list = new ArrayList<>();
        } else {
            this.list = list;
        }
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, 0, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = null;
        if (viewType == CHAT_TEXT_LEFT) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text_left, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text_right, parent, false);
        }
        return new ViewHolder(v, viewType, true);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {
        final ChatMessageBean bean = list.get(position);
        try {
            if (getAdapterItemViewType(position)==CHAT_TEXT_RIGHT) {
                holder.iv_left.setVisibility(View.INVISIBLE);
                holder.iv_right.setVisibility(View.VISIBLE);
                Glide.with(context).load(UserDataUtil.getAvatar(context)).placeholder(R.drawable.icon_chat_default)
                        .into(holder.iv_right);
                holder.tv_content.setBackgroundResource(R.drawable.rc_ic_bubble_right);
            } else {
                holder.iv_left.setVisibility(View.VISIBLE);
                holder.iv_right.setVisibility(View.INVISIBLE);
                Glide.with(context).load(bean.getFromIcon()==null?R.drawable.icon_chat_default:bean.getFromIcon())
                        .placeholder(R.drawable.icon_chat_default).into(holder.iv_left);
                holder.tv_content.setBackgroundResource(R.drawable.rc_ic_bubble_left);
            }
            holder.tv_content.setText(TextUtils.isEmpty(bean.getText())?"":bean.getText());
            if (position==0) {
                holder.tv_time.setVisibility(View.VISIBLE);
            } else {
                long time = 0;
                long lastTime = 0;
                try {
                    time = Long.parseLong(bean.getSendTime());
                    lastTime = Long.parseLong(list.get(position-1).getSendTime());
                } catch (Exception e){

                }
                if (time-lastTime>60000) {
                    holder.tv_time.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_time.setVisibility(View.GONE);
                }
            }
            holder.tv_time.setText(TextUtils.isEmpty(bean.getSendTime())?"":bean.getSendTime());
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (InfoID!=list.get(position).getFromID()) {
            return CHAT_TEXT_LEFT;
        }
        return CHAT_TEXT_RIGHT;
    }

    @Override
    public int getAdapterItemCount() {
        if (list!=null) return list.size();
        return 0;
    }

    public void update(List<ChatMessageBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time,tv_content;
        SelectableRoundedImageView iv_left,iv_right;
        public ViewHolder(View itemView, int viewType, boolean isItem) {
            super(itemView);
            if (isItem) {
                tv_time = itemView.findViewById(R.id.tv_time);
                tv_content = itemView.findViewById(R.id.tv_content);
                iv_left = itemView.findViewById(R.id.iv_left);
                iv_right = itemView.findViewById(R.id.iv_right);
            }
        }
    }
}
