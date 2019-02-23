package com.fuyou.play.widget.custom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.Scroller;

/***
 * Created by Administrator on 2016/4/15.
 *
 *
 * @author wwei
 */
public class CustomScrollView extends ScrollView {

    Context mContext;
    private View mView;
    private Rect mRect = new Rect();
    private float touchY;

    private Scroller scroller = null;

    static final private int DONE = 3;

    private int MAX_SCROLL_HEIGHT = 400;// 最大滑动距离
    private static final float SCROLL_RATIO = 0.8f;// 阻尼系数

    private final static int SCROLL_DURATION = 400;
    private final static float OFFSET_RADIO = 1.8f;
//    private int headerHeight = 0;
    private boolean enableRefresh = true;
    private boolean refreshing = false;
    private boolean move = true;

    public void setMove(boolean move){
        this.move = move;
    }



    public CustomScrollView(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setMaxScrollHeight(int msh) {
        MAX_SCROLL_HEIGHT = msh;
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;

    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            this.mView = getChildAt(0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
            startMove();
            touchY = arg0.getY();
        }else if (arg0.getAction() == MotionEvent.ACTION_CANCEL){
            stopMove();
        }



        return super.onInterceptTouchEvent(arg0);
    }

    private int beginY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mView == null) {

            return super.onTouchEvent(ev);
        } else {
            commonOnTouchEvent(ev);
        }

        return super.onTouchEvent(ev);
    }

    private void commonOnTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                touchY = ev.getY();
                beginY = (int) ((int) ev.getY() + getScrollY()*1.5);
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animation();
                }else {
                    stopMove();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = touchY;
                float nowY = ev.getY();
                int deltaY = (int) (preY - nowY);
                touchY = nowY;
                if (isNeedMove()) {
                    if (mRect.isEmpty()) {
                        mRect.set(mView.getLeft(), mView.getTop(), mView.getRight(), mView.getBottom());
                    }
                    int offset = mView.getTop() - deltaY;

                    if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                        mView.layout(mView.getLeft(), mView.getTop() - (int) (deltaY * SCROLL_RATIO), mView.getRight(), mView.getBottom()
                                - (int) (deltaY * SCROLL_RATIO));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void startMove(){
        if (mScroll!=null) {
            mScroll.forScrollCustomChange(false);
        }
    }

    private boolean isNeedMove() {
        int viewHight = mView.getMeasuredHeight();
        int srollHight = getHeight();
        int offset = viewHight - srollHight;
        int scrollY = getScrollY();

        if ((scrollY == 0 || scrollY == offset)&&move) {
            return true;
        }
        return false;
    }

    private boolean isNeedAnimation() {
        return !mRect.isEmpty();
    }

    private void animation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, mView.getTop(), mRect.top);
        ta.setDuration(200);
        mView.startAnimation(ta);
        mView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        mRect.setEmpty();
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                stopMove();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private  onScrollCustomChange mScroll;

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        if (clampedX||clampedY){
//            stopMove();
        }

    }



    private void stopMove(){
        if (mScroll!=null){
            mScroll.forScrollCustomChange(true);
        }
    }

    public interface onScrollCustomChange{
        void forScrollCustomChange(boolean fit);
    }

    private OnScrollListener onScrollListener;

    public interface OnScrollListener{
        public void onScroll(int x, int y, int oldx, int oldy);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(onScrollListener!=null){
            onScrollListener.onScroll(l,t,oldl,oldt);
        }
    }




}