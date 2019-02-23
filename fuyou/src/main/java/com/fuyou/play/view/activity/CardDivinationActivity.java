package com.fuyou.play.view.activity;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.fuyou.play.R;
import com.fuyou.play.bean.CardArrayEntity;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.util.DensityUtils;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.util.UiUtils;
import com.fuyou.play.util.shake.MySensorEventListener;
import com.fuyou.play.util.shake.Shakeable;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.custom.Rotatable;
import com.fuyou.play.widget.custom.ShuffleMLView;
import com.nineoldandroids.view.ViewHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/23 0023.
 * 占卜页面
 *
 * @author wwei
 */
public class CardDivinationActivity extends BaseActivity implements HttpRepListener, Shakeable {

    private View iv_back_base;
    private TextView tv_title, tv_hint;
    private ShuffleMLView smv;
    private List<CardArrayEntity> list;
    private int mItemWidth;
    private int mItemHeight;
    private int next = 0;
    private MyHandler mHandler;
    private boolean cancelAble = false;
    private boolean shakeAble = false;
    private int state = 0;
    private int type;
    private int mNum;
    //传感器管理器
    private SensorManager manager;
    //传感器监听器
    private MySensorEventListener listener;
    //震动服务
    private Vibrator mVibrator;
    private SoundPool mSndPool;
    private HashMap<Integer, Integer> mSoundPoolMap = new HashMap<Integer, Integer>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_card_divination);
        initStatusBar(findViewById(R.id.status_bar));
        mHandler = new MyHandler(this);
        manager = (SensorManager) this.getSystemService(Service.SENSOR_SERVICE);
        listener = new MySensorEventListener(this);
        // 获取震动服务
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        loadSound();
        initDensity();
        initViews();
        initData();
    }

    private int sw;
    private int sh;
    private void initDensity(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        sw = width;
        sh = metric.heightPixels;
        sh = sh - DensityUtils.dp2px(this, 44) - getStatusBarHeight();
        mItemWidth = (width- DensityUtils.dp2px(this, 66))/3 - DensityUtils.dp2px(this, 6);
        mItemHeight = mItemWidth*(162)/98;
    }

    private void initViews() {
        try {
            iv_back_base = findViewById(R.id.iv_back_base);
            iv_back_base.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            iv_back_base.setVisibility(View.GONE);
            tv_title = findViewById(R.id.tv_title_base);
            tv_hint = findViewById(R.id.tv_hint);
            smv = findViewById(R.id.smv);
            smv.init(sw, sh, DensityUtils.dp2px(this, 86), DensityUtils.dp2px(this, 140));
            smv.setShuffleListener(new ShuffleMLView.ShuffleListener() {
                @Override
                public void onShuffleListener(int type) {
                    state = type;
//                    LogCustom.show("type:"+type);
                    if (type==ShuffleMLView.TYPE_FIRST) {
                        tv_hint.setText(getString(R.string.divination_hint_text_2));
                    } else if (type ==ShuffleMLView.TYPE_CANCEL_HINT) {
                        tv_hint.setVisibility(View.GONE);

                    } else if (type==ShuffleMLView.TYPE_SECOND){
                        findViewById(R.id.tv_middle).setVisibility(View.VISIBLE);
                        shakeAble = true;

                        /////////
                        smv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chooseCard(1);
                            }
                        });

                    } else if (type==ShuffleMLView.TYPE_END){
                        toNextStep();
                    }
                }
            });
            smv.shuffle();
            smv.setVisibility(View.VISIBLE);
            tv_title.setText(getString(R.string.title_divination_type_1));
            tv_hint.setText(getString(R.string.divination_hint_text_1));
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    //洗牌动作完成，并执行下一步
    private void toNextStep(){
        AlphaAnimation a = new AlphaAnimation(1, 0);
        a.setStartOffset(500);
        a.setDuration(500);
        smv.setAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                showResultView();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        smv.setVisibility(View.GONE);
        a.start();
    }

    //显示卡牌
    private void showResultView(){
        if (mBaseExit) return;
        if (next==1){
            AlphaAnimation a = new AlphaAnimation(0, 1f);
            a.setStartOffset(500);
            a.setDuration(500);
            v_result.setAnimation(a);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    showResultAnim();
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            v_result.setVisibility(View.VISIBLE);
            a.start();
        } else if (next==2) {
            cancelAble = true;
            showBackView();
        } else {
            cancelAble = true;
            showBackView();
        }
    }

    //改变视角距离, 贴近屏幕
    private void setCameraDistance(View v) {
        int distance = 10000;
        float scale = getResources().getDisplayMetrics().density * distance;
        v.setCameraDistance(scale);
    }

    //执行卡牌展示动画
    private void showResultAnim(){
        if (mBaseExit) return;
        try {
            if (count==1){
                if (views!=null&&views.size()!=0){
                    ViewHelper.setRotationY(views_real.get(0), 180f);
                    Rotatable rotatable = new Rotatable.Builder(views.get(0))
                            .sides(views_back.get(0), views_real.get(0))
                            .direction(Rotatable.ROTATE_Y)
                            .rotationCount(1)
                            .build();
                    rotatable.setTouchEnable(false);
                    rotatable.rotate(Rotatable.ROTATE_Y, -180, 1500);
                }
            } else {
                if (views!=null&&views.size()!=0){
                    for (int i=0;i<3;i++){
                        ViewHelper.setRotationY(views_real.get(i), 180f);
                        Rotatable rotatable = new Rotatable.Builder(views.get(i))
                                .sides(views_back.get(i), views_real.get(i))
                                .direction(Rotatable.ROTATE_Y)
                                .rotationCount(1)
                                .build();
                        rotatable.setTouchEnable(false);
                        rotatable.rotate(Rotatable.ROTATE_Y, -180, 1500);
                    }
                }
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mBaseExit) {
                    cancelAble = true;
                    mHandler.sendEmptyMessage(1);
                }
            }
        }, 1500);
    }

    private void initData() {
        initResult(3);
    }

    private int count = 0;
    private View v1,v2,v3,v_result;
    //设置请求卡牌结果,分1张或3张
    private void initResult(int count){
        this.count = count;
        v_result = findViewById(R.id.v_result);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        views = new ArrayList<>();
        views_back = new ArrayList<>();
        views_real = new ArrayList<>();
        list = new ArrayList<>();
        list.add(new CardArrayEntity());
        list.add(new CardArrayEntity());
        list.add(new CardArrayEntity());
        if (count==1) {
            settingResult(v2, list.get(0), 0);
        } else {
            settingResult(v1, list.get(0), 0);
            settingResult(v2, list.get(1), 1);
            settingResult(v3, list.get(2), 2);
            v1.setVisibility(View.VISIBLE);
            v3.setVisibility(View.VISIBLE);
        }
        next = 1;
    }

    private List<View> views;
    private List<View> views_back;
    private List<View> views_real;
    //设置卡牌正反面和点击事件
    private void settingResult(View v, final CardArrayEntity entity, final int pos){
        try {
            ImageView iv_back = v.findViewById(R.id.iv_back);
            ImageView iv_real = v.findViewById(R.id.iv_real);
            setViewWH(iv_back, mItemWidth, mItemHeight);
            setViewWH(iv_real, mItemWidth, mItemHeight);
            TextView tv = v.findViewById(R.id.tv);
            Glide.with(this).load(R.mipmap.tarot_choose_card)
                    .transform(new CenterCrop(this), UiUtils.getGideTransformation(this))
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_back);
            Glide.with(this).load(R.mipmap.tarot_choose_card)
                    .transform(new CenterCrop(this), UiUtils.getGideTransformation(this))
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_real);
            /*if (!TextUtils.isEmpty(entity.getSite())) {
                tv.setText(entity.getSite());
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.GONE);
            }*/
