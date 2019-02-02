package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.player.MyJzvdStd;
import com.yyspbfq.filmplay.ui.dialog.LoginDialog;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.SystemUtils;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            vh.jzvd.setUp(entity.getVideo_url()
                    , entity.getName(), entity,  JzvdStd.SCREEN_WINDOW_LIST);
            Glide.with(context).
                    load(entity.getVideo_thump()).
                    into(vh.jzvd.thumbImageView);
            vh.jzvd.positionInList = position;
            vh.watch.setText(entity.getWatch_num()+"次播放");
            if (entity.getCanCollection()!=1) {
                vh.collation.setImageResource(R.mipmap.icon_ilike_select);
            } else {
                vh.collation.setImageResource(R.mipmap.icon_ilike_default);
            }
            vh.collation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCollation(entity.getCanCollection(), position);
                }
            });
            vh.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toShare();
                }
            });
        } catch (Exception e){
            BLog.e(e);
        }
        return convertView;
    }

    private void toShare() {
        try {
            SystemUtils.copyTextToClip(context,SPLongUtils.getInviteCodeUrl(context));
            ToastUtils.showToast("复制成功，快去分享吧");
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private boolean isLoading = false;
    private void toCollation(int isCan, int position) {
        if (!UserDataUtil.isLogin(context)) {
            new LoginDialog(context).show();
            return;
        }
        if (isLoading) return;
        if (isCan!=1) {
            ToastUtils.showToast("已经收藏过该视频");
        } else {
            isLoading = true;
            Map<String, String> map = new HashMap<>();
            map.put("vid", list.get(position).getId());
            Factory.resp(new WLibHttpListener() {
                @Override
                public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
                    try {
                        list.get(position).setCanCollection(0);
                        notifyDataSetChanged();
                    } catch (Exception e){

                    }
                }

                @Override
                public void handleLoading(int flag, Object tag, boolean isShow) {

                }

                @Override
                public void handleError(int flag, Object tag, int errorType, String response, String hint) {

                }

                @Override
                public void handleAfter(int flag, Object tag) {
                    isLoading = false;
                }
            }, HttpFlag.FLAG_VIDEO_COLLECTION, null, null).post(map);
        }
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
