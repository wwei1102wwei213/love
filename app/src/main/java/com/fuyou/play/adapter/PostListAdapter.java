package com.fuyou.play.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fuyou.play.R;
import com.fuyou.play.bean.UserInfo;
import com.fuyou.play.bean.discuss.DiscussEntity;
import com.fuyou.play.util.CommonUtils;
import com.fuyou.play.util.Const;
import com.fuyou.play.util.DensityUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.view.activity.InfoProfileActivity;
import com.fuyou.play.widget.custom.CollapsibleTextView;

import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends BaseAdapter{

    private Context context;
    private List<DiscussEntity> list;
    private int width;

    public PostListAdapter(Context context, List<DiscussEntity> list) {
        this.context = context;
        this.list = list;
        width = (CommonUtils.getDeviceWidth(context) - DensityUtils.dp2px(context, 62 + 30 + 8))/3;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
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
        ViewHolder holder;
        LogCustom.e("position:"+position+",");
        if (convertView==null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_discuss_list_rv, parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.iv = convertView.findViewById(R.id.iv_avatar);
            holder.ctv = convertView.findViewById(R.id.ctv);
            holder.gv = convertView.findViewById(R.id.gv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            final DiscussEntity entity = list.get(position);
            holder.tv_name.setText(TextUtils.isEmpty(entity.getAuthorName())?"":entity.getAuthorName());
            if (TextUtils.isEmpty(entity.getContent())) {
                holder.ctv.setVisibility(View.GONE);
            } else {
                holder.ctv.setText(entity.getContent());
                holder.ctv.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(entity.getAuthorIcon())) {
                holder.iv.setImageResource(R.mipmap.default_error_img);
            } else {
                Glide.with(context).load(entity.getAuthorIcon()).placeholder(R.mipmap.default_error_img).into(holder.iv);
            }
            if (TextUtils.isEmpty(entity.getImages())) {
                holder.gv.setVisibility(View.GONE);
            } else {
                List<String> temp = new ArrayList<>();
                String[] args = entity.getImages().split(",");
                for (String arg:args) {
                    temp.add(arg);
                }
                CommentPicGvAdapter adapter = new CommentPicGvAdapter(context, temp, width);
                holder.gv.setAdapter(adapter);
                holder.gv.setVisibility(View.VISIBLE);
            }
            final UserInfo info = new UserInfo();
            info.setId(Integer.parseInt(entity.getAuthor()));
            info.setIcon(entity.getAuthorIcon());
            info.setNickName(entity.getAuthorName());
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, InfoProfileActivity.class);
                    intent.putExtra(Const.INTENT_CHAT_TYPE, 0);
                    intent.putExtra(Const.INTENT_CHAT_USER, info);
                    context.startActivity(intent);
                }
            });
        } catch (Exception e){
            LogCustom.e(e, "");
        }
        return convertView;
    }

    public void update(List<DiscussEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tv_name;
        ImageView iv;
        CollapsibleTextView ctv;
        GridView gv;
    }
}