//            tv.setText(TextUtils.isEmpty(entity.getCard_name())?"":entity.getCard_name());
            views.add(v.findViewById(R.id.v_result_img));
            setCameraDistance(v.findViewById(R.id.v_result_img));
            v.findViewById(R.id.v_result_img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCardDetail(pos);
                }
            });
            views_back.add(iv_back);
            views_real.add(iv_real);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "settingResult CoolingResult");
        }
    }

    private void toCardDetail(int pos) {
        /*Intent intent = new Intent(CardDivinationActivity.this, CardDetailActivity.class);
        ArrayList<CardDetailEntity> temp = new ArrayList<>();
        for (int i=0;i<list.size();i++) {
            CardDetailEntity item = new CardDetailEntity();
            item.setDescribe(list.get(i).getDescribe());
            item.setImg_url(list.get(i).getImg_url());
            item.setName(list.get(i).getCard_name());
            temp.add(item);
        }
        intent.putExtra("CardDetailPosition", pos);
        intent.putExtra("CardDetailList", temp);
        intent.putExtra("CardDetailType", true);
        intent.putExtra("FromWhere", "divination");
        startActivity(intent);
        finish();*/
    }

    private void showBackView() {
        try {
            AlphaAnimation a = new AlphaAnimation(0, 1);
            a.setDuration(300);
            a.setFillAfter(true);
            iv_back_base.setAnimation(a);
            iv_back_base.setVisibility(View.VISIBLE);
            a.start();
            tv_hint.setText(getString(R.string.divination_click_2read));
            tv_hint.setVisibility(View.VISIBLE);
            tv_hint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCardDetail(0);
                }
            });

        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    private void setViewWH(View v, int w, int h){
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.width = w;
        params.height = h;
        v.setLayoutParams(params);
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {

    }

    @Override
    public void showLoading(int flag, Object obj) {

    }

    @Override
    public void hideLoading(int flag, Object obj) {

    }

    @Override
    public void showError(int flag, Object obj, int errorType) {
        next = 2;
    }

    @Override
    public void onShake(Object... objs) {
        try {
            if (shakeAble&&!mPause) {
                chooseCard(0);
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    //选牌

    /***
     *
     * @param fromShake 0表示摇一摇有声音 1表示点击没有声音
     */
    private void chooseCard(int fromShake) {
        try {
            if (shakeAble&&!mPause) {
                shakeAble = false;
                if(fromShake==0) {//震动
                    if (mVibrator != null) {
                        mVibrator.vibrate(500);
                    }
                }
                manager.unregisterListener(listener);
                AlphaAnimation a = new AlphaAnimation(1f, 0);
                a.setStartOffset(150);
                a.setDuration(450);
                findViewById(R.id.tv_middle).setAnimation(a);
                findViewById(R.id.tv_middle).setVisibility(View.GONE);
                a.start();
                if(fromShake==0){//声音
                    try {
                        mSndPool.play(mSoundPoolMap.get(0), (float) 0.2, (float) 0.2, 0,0, (float) 0.6);
                    } catch (Exception e){
                        ExceptionUtils.ExceptionSend(e);
                    }
                }
                if (next==1) {//请求成功
                    if (smv!=null&&list!=null&&list.size()>0) {
                        smv.moveResultCard(list.size());
                    }
                } else if (next==2) {//请求失败

                } else {//未知错误

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 加载音效
    @SuppressWarnings("deprecation")
    private void loadSound() {
        mSndPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        new Thread() {
            public void run() {
                mSoundPoolMap.put(0, mSndPool.load(CardDivinationActivity.this, R.raw.shake, 1));
//                mSoundPoolMap.put(1, mSndPool.load(CardDivinationActivity.this, R.raw.shake_match, 1));
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        if (cancelAble) {
//            startActivity(CoolingResultActivity.class);
            finish();
        }else {
            if (state>=2) {
                showToast(state==2?"您还没有选择卡牌":"正在选择卡牌");
            } else {
                showToast("正在洗牌中");
            }
        }
    }

    private boolean mPause = false;
    @Override
    protected void onResume() {
        super.onResume();
        LogCustom.show("onResume");
        mPause = false;
        try {
            if (!shakeAble) {
                LogCustom.show("onResume shakeAble");
                manager.registerListener(listener,
                        manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPause = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            LogCustom.show("onDestroy");
            if (smv!=null) smv.onDismiss();
            manager.unregisterListener(listener);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<CardDivinationActivity> weakReference;

        public  MyHandler(CardDivinationActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        private CardDivinationActivity activity;

        @Override
        public void handleMessage(Message msg) {
            activity = weakReference.get();
            if (msg.what==1) {
                activity.showBackView();
            }
        }
    }
}
