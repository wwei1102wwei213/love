package com.fuyou.play.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.fuyou.play.R;
import com.fuyou.play.bean.UserInfo;
import com.fuyou.play.bean.discuss.DiscussEntity;
import com.fuyou.play.util.CommonUtils;
import com.fuyou.play.util.Const;
import com.fuyou.play.util.DensityUtils;
import com.fuyou.play.view.activity.InfoProfileActivity;
import com.fuyou.play.widget.custom.CollapsibleTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-07-26.
 */
public class DiscussListAdapter extends BaseRecyclerAdapter<DiscussListAdapter.ViewHolder>{

    private Context context;
    private List<DiscussEntity> list;
    private int width;

    public DiscussListAdapter(Context context, List<DiscussEntity> list) {
        this.context = context;
        this.list = list;
        width = (CommonUtils.getDeviceWidth(context) - DensityUtils.dp2px(context, 62 + 30 + 8))/3;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, 0, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discuss_list_rv, parent, false);
        return new ViewHolder(v, viewType, true);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {
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
        /*List<MomentComment> comments = item.getComments();
        if (comments!=null && comments.size() > 0) {
            holder.ll_comment.removeAllViews();
            holder.ll_comment.setVisibility(View.VISIBLE);
            for (MomentComment itemBean:comments){
                CommentTextView ctv =  (CommentTextView) LayoutInflater.from(context).inflate(R.layout.tv_item_comment_post, holder.ll_comment, false);
                ctv.setTextComment(itemBean, new CommentTextView.CommentPartClickListener() {
                    @Override
                    public void onCommentPartClickListener(MomentComment itemBean, int position) {
                        //todo
                    }
                }, position);
                holder.ll_comment.addView(ctv);
            }
        } else {
            holder.ll_comment.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getAdapterItemCount() {
        if (list!=null) return list.size();
        return 0;
    }

    public void update(List<DiscussEntity> list){
        this.list = list;
        notifyDataSetChanged();
        /*try {
            if (postItems==null||postItems.size()==0){
                this.postItems = new ArrayList<>();
                notifyDataSetChanged();
            } else {
                if (startPosition==0){
                    notifyDataSetChanged();
                } else {
                    this.postItems = postItems;
                    try {
                        //因为有上拉头，所以下标需要+1，否则刷新的是上次的最后一条
                        //只需要刷新一条,其他的item执行的是onBindViewHolder
                        notifyItemChanged(startPosition+1);
                    } catch (Exception e){
                        NLog.e(e);
                    }
                }
            }
        } catch (Exception e){
            NLog.e(e);
        }*/
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private ImageView iv;
        private CollapsibleTextView ctv;
        private GridView gv;
        private LinearLayout ll_comment;
        private View pb;
        private TextView tv_loading;

        public ViewHolder(View v, int viewType, boolean isItem) {
            super(v);
            if (isItem) {
                tv_name = v.findViewById(R.id.tv_name);
                iv = v.findViewById(R.id.iv_avatar);
                ctv = v.findViewById(R.id.ctv);
                gv = v.findViewById(R.id.gv);
                ll_comment = v.findViewById(R.id.ll_comment);
            }
        }

    }

}
