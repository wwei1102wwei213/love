package com.fuyou.play.widget.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2018-08-17.
 */
public class NestedGridView extends GridView{


    public NestedGridView(Context context) {
        super(context);
    }

    public NestedGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
