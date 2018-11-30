package com.fuyou.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.fuyou.play.R;
import com.fuyou.play.util.GlideRoundTransform;

import java.util.List;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class QuizzesGvAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private String baseUrl;
    private int dex;
    public QuizzesGvAdapter(Context context, List<String> list, int dex, String baseUrl){
        this.list = list;
        this.dex = dex;
        this.context = context;
        this.baseUrl = baseUrl;
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
        if (convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_quizzes_body_gv, parent, false);
            vh.iv = (ImageView) convertView.findViewById(R.id.iv_item_quizzes_body_gv);
            vh.tv = (TextView) convertView.findViewById(R.id.tv_item_quizzes_body_gv);
            vh.v = (RelativeLayout)convertView.findViewById(R.id.v_ll);
            ViewGroup.LayoutParams params = vh.v.getLayoutParams();
            params.width = dex;
            params.height = dex;
            vh.v.setLayoutParams(params);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        String str = list.get(position);
        String url = baseUrl + str.replace(" ", "+").substring(0, str.length() - 4) + "jpg";

        vh.tv.setText(str.substring(0, str.length() - 5));
        Glide.with(context).load(url)
                .bitmapTransform(new CenterCrop(context), new GlideRoundTransform(context,6))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(vh.iv);
        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
        RelativeLayout v;
    }

}
