package com.fuyou.play.view.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.fuyou.play.R;
import com.fuyou.play.bean.other.BannerEntity;
import com.fuyou.play.bean.other.MenuEntity;
import com.fuyou.play.util.CommonUtils;
import com.fuyou.play.util.DensityUtils;
import com.fuyou.play.util.GlideRoundTransform;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.view.BaseFragment;
import com.fuyou.play.view.activity.TestAcctivity;
import com.fuyou.play.widget.pullwidget.elasticity.ElasticityHelper;
import com.fuyou.play.widget.pullwidget.elasticity.ElasticityNestedScrollView;
import com.fuyou.play.widget.pullwidget.pullrefresh.PullElasticityNestedScrollView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/23 0023.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private PullElasticityNestedScrollView pv;
    private ElasticityNestedScrollView sv;
    private LinearLayout v_menu;
    private Banner banner;
    private View statusBar;
    private float HeightTop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        statusBar = findViewById(R.id.status_bar);
        HeightTop = DensityUtils.dp2px(context, 170);
        initStatusBar(statusBar);
        statusBar.setAlpha(0);
        initView();
        return rootView;
    }

    private void initView() {
        try {
            pv = (PullElasticityNestedScrollView) findViewById(R.id.pv);
            sv = pv.getRefreshableView();
            LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.content_home_fragment, sv, false);
            initMenu(layout);
            initBanner(layout);
            initQuizzes(layout);
            initMatching(layout);
            initLibrary(layout);
            sv.addView(layout);
            pv.setPullRefreshEnabled(false);
            pv.setPullLoadEnabled(false);
            sv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (pv!=null) statusBarChangerAlpha(scrollY);
                }
            });
            ElasticityHelper.setUpOverScroll(sv);
        } catch (Exception e){
            LogCustom.e(e, "initView");
        }
    }

    //设置顶部主菜单
    private void initMenu(ViewGroup group) {
        v_menu = (LinearLayout) group.findViewById(R.id.v_menu);
        List<MenuEntity> list = new ArrayList<>();
        MenuEntity entity = new MenuEntity(R.mipmap.menu_home_top_1, "Test");
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        for (int i=0;i<list.size();i++) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_menu_home_top, v_menu, false);
            ImageView iv = (ImageView) v.findViewById(R.id.iv);
            Glide.with(context).load(list.get(i).getRes())
                    .bitmapTransform(new CenterCrop(context), new GlideRoundTransform(context,8))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade().into(iv);
            TextView tv = (TextView) v.findViewById(R.id.tv);
            tv.setText(entity.getName());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToast("Test");
                }
            });
            v_menu.addView(v);
        }
    }

    //设置BANNER
    private void initBanner(ViewGroup group) {
        banner = (Banner) group.findViewById(R.id.banner);
        banner.setIndicatorGravity(BannerConfig.RIGHT);

        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                try {
                    BannerEntity entity = (BannerEntity) path;
                    imageView.setImageResource(entity.getRes());
                } catch (Exception e){
                    LogCustom.e(e, "");
                }
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                showToast(""+position);
            }
        });
        List<BannerEntity> list = new ArrayList<>();
        BannerEntity entity = new BannerEntity(R.mipmap.banner_test, "", 0);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        banner.setImages(list);
        banner.start();
    }

    //设置测试菜单
    private void initQuizzes(ViewGroup group) {
        int width = CommonUtils.getDeviceWidth(context);
        if (width==0) return;
        int height = (width - DensityUtils.dp2px(context, 40))/3;
        ImageView iv1 = (ImageView) group.findViewById(R.id.iv_quizzes_1);
        ImageView iv2 = (ImageView) group.findViewById(R.id.iv_quizzes_2);
        ImageView iv3 = (ImageView) group.findViewById(R.id.iv_quizzes_3);
        setViewHeight(iv1, height);
        setViewHeight(iv2, height);
        setViewHeight(iv3, height);
        iv1.setImageResource(R.mipmap.menu_home_top_1);
        iv2.setImageResource(R.mipmap.menu_home_top_1);
        iv3.setImageResource(R.mipmap.menu_home_top_1);
        group.findViewById(R.id.v_quizzes_1).setOnClickListener(this);
        group.findViewById(R.id.v_quizzes_2).setOnClickListener(this);
        group.findViewById(R.id.v_quizzes_3).setOnClickListener(this);
    }

    //设置匹配菜单
    private void initMatching(ViewGroup group) {
        int width = CommonUtils.getDeviceWidth(context);
        if (width==0) return;
        int height = (width - DensityUtils.dp2px(context, 25))/3;
        ImageView iv1 = (ImageView) group.findViewById(R.id.iv_matching_1);
        ImageView iv2 = (ImageView) group.findViewById(R.id.iv_matching_2);
        ImageView iv3 = (ImageView) group.findViewById(R.id.iv_matching_3);
        ImageView iv4 = (ImageView) group.findViewById(R.id.iv_matching_4);
        setViewHeight(iv1, height);
        setViewHeight(iv2, height);
        setViewHeight(iv3, height);
        setViewHeight(iv4, height);
        iv1.setImageResource(R.mipmap.menu_home_top_1);
        iv2.setImageResource(R.mipmap.menu_home_top_1);
        iv3.setImageResource(R.mipmap.menu_home_top_1);
        iv4.setImageResource(R.mipmap.menu_home_top_1);
        group.findViewById(R.id.v_matching_1).setOnClickListener(this);
        group.findViewById(R.id.v_matching_2).setOnClickListener(this);
        group.findViewById(R.id.v_matching_3).setOnClickListener(this);
        group.findViewById(R.id.v_matching_4).setOnClickListener(this);
    }

    //设置
    private void initLibrary(ViewGroup group) {
        int width = CommonUtils.getDeviceWidth(context);
        if (width==0) return;
        int height = (width - DensityUtils.dp2px(context, 24))/3;
        ImageView iv1 = (ImageView) group.findViewById(R.id.iv_library_1);
        ImageView iv2 = (ImageView) group.findViewById(R.id.iv_library_2);
        ImageView iv3 = (ImageView) group.findViewById(R.id.iv_library_3);
        ImageView iv4 = (ImageView) group.findViewById(R.id.iv_library_4);
        ImageView iv5 = (ImageView) group.findViewById(R.id.iv_library_5);
        iv1.setImageResource(R.mipmap.home_library_zodiac_signs);
        iv2.setImageResource(R.mipmap.home_library_zodiac_signs);
        iv3.setImageResource(R.mipmap.home_library_zodiac_signs);
        iv4.setImageResource(R.mipmap.home_library_zodiac_signs);
        iv5.setImageResource(R.mipmap.home_library_zodiac_signs);
        group.findViewById(R.id.v_library_1).setOnClickListener(this);
        group.findViewById(R.id.v_library_2).setOnClickListener(this);
        group.findViewById(R.id.v_library_3).setOnClickListener(this);
        group.findViewById(R.id.v_library_4).setOnClickListener(this);
        group.findViewById(R.id.v_library_5).setOnClickListener(this);
    }

    //设置滑动时状态栏透明度
    private void statusBarChangerAlpha(int top){
        try {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
                if (top>0) {
                    float alpha = top/HeightTop;
                    alpha = alpha>1?1:alpha;
                    statusBar.setAlpha(alpha>1?1:alpha);
                } else {
                    statusBar.setAlpha(0);
                }
            }
        } catch (Exception e){
            LogCustom.e(e, "");
        }
    }

    private void setViewHeight(View v, int height) {
        try {
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.height = height;
            v.setLayoutParams(params);
        } catch (Exception e){
            LogCustom.e(e, "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_quizzes_1:
                startActivity(TestAcctivity.class);
                break;
            case R.id.v_quizzes_2:
                showToast("2");
                break;
            case R.id.v_quizzes_3:
                showToast("3");
                break;
            case R.id.v_matching_1:
                showToast("4");
                break;
            case R.id.v_matching_2:
                showToast("5");
                break;
            case R.id.v_matching_3:
                showToast("6");
                break;
            case R.id.v_matching_4:
                showToast("7");
                break;
            case R.id.v_library_1:
                showToast("7");
                break;
            case R.id.v_library_2:
                showToast("7");
                break;
            case R.id.v_library_3:
                showToast("7");
                break;
            case R.id.v_library_4:
                showToast("7");
                break;
            case R.id.v_library_5:
                showToast("7");
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (banner!=null) banner.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (banner!=null) banner.stopAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (banner!=null) banner.stopAutoPlay();
    }
}
