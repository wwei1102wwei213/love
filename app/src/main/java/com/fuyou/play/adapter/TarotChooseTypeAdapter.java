package com.fuyou.play.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 *  选择塔罗类型Adapter
 */

public class TarotChooseTypeAdapter extends PagerAdapter {
    private List<View> viewLists;

    public TarotChooseTypeAdapter(Context context, List<View> viewLists) {
        this.viewLists = viewLists;
    }

    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View view, int position, Object object)                       //销毁Item
    {
        ((ViewPager) view).removeView(viewLists.get(position));
    }

    @Override
    public Object instantiateItem(View view, int position)                                //实例化Item
    {
        ((ViewPager) view).addView(viewLists.get(position), 0);

        return viewLists.get(position);
    }

}
