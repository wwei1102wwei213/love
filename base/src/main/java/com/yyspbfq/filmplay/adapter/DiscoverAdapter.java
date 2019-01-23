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
import com.yyspbfq.filmplay.player.MyJzvdStd;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

public class DiscoverAdapter extends BaseAdapter{

    private Context context;
    private List<VideoEntity> list;
    public DiscoverAdapter(Context context, List<VideoEntity> list) {
        this.context = context;
        if (list == null) list = new ArrayList<>();
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_discover_lv, parent, false);
            vh.jzvd = convertView.findViewById(R.id.jvd);
            vh.collation = convertView.findViewById(R.id.iv_collation);
            vh.share = convertView.findViewById(R.id.iv_share);
            vh.watch = convertView.findViewById(R.id.tv_watch);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final VideoEntity entity = list.get(position);
        try {
            VideoEntity test = new VideoEntity();
            test.setId("1");
            test.setVideo_url("https://v1.bugogames.com/20181228/DzsBvBMv/index.m3u8");
            test.setName(entity.getName());
            vh.jzvd.setUp(test.getVideo_url()
                    , entity.getName(), test,  JzvdStd.SCREEN_WINDOW_LIST);
            Glide.with(context).
                    load(entity.getVideo_thump()).
                    into(vh.jzvd.thumbImageView);
            vh.jzvd.positionInList = position;
            vh.watch.setText(entity.getWatch_num()+"次播放");
        } catch (Exception e){
            BLog.e(e);
        }
        return convertView;
    }

    public void update(List<VideoEntity> list) {
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView collation, share;
        TextView watch;
        MyJzvdStd jzvd;
    }
}
