package com.fuyou.play.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.fuyou.play.bean.moment.MomentComment;
import com.fuyou.play.bean.moment.MomentItem;
import com.fuyou.play.util.DensityUtils;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.widget.custom.CollapsibleTextView;
import com.fuyou.play.widget.custom.CommentTextView;

import java.util.List;

/**
 * Created by Administrator on 2018-08-17.
 */
public class MomentListAdapter extends BaseRecyclerAdapter<MomentListAdapter.ViewHolder>{

    private Context context;
    private List<MomentItem> list;
    private int width;

    public MomentListAdapter(Context context, List<MomentItem> list) {
        this.context = context;
        this.list = list;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int WIDTH = dm.widthPixels;
        width = (WIDTH - DensityUtils.dp2px(context, 62 + 30 + 8))/3;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, 0, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment_list, parent, false);
        return new ViewHolder(v, viewType, true);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {
        try {
            final MomentItem item = list.get(position);
            holder.tv_name.setText(TextUtils.isEmpty(item.getSender().getNick())?"":item.getSender().getNick());
            Glide.with(context).load(item.getSender().getAvatar()).placeholder(R.mipmap.default_error_img).into(holder.iv);
            if (TextUtils.isEmpty(item.getContent())) {
                holder.ctv.setVisibility(View.GONE);
            } else {
                holder.ctv.setText(item.getContent());
                holder.ctv.setVisibility(View.VISIBLE);
            }
            holder.gv.setVisibility(View.GONE);
            /*if (item.getImages()!=null&&item.getImages().size()>0) {
                CommentPicGvAdapter adapter = new CommentPicGvAdapter(context, item.getImages(), width);
                holder.gv.setAdapter(adapter);
                holder.gv.setVisibility(View.VISIBLE);
            } else {
                holder.gv.setVisibility(View.GONE);
            }*/
            List<MomentComment> comments = item.getComments();
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
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    @Override
    public int getAdapterItemCount() {
        if (list!=null&&list.size()>0) return list.size();
        return 0;
    }

    public void update(List<MomentItem> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private ImageView iv;
        private CollapsibleTextView ctv;
        private GridView gv;
        private LinearLayout ll_comment;

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
