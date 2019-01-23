package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.UserInvitee;

import java.util.List;


public class ListInviteeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<UserInvitee> mData;

    public ListInviteeAdapter(Context context, List<UserInvitee> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public UserInvitee getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_lv_my_invite, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.mobile = (TextView) convertView.findViewById(R.id.tv_mobile);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final UserInvitee userInvitee = mData.get(position);
        holder.name.setText(userInvitee.name==null?"":userInvitee.name);
        holder.mobile.setText(getMobile(userInvitee.mobile));
        holder.time.setText(userInvitee.cdate);

        return convertView;
    }

    private String getMobile(String mobile ) {
        if (mobile==null) return "";
        if (mobile.length()>7) {
            return mobile.substring(0, 3) + "****" +
                    mobile.substring(mobile.length()-4, mobile.length());
        }
        return "";
    }

    private final class ViewHolder {
        public TextView name;
        public TextView time;
        public TextView mobile;
    }

}
