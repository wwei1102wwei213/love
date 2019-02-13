package com.yyspbfq.filmplay.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.widget.DrawCircleView;
import com.wei.wlib.widget.coverflow.CoverFlow;
import com.wei.wlib.widget.coverflow.core.PageItemClickListener;
import com.wei.wlib.widget.coverflow.core.PagerContainer;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.HomeLikeAdapter;
import com.yyspbfq.filmplay.bean.AdvertBean;
import com.yyspbfq.filmplay.bean.HomeClassifyBean;
import com.yyspbfq.filmplay.bean.HomeColumnBean;
import com.yyspbfq.filmplay.bean.SlideBean;
import com.yyspbfq.filmplay.bean.VideoShortBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseFragment;
import com.yyspbfq.filmplay.ui.activity.DownloadListActivity;
import com.yyspbfq.filmplay.ui.activity.VideoClassifyActivity;
import com.yyspbfq.filmplay.ui.activity.VideoRecordActivity;
import com.yyspbfq.filmplay.ui.activity.VideoSearchActivity;
import com.yyspbfq.filmplay.ui.dialog.LoginDialog;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.UiUtils;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements WLibHttpListener{

    private int indexBanner;
    private MyHandler mHandler;
    private static int CHANGE_BANNER = 1;
    private AdvertBean mAdvertBean;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.base_fragment_home, container, false);
        initStatusBar(findViewById(R.id.status_bar));
        mHandler = new MyHandler(this);
        initScreenSize();
        initViews();
        initData();
        return rootView;
    }

    private int mItemW, mItemH;
    private void initScreenSize() {
        mItemW = (CommonUtils.getDeviceWidth(context) - DensityUtils.dp2px(context, 30))/2;
        mItemH = mItemW*9/16;
    }

    private ViewPager pager;
    private PagerContainer container;
    private LinearLayout ll_classify;
    private LinearLayout ll_newest;
    private LinearLayout ll_hot;
    private LinearLayout ll_column;
    private RecyclerView rv_like;
    private HomeLikeAdapter likeAdapter;
    private MyPagerAdapter pagerAdapter;
    private DrawCircleView dcv;
    private ImageView iv_adv;
    private void initViews() {

        ll_classify = (LinearLayout) findViewById(R.id.ll_classify);
        ll_newest = (LinearLayout) findViewById(R.id.ll_newest);
        ll_hot = (LinearLayout) findViewById(R.id.ll_hot);
        ll_column = (LinearLayout) findViewById(R.id.ll_column);
        iv_adv = (ImageView) findViewById(R.id.iv_adv);

        rv_like = (RecyclerView) findViewById(R.id.rv_like);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_like.setLayoutManager(manager);
        likeAdapter = new HomeLikeAdapter(context, null);
        rv_like.setAdapter(likeAdapter);

        container = (PagerContainer) findViewById(R.id.pc_banner);
        pager = container.getViewPager();
        pager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        List<SlideBean> temp = new ArrayList<>();
        pagerAdapter = new MyPagerAdapter(temp);
        pager.setAdapter(pagerAdapter);
        pager.setClipChildren(false);
        pager.setOffscreenPageLimit(8);
        indexBanner = 0;

        container.setPageItemClickListener(new PageItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showToast("position:" + position);
            }
        });
        container.setPagerChangeListener(new PagerContainer.PagerChangeListener() {
            @Override
            public void onPagerSelected(int position) {
                try {
                    indexBanner = position;
                    dcv.redraw(position%pagerAdapter.getListSize());
                } catch (Exception e){
                    BLog.e(e);
                }
            }
        });
        new CoverFlow.Builder()
                .with(pager)
                .scale(0.3f)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.home_banner_margin))
                .spaceSize(0f)
                .build();

        dcv = (DrawCircleView) findViewById(R.id.dcv);

        findViewById(R.id.tv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSearchActivity.actionStart(context);
            }
        });
        findViewById(R.id.iv_search_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadListActivity.actionStart(context);
            }
        });
        findViewById(R.id.iv_search_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserDataUtil.isLogin(context)) {
                    VideoRecordActivity.actionStart(context);
                } else {
                    new LoginDialog(context).show();
                }
            }
        });

        findViewById(R.id.more_newest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoClassifyActivity.actionStart(context, "0");
            }
        });
        findViewById(R.id.more_hot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoClassifyActivity.actionStart(context, "0");
            }
        });
