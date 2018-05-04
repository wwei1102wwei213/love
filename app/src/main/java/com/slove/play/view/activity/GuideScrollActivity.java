package com.slove.play.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slove.play.R;
import com.slove.play.util.ExceptionUtils;
import com.slove.play.view.BaseActivity;


/**
 *  引导页
 */

public class GuideScrollActivity extends BaseActivity {

    private Context mContext;
    private ViewPager mViewPager;
    private int[] mScrollImageRes = new int[]{R.mipmap.splash_bg, R.mipmap.splash_bg, R.mipmap.splash_bg, R.mipmap.splash_bg};
    private View[] mScrollImg = new View[4];
    private TextView mSkipView;
    private TextView mEnterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_scroll);
        mContext = this;

        initView();
    }

    private void initView() {
        try{
            mSkipView = (TextView) findViewById(R.id.skip_view);
            mEnterView = (TextView) findViewById(R.id.enter_view);
            mSkipView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }
            });
            mEnterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }
            });
            mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
            for (int i = 0; i < mScrollImg.length; i++) {
                ImageView img = new ImageView(mContext);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                img.setImageResource(mScrollImageRes[i]);
                mScrollImg[i] = img;
            }
            mViewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return mScrollImg.length;
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public void destroyItem(View container, int position, Object object) {
                    ((ViewPager) container).removeView(mScrollImg[position]);
                }

                @Override
                public Object instantiateItem(View container, int position) {
                    ((ViewPager) container).addView(mScrollImg[position]);
                    return mScrollImg[position];
                }
            });

            mViewPager.setOnPageChangeListener(new GuideOnPageChangeListener());
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "GuideScrollActivity initView");
        }
    }

    public class GuideOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int page) {
            switch (page) {
                case 3:
                    mEnterView.setVisibility(View.VISIBLE);
                    break;
                default:
                    mEnterView.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }
}
