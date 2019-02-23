package com.fuyou.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fuyou.play.R;

import java.util.List;

/**
 * Created by Administrator on 2018-08-26.
 */

public class GridPictureAdapter extends BaseAdapter{


    private List<String> imgsUrl;
    private Context context;

    public GridPictureAdapter(Context context, List<String> url) {
        this.context = context;
        this.imgsUrl = url;
    }

    @Override
    public int getCount() {
        return imgsUrl.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    public void updateDate(List<String> url) {
        this.imgsUrl = url;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*LayoutInflater.from(context)*/
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_post_image, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        int viewType = getItemViewType(position);
        Glide.with(context).load(imgsUrl.get(position))
                .into(holder.image);
        /*if (viewType == ADD_VIEW) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.add_photo));
        } else {
            try {
                Glide.with(context).load(imgsUrl.get(position))
                        .into(holder.image);
            } catch (Exception e){
                ExceptionUtils.ExceptionSend(e);
                holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.add_photo));
            }
        }*/
        return convertView;
    }

    class ViewHolder {
        public ImageView image;
    }

}
