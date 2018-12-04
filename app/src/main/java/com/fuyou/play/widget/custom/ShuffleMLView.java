package com.fuyou.play.widget.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fuyou.play.R;

/**
 * Created by Administrator on 2018/5/22 0022.
 */

public class ShuffleMLView extends FrameLayout{

    private ImageView[] views;
    private ImageView[] removeViews;
    private ImageView[] translateViews;
    private ShuffleListener listener;
    public static final int TYPE_FIRST = 1;
    public static final int TYPE_SECOND = 2;
    public static final int TYPE_CANCEL_HINT = 3;
    public static final int TYPE_END = 4;
    public boolean isShow = false;
    private int mWidth = 1080;

    private int START_OFFSET = 200;
    private int TRANSLATE_TIME_SHORT = 600;
    private int TRANSLATE_TIME_LONG = 750;

    public boolean isShow() {
        return isShow;
    }

    public void setShuffleListener(ShuffleListener listener) {
        this.listener = listener;
    }

    public ShuffleMLView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        init();
    }

    public ShuffleMLView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }

    private float cw,ch;
    private int r = 432;
    private int rw = 354;
    private int rh = 354;
    private float rwf = 1.5f;
    private float rwh = 1.5f;
    private float space = 20;
    public void init(int width, int height, int cw, int ch){
        mWidth = width;
        this.cw = cw;
        this.ch = ch;
        this.r = (mWidth-cw)/2;
        this.rw = r-40;
        this.cw = cw;
        this.rwf = rw/this.cw;
        this.rh = (height-ch)/2-40;
        this.ch = ch;
        this.rwh = rh/this.ch;
        this.space = rw/11;
//        LogCustom.show("w:"+mWidth+",cw:"+cw+",ch:"+ch+",r:"+r);
//        LogCustom.show("rw:"+rw+",rwf:"+rwf+",rh:"+rh+",rwh:"+rwh);
        isShow = true;
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER
        );
        setLayoutParams(params);
        views = new ImageView[22];
        for (int i=0;i<22;i++) {
            ImageView v = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.view_shuffle_img, this, false);
