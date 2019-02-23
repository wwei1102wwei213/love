package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.DeductionCoinBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.biz.login.UserHelper;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.ui.dialog.LoginDialog;
import com.yyspbfq.filmplay.ui.dialog.NormalDialog;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.SystemUtils;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;
import com.yyspbfq.filmplay.utils.tools.FileUtils;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class DiscoverAdapter extends BaseAdapter{

    private JzvdStd jvd;
    private Context context;
    private List<VideoEntity> list;
    private View mView;
    private int INDEX = -1;
    private boolean isPlayed = false;
    public DiscoverAdapter(Context context, List<VideoEntity> list) {
        this.context = context;
        if (list == null) list = new ArrayList<>();
        this.list = list;
        mView = LayoutInflater.from(context).inflate(R.layout.item_jvd_list, null);
        jvd = mView.findViewById(R.id.jvd);
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

            vh.collation = convertView.findViewById(R.id.iv_collation);
            vh.share = convertView.findViewById(R.id.iv_share);
            vh.watch = convertView.findViewById(R.id.tv_watch);
            vh.v_detail = convertView.findViewById(R.id.v_detail);
            vh.v_play = convertView.findViewById(R.id.v_play);
            vh.thumb = convertView.findViewById(R.id.iv_thumb);
            vh.title = convertView.findViewById(R.id.tv_title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final VideoEntity entity = list.get(position);
        try {


            vh.title.setText(entity.getName()==null?"":entity.getName());
            Glide.with(context).
                    load(entity.getVideo_thump()).
                    into(vh.thumb);
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
            if (position==INDEX) {
                vh.v_detail.setVisibility(View.INVISIBLE);
                try {
                    vh.v_play.removeAllViews();
                    vh.v_play.addView(mView);
                } catch (Exception e){
                    BLog.e(e);
                }
            } else {
                vh.v_detail.setVisibility(View.VISIBLE);
                vh.v_play.removeAllViews();
            }
            vh.v_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (isPlayed) {

                    }
                    */
                    Jzvd.releaseAllVideos();
                    isPlayed = false;
                    INDEX = -1;
                    notifyDataSetChanged();
                    try {
                        boolean isLocal = FileUtils.isVideoExist(entity.getId());
                        if (isLocal) {
                            Glide.with(context).load(entity.getVideo_thump()).into(jvd.thumbImageView);
                            try {
                                vh.v_play.removeAllViews();
                                vh.v_play.addView(mView);
                            } catch (Exception e){
                                BLog.e(e);
                            }
                            jvd.setUp(FileUtils.getVideoFileAbsolutePathWithMp4(entity.getId()), entity.getName(), entity,  JzvdStd.SCREEN_WINDOW_NORMAL);
                            jvd.toStart();
                            isPlayed = true;
                            INDEX = position;
                            notifyDataSetChanged();
                        } else {
                            Map<String, String> map = new HashMap<>();
                            map.put("vid", entity.getId());
                            map.put("type", "1");
                            Factory.resp(new WLibHttpListener() {
                                @Override
                                public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
                                    try {
                                        DeductionCoinBean bean = (DeductionCoinBean) formatData;
                                        if (bean.getCanHandle()==1) {
                                            Glide.with(context).load(entity.getVideo_thump()).into(jvd.thumbImageView);
                                            try {
                                                vh.v_play.removeAllViews();
                                                vh.v_play.addView(mView);
                                            } catch (Exception e){
                                                BLog.e(e);
                                            }
                                            jvd.setUp(entity.getVideo_url(), entity.getName(), entity,  JzvdStd.SCREEN_WINDOW_NORMAL);
                                            jvd.toStart();
                                            isPlayed = true;
                                            INDEX = position;
                                            notifyDataSetChanged();
                                            if (bean.getDeduction()==1) {
                                                UserHelper.getInstance().getUserInfo();
                                            }

                                        } else {
                                            NormalDialog normalDialog = new NormalDialog(context);
                                            normalDialog.setTitle("您今日的观影次数已经耗尽，是否去免费增加次数？");
                                            normalDialog.show();
                                        }
                                    } catch (Exception e){
                                        BLog.e(e);
                                    }
                                }
                                @Override
                                public void handleLoading(int flag, Object tag, boolean isShow) {}
                                @Override
                                public void handleError(int flag, Object tag, int errorType, String response, String hint) {}
                                @Override
                                public void handleAfter(int flag, Object tag) {}
                            }, HttpFlag.FLAG_DEDUCTION_COIN, null, DeductionCoinBean.class).post(map);
                        }
                    } catch (Exception e){
                        BLog.e(e);
                    }
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

    public int getINDEX() {
        return INDEX;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setINDEX(int INDEX) {
        this.INDEX = INDEX;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    private void toChange() {

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

    public void update(List<VideoEntity> list, int page) {
        if (list==null) list = new ArrayList<>();
        if (page==0) {
            this.list = list;
        } else {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView collation, share;
        TextView watch;
        View v_detail;
        ImageView thumb;
        TextView title;
        FrameLayout v_play;
    }
}
