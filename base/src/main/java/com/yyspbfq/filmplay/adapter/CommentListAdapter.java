package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.widget.CircleImageView;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.CommentEntity;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.dialog.LoginDialog;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentListAdapter extends BaseAdapter{

    private Context context;
    private List<CommentEntity> list;
    private List<Integer> zanLists;
    public CommentListAdapter (Context context, List<CommentEntity> list) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        zanLists = new ArrayList<>();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment_list_lv, parent, false);
            vh.civ = convertView.findViewById(R.id.civ);
            vh.iv_comment_zan = convertView.findViewById(R.id.iv_comment_zan);
            vh.tv_zan_num = convertView.findViewById(R.id.tv_zan_num);
            vh.tv_title = convertView.findViewById(R.id.tv_title);
            vh.tv_comment_time = convertView.findViewById(R.id.tv_comment_time);
            vh.tv_comment_content = convertView.findViewById(R.id.tv_comment_content);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        try {
            final CommentEntity entity = list.get(position);
            Glide.with(context).load(entity.getFig()).crossFade().into(vh.civ);
            vh.tv_zan_num.setText(entity.getZan()==null?"0":entity.getZan());
            if (zanLists.contains(position)) {
                vh.iv_comment_zan.setSelected(true);
            } else {
                vh.iv_comment_zan.setSelected(false);
            }
            vh.iv_comment_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!UserDataUtil.isLogin(context)) {
                        new LoginDialog(context).show();
                        return;
                    }
                    if (zanLists.contains(position)) {
                        ToastUtils.showToast("您已经赞过了");
                    } else {
                        toCommentZan(entity, position);
                    }
                }
            });
            vh.tv_title.setText(entity.getNick()==null?"":entity.getNick());
            vh.tv_comment_time.setText(entity.getCreateDate()==null?"":entity.getCreateDate());
            vh.tv_comment_content.setText(entity.getMess()==null?"":entity.getMess());

        } catch (Exception e){
            BLog.e(e);
        }

        return convertView;
    }

    public void update(List<CommentEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private void toCommentZan(CommentEntity entity, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("vid", entity.getVid());
        map.put("id", entity.getId());
        Factory.resp(new WLibHttpListener() {
            @Override
            public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
                try {
                    if (!zanLists.contains((int) tag)) {
                        zanLists.add((int) tag);
                        notifyDataSetChanged();
                        if (!TextUtils.isEmpty(hint)) ToastUtils.showToast(hint);
                    }
                } catch (Exception e){
                    BLog.e(e);
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

            }
        }, HttpFlag.FLAG_COMMENT_LIKE, position, null).post(map);
    }

    class  ViewHolder {
        CircleImageView civ;
        ImageView iv_comment_zan;
        TextView tv_zan_num, tv_title, tv_comment_time, tv_comment_content;
    }

}
