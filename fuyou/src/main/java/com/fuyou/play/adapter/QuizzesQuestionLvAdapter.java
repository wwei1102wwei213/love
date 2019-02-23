package com.fuyou.play.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.bean.quizzes.QuizzesQuestionBean;
import com.fuyou.play.util.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class QuizzesQuestionLvAdapter extends BaseAdapter {

    private Context context;
    private List<QuizzesQuestionBean.AllQuestionBean.AnswersBean> list;

    public QuizzesQuestionLvAdapter(Context context, List<QuizzesQuestionBean.AllQuestionBean.AnswersBean> list){
        this.context = context;
        if (list == null) list = new ArrayList<>();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public QuizzesQuestionBean.AllQuestionBean.AnswersBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        try {
            if (convertView==null){
                vh = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_quizzes_qusetion, parent, false);
                vh.content = (TextView) convertView.findViewById(R.id.tv_content);
                vh.line = convertView.findViewById(R.id.line);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            String content = list.get(position).getText();
            vh.content.setText(TextUtils.isEmpty(content)?"":content);
            if (position==list.size()-1){
                vh.line.setVisibility(View.GONE);
            } else {
                vh.line.setVisibility(View.VISIBLE);
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
        return convertView;
    }

    public void update(List<QuizzesQuestionBean.AllQuestionBean.AnswersBean> list){
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView content;
        View line;
    }

}
