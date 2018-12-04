package com.fuyou.play.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.fuyou.play.R;
import com.fuyou.play.adapter.common.CCAdapterHolder;
import com.fuyou.play.adapter.common.CCRecyclerAdapter;

import java.util.List;

/**
 * Created by Creacc on 2017/9/25.
 */

public class TarotChooseAdapter extends CCRecyclerAdapter<Integer> {

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public TarotChooseAdapter(Context context, List<Integer> adapterContent) {
        super(context, adapterContent);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public CCAdapterHolder<Integer> createHolder(int type) {
        return new CCAdapterHolder<Integer>() {

            @Override
            public int getResource() {
                return R.layout.item_tarot_choose;
            }

            @Override
            public void initializeView(View convertView) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(null, view, getPosition(view), 0);
                        }
                    }
                });
            }

            @Override
            public void updateView(Integer content, int position) {
            }
        };
    }

}
