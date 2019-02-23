package com.fuyou.play.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fuyou.play.R;
import com.fuyou.play.util.CommonUtils;
import com.fuyou.play.util.DensityUtils;
import com.fuyou.play.util.ExceptionUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/9/23.
 * 发帖时显示图片的gridview适配器
 */
public class GridAdapter extends BaseAdapter {

    private static final int ADD_VIEW = 0;
    private static final int NOMAL_VIEW = 1;
    private LayoutInflater inflater; // 视图容器

    private List<String> imgsUrl;
    private Context context;
    private int width;

    public GridAdapter(Context context, List<String> url) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.imgsUrl = url;
        width = (CommonUtils.getDeviceWidth(context) - DensityUtils.dp2px(context, 48))/4;
    }

    @Override
    public int getCount() {
        return imgsUrl.size() + 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == imgsUrl.size()) {
            return ADD_VIEW;
        } else {
        return NOMAL_VIEW;
        }
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void updateDate(List<String> url) {
        this.imgsUrl = url;
        this.notifyDataSetChanged();
    }

    public List<String> getList() {
        return imgsUrl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_post_image, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.img);
            ViewGroup.LayoutParams params = holder.image.getLayoutParams();
            params.width = width;
            params.height = width;
            holder.image.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int viewType = getItemViewType(position);

        if (viewType == ADD_VIEW) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.add_photo));
        } else {
            try {
                Glide.with(context).load(imgsUrl.get(position))
                        .into(holder.image);
            } catch (Exception e){
                ExceptionUtils.ExceptionSend(e);
                holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.add_photo));
            }
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView image;
    }
}