//            ImageView v = new ImageView(getContext());
            v.setImageResource(R.mipmap.tarot_choose_card);
            views[i] = v;
            addView(v);
        }
    }

    private float[] rotates;
    private float[] rotateY;
    private float[] TransX;
    private float[] TransY;
    public void shuffle(boolean top){
        for (int i=0;i<views.length;i++) {
            TranslateAnimation a = new TranslateAnimation(0, getRandom2(), 0, getRandom2());
            a.setStartOffset(0);
            a.setDuration(240);
            a.setFillAfter(true);
            a.setFillBefore(true);
            a.setRepeatCount(1);
            a.setRepeatMode(Animation.REVERSE);
            views[i].setAnimation(a);
            if (i==views.length-1) {
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!isShow) return;
                        shuffle(1);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
            a.start();
        }
    }

    private void shuffle(int tag) {
        if (tag==1){
            rotates = new float[views.length];
            for (int i=0;i<views.length;i++) {
                rotates[i] = getRandomRotate();
                RotateAnimation b = new RotateAnimation(0, rotates[i],Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                b.setStartOffset(240);
                b.setDuration(480);
                b.setFillAfter(true);
                b.setFillBefore(false);
                b.setInterpolator(new LinearInterpolator());
                views[i].setAnimation(b);
                if (i==views.length-1) {
                    b.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (!isShow) return;
                            shuffle(2);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                }
                b.start();
            }
        } else if (tag==2){
            TransX = new float[views.length];
            TransY = new float[views.length];
            for (int i=0;i<views.length;i++) {
                TransX[i] = getRandom();
                TransY[i] = getRandom();
                TranslateAnimation c = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,
                        rwf, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, 0);
//                TranslateAnimation c = new TranslateAnimation(0, TransX[i], 0, TransY[i]);
                c.setStartOffset(240);
                c.setDuration(1000);
                c.setFillAfter(true);
                c.setFillBefore(false);
                /*c.setRepeatCount(1);
                c.setRepeatMode(Animation.REVERSE);*/
                c.setInterpolator(new LinearInterpolator());
                views[i].setAnimation(c);
                if (i==views.length-1) {
                    c.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (!isShow) return;
                            shuffle(3);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                }
                c.start();
            }
        } else if (tag==3) {
            for (int i=0;i<views.length;i++) {
                RotateAnimation b = new RotateAnimation( rotates[i],0,Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                b.setStartOffset(240);
                b.setDuration(480);
                b.setFillAfter(true);
                b.setFillBefore(true);
                b.setInterpolator(new LinearInterpolator());
                views[i].setAnimation(b);
                if (i==views.length-1) {
                    b.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (!isShow) return;
                            if (listener!=null) {
                                listener.onShuffleListener(TYPE_FIRST);
                            }
                            toShuffleSecond();
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                }
                b.start();
            }
        }
    }

    public void shuffle() {
        float[] xx = new float[22];
        float[] yy = new float[22];
        /*xx[i] = r*(float)Math.cos(Math.PI/180 * (i*18));
        yy[i] = r*(float)Math.sin(Math.PI/180 * (i*18));*/
        /*point_x = (radius*3/5 - 30 - radius/10) *(float)Math.cos(Math.PI/180 * (planetOffset+180));
        point_y = (radius*3/5 - 32 - radius/10) *(float)Math.sin(Math.PI/180 * (planetOffset+180));*/
        for (int i=0;i<views.length;i++) {
            AnimationSet set = new AnimationSet(true);
            float x = getRandom2();
            float y = getRandom2();
            TranslateAnimation a = new TranslateAnimation(0, x, 0, y);
            a.setStartOffset(0);
            a.setDuration(240);
            a.setFillAfter(true);
            a.setFillBefore(true);
            a.setRepeatCount(1);
            a.setRepeatMode(Animation.REVERSE);
            set.addAnimation(a);
            RotateAnimation bb = new RotateAnimation(0, getRandomRotate360(),Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            bb.setStartOffset(240);
            bb.setDuration(360);
            bb.setFillAfter(true);
            bb.setFillBefore(true);
            bb.setRepeatCount(1);
            bb.setRepeatMode(Animation.REVERSE);
            set.addAnimation(bb);
            RotateAnimation b = new RotateAnimation(0, getRandomRotate(),Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            b.setStartOffset(960);
            b.setDuration(1000);
            b.setFillAfter(true);
            b.setFillBefore(true);
            b.setRepeatCount(1);
            b.setRepeatMode(Animation.REVERSE);
            set.addAnimation(b);
            float xxx = getRandom();
            float yyy = getRandom();
            xx[i] = xxx;
            yy[i] = yyy;
            TranslateAnimation c = new TranslateAnimation(0, xxx, 0, yyy);
            c.setStartOffset(960);
            c.setDuration(1000);
            c.setFillAfter(true);
            c.setFillBefore(true);
            c.setRepeatCount(1);
            c.setRepeatMode(Animation.REVERSE);
            set.addAnimation(c);
            /*RotateAnimation bbb = new RotateAnimation(0, getRandomRotate360(),Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            bbb.setStartOffset(1800);
            bbb.setDuration(480);
            bbb.setFillAfter(true);
            bbb.setFillBefore(true);
            bbb.setRepeatCount(1);
            bbb.setRepeatMode(Animation.REVERSE);
            set.addAnimation(bbb);*/
            views[i].setAnimation(set);
            set.setInterpolator(new LinearInterpolator());
            set.setFillAfter(true);
            set.setFillBefore(true);
            set.setRepeatCount(1);
            set.setRepeatMode(Animation.REVERSE);

            if (i==views.length-1) {
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!isShow) return;
                        if (listener!=null) {
                            listener.onShuffleListener(TYPE_FIRST);
                        }
                        toShuffleSecond();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
            set.start();
        }
//        LogCustom.show(Arrays.toString(xx));
//        LogCustom.show(Arrays.toString(yy));
    }

    private void toShuffleSecond() {
        if (!isShow) return;

        TranslateAnimation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,
                    rwf, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, 0);
        a.setStartOffset(START_OFFSET);
        a.setDuration(TRANSLATE_TIME_SHORT);
        a.setInterpolator(new LinearInterpolator());
        a.setFillAfter(true);
        a.setFillBefore(true);
        for (int i=8;i<15;i++) {
            views[i].setAnimation(a);
        }
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isShow) return;
                toShuffleTranslate(1);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        a.start();
    }

    private void toShuffleTranslate(int type) {
        if (!isShow) return;
        if (type==1) {
            TranslateAnimation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,
                    0-rwf, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, 0);
            a.setStartOffset(START_OFFSET);
            a.setDuration(TRANSLATE_TIME_SHORT);
            a.setInterpolator(new LinearInterpolator());
            a.setFillAfter(true);
            a.setFillBefore(true);
            for (int i=15;i<views.length;i++) {
                views[i].setAnimation(a);
            }
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!isShow) return;
                    toShuffleTranslate(2);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            a.start();
        } else if (type==2){
            TranslateAnimation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,
                    0-rwf, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, 0);
            a.setStartOffset(START_OFFSET);
            a.setDuration(TRANSLATE_TIME_SHORT);
            a.setInterpolator(new LinearInterpolator());
            a.setFillAfter(true);
            a.setFillBefore(true);
            for (int i=0;i<12;i++) {
                views[i].setAnimation(a);
            }
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!isShow) return;
                    toShuffleTranslate(3);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            a.start();
        } else if (type==3){
            TranslateAnimation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0-rwf,Animation.RELATIVE_TO_SELF,
                    rwf, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, 0);
            a.setStartOffset(START_OFFSET);
            a.setDuration(TRANSLATE_TIME_LONG);
            a.setInterpolator(new LinearInterpolator());
            a.setFillAfter(true);
            a.setFillBefore(true);
            for (int i=0;i<8;i++) {
                views[i].setAnimation(a);
            }
            for (int i=15;i<views.length;i++) {
                views[i].setAnimation(a);
            }
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!isShow) return;
                    toShuffleTranslate(4);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            a.start();
        } else if (type==4) {
            TranslateAnimation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF,rwf,Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, 0);
            a.setStartOffset(START_OFFSET);
            a.setDuration(TRANSLATE_TIME_SHORT);
            a.setInterpolator(new LinearInterpolator());
            a.setFillAfter(true);
            a.setFillBefore(true);
            for (int i=0;i<views.length;i++) {
                views[i].setAnimation(a);
            }
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!isShow) return;
                    if (listener!=null) {
                        listener.onShuffleListener(TYPE_CANCEL_HINT);
                    }
                    toShuffleTranslate(5);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            a.start();
        } else if (type==5) {
            TranslateAnimation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, rwh);
            a.setStartOffset(START_OFFSET);
            a.setDuration(TRANSLATE_TIME_SHORT);
            a.setInterpolator(new LinearInterpolator());
            a.setFillAfter(true);
            a.setFillBefore(true);
            for (int i=0;i<views.length;i++) {
                views[i].setAnimation(a);
            }
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!isShow) return;
                    toShuffleTranslate(6);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            a.start();
            /*for (int i=0;i<views.length;i++) {
                AnimationSet set = new AnimationSet(true);
                RotateAnimation b = new RotateAnimation(0, i*15, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.2f);
                b.setStartOffset(400);
                b.setDuration(1000);
                set.addAnimation(b);
                views[i].setAnimation(set);
                set.setInterpolator(new LinearInterpolator());
                set.setFillAfter(true);
                set.setFillBefore(true);
                if (i==21) {
                    set.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (!isShow) return;
                            if (listener!=null) {
                                listener.onShuffleListener(TYPE_SECOND);
                            }
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                }
                set.start();
            }*/
        } else if (type==6) {
            for (int i=0;i<views.length;i++) {
                /*TranslateAnimation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,
                        0, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, rwh);*/
                TranslateAnimation a = new TranslateAnimation(0, (i-11)*space, rwh*ch, rwh*ch);
                a.setStartOffset(START_OFFSET);
                a.setDuration(TRANSLATE_TIME_SHORT);
                a.setInterpolator(new LinearInterpolator());
                a.setFillAfter(true);
                a.setFillBefore(true);
                views[i].setAnimation(a);
                final boolean isEnd = i==views.length-1;
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!isShow) return;
                        if (listener!=null&&isEnd) {
                            listener.onShuffleListener(TYPE_SECOND);
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                a.start();
            }
        }
    }

    private int widthMeasureSpec,heightMeasureSpec;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        LogCustom.show("onMeasure, widthMeasureSpec: "+widthMeasureSpec+",heightMeasureSpec:"+heightMeasureSpec);
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        LogCustom.show("onWindowFocusChanged, hasWindowFocus: "+hasWindowFocus);
        super.onWindowFocusChanged(hasWindowFocus);
    }

    private int left,top,right,bottom;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        LogCustom.show("onLayout, changed: "+changed+"left top right bottom"+left+""+top+""+right+""+bottom+"");
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void requestLayout() {
//        LogCustom.show("requestLayout,: ");
        super.requestLayout();
    }


    public void moveResultCard(int num) {
//        LogCustom.show("moveResultCard"+num);
        try {
            requestFocus();
            requestLayout();
            layout(left, top, right, bottom);
            onWindowFocusChanged(true);
        } catch (Exception e){

        }
        int[] oldNums = new int[]{-1,-1,-1};
        if (num==2) num=1;
        for (int i=0;i<num;i++) {
            int randomNum = getRandomNum(oldNums);
            oldNums[i] = randomNum;
            AnimationSet set = new AnimationSet(true);
            TranslateAnimation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF,(randomNum-11)*space/rw,Animation.RELATIVE_TO_SELF,
                    (randomNum-11)*space/rw, Animation.RELATIVE_TO_SELF,rwh,Animation.RELATIVE_TO_SELF, 0 - (ch/2)/rh);
//            TranslateAnimation a = new TranslateAnimation((randomNum-17)*space, (randomNum-17)*space, rwh*ch, ch/2);
            a.setStartOffset(START_OFFSET);
            a.setDuration(TRANSLATE_TIME_SHORT+200);
            set.addAnimation(a);
            set.setInterpolator(new LinearInterpolator());
            set.setFillAfter(true);
            set.setFillBefore(true);
            views[randomNum].setAnimation(set);
            final boolean isEnd = i==num-1;
            set.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!isShow) return;
                    if (listener!=null&&isEnd) {
                        listener.onShuffleListener(TYPE_END);
                    }
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            set.start();
        }
    }

    private int getRandomNum(int[] oldNums) {
        int randomNum = (int) (Math.random()*21);
        while (isHasNum(oldNums, randomNum)) {
            randomNum = (int) (Math.random()*21);
        }
        return randomNum;
    }

    private boolean isHasNum(int[] oldNums, int randomNum){
        boolean result = false;
        for (int i:oldNums) {
            if (i==randomNum){
                result = true;
                break;
            }
        }
        return result;
    }

    private float getRandom(){
        float temp = (mWidth-ch)/2 + 10;
        double t = (Math.random() * 2 * temp);
        if (t>temp) {
            t = temp - t;
        }
        return (float) t;
    }

    private float getRandom2(){
        float mr = r/5;
        double t = (Math.random() * 2 * mr);
        if (t>mr) {
            t = mr - t;
        }
        return (float) t;
    }
    private float getRandomRotate(){
        int t = (int)(Math.random() * 180);
        if (t>90) {
            t = 90 - t;
        }
        return t;
    }

    private float getRandomRotate360(){
        int t = (int)(Math.random() * 720);
        if (t>360) {
            t = 360 - t;
        }
        return t;
    }

    public void onDismiss(){
        isShow = false;
    }

    public interface ShuffleListener {
        void onShuffleListener(int type);
    }

}
