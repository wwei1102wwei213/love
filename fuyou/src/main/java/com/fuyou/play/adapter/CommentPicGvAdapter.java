package com.fuyou.play.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fuyou.play.R;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.view.activity.ShowPhotoActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018-08-17.
 */
public class CommentPicGvAdapter extends android.widget.BaseAdapter{

    private Context context;
    private List<String> list;
    private int wh;

    public CommentPicGvAdapter(Context context, List<String> list, int wh){
        this.context = context;
        this.list = list;
        this.wh = wh;
    }

    @Override
    public int getCount() {
        if (list!=null) return list.size();
        return 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment_item_pic, parent, false);
            vh.iv_item = (ImageView) convertView.findViewById(R.id.iv);
            setViewSize(vh.iv_item);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            if (!TextUtils.isEmpty(list.get(position))) {
                Glide.with(context).load(list.get(position)).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.mipmap.default_error_img).into(vh.iv_item);
                vh.iv_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, ShowPhotoActivity.class)
                                .putExtra("list_show_photo", (Serializable) (list)).putExtra("position", position));
                    }
                });
            } else {
                vh.iv_item.setImageResource(R.mipmap.default_error_img);
            }

        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
        return convertView;
    }

    private void setViewSize(View v){
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.width = wh;
        params.height = wh;
        v.setLayoutParams(params);
    }

    class ViewHolder{
        ImageView iv_item;
    }
}