//        mHandler.sendEmptyMessageDelayed(CHANGE_BANNER, 4000);
        iv_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.handleAdvert(context, mAdvertBean);
            }
        });
    }

    private void initData() {
        Factory.resp(this, HttpFlag.FLAG_HOME_SLIDE, null, null).post(null);
        Factory.resp(this, HttpFlag.FLAG_HOME_MENU_COLUMN, null, null).post(null);
        Factory.resp(this, HttpFlag.FLAG_HOME_LIKE_VIDEO, null, null).post(null);
        Factory.resp(this, HttpFlag.FLAG_HOME_NEWEST_VIDEO, null, null).post(null);
        Factory.resp(this, HttpFlag.FLAG_HOME_HOT_VIDEO, null, null).post(null);
        Factory.resp(this, HttpFlag.FLAG_HOME_CLASSIFY_LIST, null, null).post(null);
        Factory.resp(this, HttpFlag.FLAG_ADVERT_INDEX, null, AdvertBean.class).post(null);
    }

    private boolean isPause = false;
    private void changeBanner() {
        if (!isPause) {
            pager.setCurrentItem(indexBanner+1);
        }
        mHandler.sendEmptyMessageDelayed(CHANGE_BANNER, 4000);
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_HOME_MENU_COLUMN) {//推荐栏目
            try {
                List<HomeColumnBean> datas = new Gson().fromJson(formatData.toString(), new TypeToken<List<HomeColumnBean>>(){}.getType());
                UiUtils.handleHomeColumn(context, ll_column, datas, mItemH);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_HOME_LIKE_VIDEO) {//猜你喜欢
            try {
                List<VideoShortBean> datas = new Gson().fromJson(formatData.toString(), new TypeToken<List<VideoShortBean>>(){}.getType());
                likeAdapter.update(datas);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_HOME_NEWEST_VIDEO) {//最新
            try {
                List<VideoShortBean> datas = new Gson().fromJson(formatData.toString(), new TypeToken<List<VideoShortBean>>(){}.getType());
                UiUtils.handleHomeMenuDefault(context, ll_newest, datas, mItemH);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_HOME_HOT_VIDEO) {//最热
            try {
                List<VideoShortBean> datas = new Gson().fromJson(formatData.toString(), new TypeToken<List<VideoShortBean>>(){}.getType());
                UiUtils.handleHomeMenuDefault(context, ll_hot, datas, mItemH);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_HOME_CLASSIFY_LIST) {//分类列表
            try {
                List<HomeClassifyBean> datas = new Gson().fromJson(formatData.toString(), new TypeToken<List<HomeClassifyBean>>(){}.getType());
                UiUtils.handleClassifyView(context, ll_classify, datas);
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_HOME_SLIDE) {//幻灯片
            try {
                List<SlideBean> datas = new Gson().fromJson(formatData.toString(), new TypeToken<List<SlideBean>>(){}.getType());
                if (datas!=null&&datas.size()>0) {
                    dcv.setDrawCricle(datas.size(), 0, 0, 0 );
                    pagerAdapter.update(datas);
                    indexBanner = datas.size();
                    pager.setCurrentItem(datas.size());
                }
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_ADVERT_INDEX) {
            try {
                mAdvertBean = (AdvertBean) formatData;
                if (mAdvertBean.getThumb()!=null) {
                    findViewById(R.id.line_adv).setVisibility(View.VISIBLE);
                    iv_adv.setVisibility(View.VISIBLE);
                    Glide.with(context).load(mAdvertBean.getThumb()+"").crossFade().into(iv_adv);
                } else {
                    findViewById(R.id.line_adv).setVisibility(View.GONE);
                    iv_adv.setVisibility(View.GONE);
                }
            } catch (Exception e){
                BLog.e(e);
            }
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

    private static class MyHandler extends Handler {

        private WeakReference<HomeFragment> weak;

        public MyHandler(HomeFragment fragment) {
            weak = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weak.get()!=null) {
                if (msg.what == CHANGE_BANNER) {
                    weak.get().changeBanner();
                }
            }
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        private List<SlideBean> slides;

        public MyPagerAdapter(List<SlideBean> slides) {
            this.slides = slides;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_cover,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_cover);
//            imageView.setImageDrawable(getResources().getDrawable(args[position%args.length]));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context).load(slides.get(position%slides.size()).getPic()).crossFade().into(imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiUtils.handleSlide(context, slides.get(position%slides.size()));

                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        public int getListSize() {
            return slides.size();
        }

        public void update(List<SlideBean> slides) {
            this.slides = slides;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (slides==null||slides.size()==0) return 0;
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler!=null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
