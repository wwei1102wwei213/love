package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.HomeClassifyBean;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.ArrayList;
import java.util.List;

public class ClassifyChooseAdapter extends RecyclerView.Adapter<ClassifyChooseAdapter.ViewHolder>{

    private Context context;
    private List<HomeClassifyBean> list;
    private int selectIndex = 0;
    private ClassifyTagSelectListener listener;

    public ClassifyChooseAdapter (Context context, List<HomeClassifyBean> list, ClassifyTagSelectListener listener) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        this.listener = listener;
    }

    public ClassifyChooseAdapter (Context context, List<HomeClassifyBean> list, ClassifyTagSelectListener listener, int selectIndex) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        this.selectIndex = selectIndex;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classify_tag_rv, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            final HomeClassifyBean bean = list.get(position);
            String tagName = bean.getTname();
            holder.tv.setText(TextUtils.isEmpty(tagName)?"未知分类":tagName);
            if (position==selectIndex) {
                holder.tv.setSelected(true);
            } else {
                holder.tv.setSelected(false);
            }
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSelectPosition(bean, position);
                }
            });
        } catch (Exception e){
            BLog.e(e);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getItemText() {
        return list.get(selectIndex).getTname();
    }

    public void update(List<HomeClassifyBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void updateSelectPosition(int selectIndex) {
        if (this.selectIndex == selectIndex) return;
        this.selectIndex = selectIndex;
        notifyDataSetChanged();
    }

    public void updateSelectPosition(HomeClassifyBean bean, int selectIndex) {
        if (this.selectIndex == selectIndex) return;
        this.selectIndex = selectIndex;
        notifyDataSetChanged();
        if (listener!=null) listener.OnClassifyTagSelected(bean, selectIndex);
    }

    public interface ClassifyTagSelectListener {
        void OnClassifyTagSelected(HomeClassifyBean bean, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View v;
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            tv = itemView.findViewById(R.id.tv_tag);
        }
    }

}
