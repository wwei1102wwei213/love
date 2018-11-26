package com.fuyou.play.widget.pullwidget.pullrefresh;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.fuyou.play.widget.pullwidget.pullrefresh.ILoadingLayout.State;

public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {
    private static final float OFFSET_RADIO = 2.5f;
    private static final int SCROLL_DURATION = 150;
    private int mFooterHeight;
    private LoadingLayout mFooterLayout;
    private int mHeaderHeight;
    private LoadingLayout mHeaderLayout;
    private boolean mInterceptEventEnable = true;
    private boolean mIsHandledTouchEvent = false;
    private float mLastMotionY = -1.0f;
    private State mPullDownState = State.NONE;
    private boolean mPullLoadEnabled = false;
    private boolean mPullRefreshEnabled = true;
    private State mPullUpState = State.NONE;
    private OnRefreshListener<T> mRefreshListener;
    T mRefreshableView;
    private FrameLayout mRefreshableViewWrapper;
    private boolean mScrollLoadEnabled = false;
    private SmoothScrollRunnable mSmoothScrollRunnable;
    private int mTouchSlop;

    public interface OnRefreshListener<V extends View> {
        void onPullDownToRefresh(PullToRefreshBase<V> pullToRefreshBase);

        void onPullUpToRefresh(PullToRefreshBase<V> pullToRefreshBase);
    }

    final class SmoothScrollRunnable implements Runnable {
        private boolean mContinueRunning = true;
        private int mCurrentY = -1;
        private final long mDuration;
        private final Interpolator mInterpolator;
        private final int mScrollFromY;
        private final int mScrollToY;
        private long mStartTime = -1;

        public SmoothScrollRunnable(int fromY, int toY, long duration) {
            this.mScrollFromY = fromY;
            this.mScrollToY = toY;
            this.mDuration = duration;
            this.mInterpolator = new DecelerateInterpolator();
        }

        public void run() {
            if (this.mDuration <= 0) {
                PullToRefreshBase.this.setScrollTo(0, this.mScrollToY);
                return;
            }
            if (this.mStartTime == -1) {
                this.mStartTime = System.currentTimeMillis();
            } else {
                this.mCurrentY = this.mScrollFromY - Math.round(((float) (this.mScrollFromY - this.mScrollToY)) * this.mInterpolator.getInterpolation(((float) Math.max(Math.min((1000 * (System.currentTimeMillis() - this.mStartTime)) / this.mDuration, 1000), 0)) / 1000.0f));
                PullToRefreshBase.this.setScrollTo(0, this.mCurrentY);
            }
            if (this.mContinueRunning && this.mScrollToY != this.mCurrentY) {
                PullToRefreshBase.this.postDelayed(this, 16);
            }
        }

        public void stop() {
            this.mContinueRunning = false;
            PullToRefreshBase.this.removeCallbacks(this);
        }
    }

    protected abstract T createRefreshableView(Context context, AttributeSet attributeSet);

    protected abstract boolean isReadyForPullDown();

    protected abstract boolean isReadyForPullUp();

    public PullToRefreshBase(Context context) {
        super(context);
        init(context, null);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mHeaderLayout = createHeaderLoadingLayout(context, attrs);
        this.mFooterLayout = createFooterLoadingLayout(context, attrs);
        this.mRefreshableView = createRefreshableView(context, attrs);
        if (this.mRefreshableView == null) {
            throw new NullPointerException("Refreshable view can not be null.");
        }
        addRefreshableView(context, this.mRefreshableView);
        addHeaderAndFooter(context);
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                PullToRefreshBase.this.refreshLoadingViewsSize();
                if (VERSION.SDK_INT >= 16) {
                    PullToRefreshBase.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    PullToRefreshBase.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void refreshLoadingViewsSize() {
        int headerHeight;
        int footerHeight;
        if (this.mHeaderLayout != null) {
            headerHeight = this.mHeaderLayout.getContentSize();
        } else {
            headerHeight = 0;
        }
        if (this.mFooterLayout != null) {
            footerHeight = this.mFooterLayout.getContentSize();
        } else {
            footerHeight = 0;
        }
        if (headerHeight < 0) {
            headerHeight = 0;
        }
        if (footerHeight < 0) {
            footerHeight = 0;
        }
        this.mHeaderHeight = headerHeight;
        this.mFooterHeight = footerHeight;
        if (this.mHeaderLayout != null) {
            headerHeight = this.mHeaderLayout.getMeasuredHeight();
        } else {
            headerHeight = 0;
        }
        if (this.mFooterLayout != null) {
            footerHeight = this.mFooterLayout.getMeasuredHeight();
        } else {
            footerHeight = 0;
        }
        if (footerHeight == 0) {
            footerHeight = this.mFooterHeight;
        }
        int pLeft = getPaddingLeft();
        int pTop = getPaddingTop();
        int pRight = getPaddingRight();
        int pBottom = getPaddingBottom();
        setPadding(pLeft, -headerHeight, pRight, -footerHeight);
    }

    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refreshLoadingViewsSize();
        refreshRefreshableViewSize(w, h);
        post(new Runnable() {
            public void run() {
                PullToRefreshBase.this.requestLayout();
            }
        });
    }

    public void setOrientation(int orientation) {
        if (1 != orientation) {
            throw new IllegalArgumentException("This class only supports VERTICAL orientation.");
        }
        super.setOrientation(orientation);
    }

    public final boolean onInterceptTouchEvent(MotionEvent event) {
        boolean z = false;
        if (!isInterceptTouchEventEnabled()) {
            return false;
        }
        if (!isPullLoadEnabled() && !isPullRefreshEnabled()) {
            return false;
        }
        int action = event.getAction();
        if (action == 3 || action == 1) {
            this.mIsHandledTouchEvent = false;
            return false;
        } else if (action != 0 && this.mIsHandledTouchEvent) {
            return true;
        } else {
            switch (action) {
                case 0:
                    this.mLastMotionY = event.getY();
                    this.mIsHandledTouchEvent = false;
                    break;
                case 2:
                    float deltaY = event.getY() - this.mLastMotionY;
                    if (Math.abs(deltaY) > ((float) this.mTouchSlop) || isPullRefreshing() || isPullLoading()) {
                        this.mLastMotionY = event.getY();
                        if (!isPullRefreshEnabled() || !isReadyForPullDown()) {
                            if (isPullLoadEnabled() && isReadyForPullUp()) {
                                if (Math.abs(getScrollYValue()) > 0 || deltaY < -0.5f) {
                                    z = true;
                                }
                                this.mIsHandledTouchEvent = z;
                                break;
                            }
                        }
                        if (Math.abs(getScrollYValue()) > 0 || deltaY > 0.5f) {
                            z = true;
                        }
                        this.mIsHandledTouchEvent = z;
                        if (this.mIsHandledTouchEvent) {
                            this.mRefreshableView.onTouchEvent(event);
                            break;
                        }
                    }
                    break;
            }
            return this.mIsHandledTouchEvent;
        }
    }

    public final boolean onTouchEvent(MotionEvent ev) {
        boolean handled = false;
        switch (ev.getAction()) {
            case 0:
                this.mLastMotionY = ev.getY();
                this.mIsHandledTouchEvent = false;
                return false;
            case 1:
            case 3:
                if (!this.mIsHandledTouchEvent) {
                    return false;
                }
                this.mIsHandledTouchEvent = false;
                if (isReadyForPullDown()) {
                    if (this.mPullRefreshEnabled && this.mPullDownState == State.RELEASE_TO_REFRESH) {
                        startRefreshing();
                        handled = true;
                    }
                    resetHeaderLayout();
                    return handled;
                } else if (!isReadyForPullUp()) {
                    return false;
                } else {
                    if (isPullLoadEnabled() && this.mPullUpState == State.RELEASE_TO_REFRESH) {
                        startLoading();
                        handled = true;
                    }
                    resetFooterLayout();
                    return handled;
                }
            case 2:
                float deltaY = ev.getY() - this.mLastMotionY;
                this.mLastMotionY = ev.getY();
                if (isPullRefreshEnabled() && isReadyForPullDown()) {
                    pullHeaderLayout(deltaY / OFFSET_RADIO);
                    return true;
                } else if (isPullLoadEnabled() && isReadyForPullUp()) {
                    pullFooterLayout(deltaY / OFFSET_RADIO);
                    return true;
                } else {
                    this.mIsHandledTouchEvent = false;
                    return false;
                }
            default:
                return false;
        }
    }

    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        this.mPullRefreshEnabled = pullRefreshEnabled;
    }

    public void setPullLoadEnabled(boolean pullLoadEnabled) {
        this.mPullLoadEnabled = pullLoadEnabled;
    }

    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        this.mScrollLoadEnabled = scrollLoadEnabled;
    }

    public boolean isPullRefreshEnabled() {
        return this.mPullRefreshEnabled && this.mHeaderLayout != null;
    }

    public boolean isPullLoadEnabled() {
        return this.mPullLoadEnabled && this.mFooterLayout != null;
    }

    public boolean isScrollLoadEnabled() {
        return this.mScrollLoadEnabled;
    }

    public void setOnRefreshListener(OnRefreshListener<T> refreshListener) {
        this.mRefreshListener = refreshListener;
    }

    public void onPullDownRefreshComplete() {
        if (isPullRefreshing()) {
            this.mPullDownState = State.RESET;
            onStateChanged(State.RESET, true);
            postDelayed(new Runnable() {
                public void run() {
                    PullToRefreshBase.this.setInterceptTouchEventEnabled(true);
                    PullToRefreshBase.this.mHeaderLayout.setState(State.RESET);
                }
            }, getSmoothScrollDuration());
            resetHeaderLayout();
            setInterceptTouchEventEnabled(false);
        }
    }

    public void onPullUpRefreshComplete() {
        if (isPullLoading()) {
            this.mPullUpState = State.RESET;
            onStateChanged(State.RESET, false);
            postDelayed(new Runnable() {
                public void run() {
                    PullToRefreshBase.this.setInterceptTouchEventEnabled(true);
                    PullToRefreshBase.this.mFooterLayout.setState(State.RESET);
                }
            }, getSmoothScrollDuration());
            resetFooterLayout();
            setInterceptTouchEventEnabled(false);
        }
    }

    public T getRefreshableView() {
        return this.mRefreshableView;
    }

    public LoadingLayout getHeaderLoadingLayout() {
        return this.mHeaderLayout;
    }

    public LoadingLayout getFooterLoadingLayout() {
        return this.mFooterLayout;
    }

    public void setLastUpdatedLabel(CharSequence label) {
        if (this.mHeaderLayout != null) {
            this.mHeaderLayout.setLastUpdatedLabel(label);
        }
        if (this.mFooterLayout != null) {
            this.mFooterLayout.setLastUpdatedLabel(label);
        }
    }

    public void doPullRefreshing(final boolean smoothScroll, long delayMillis) {
        postDelayed(new Runnable() {
            public void run() {
                int newScrollValue = -PullToRefreshBase.this.mHeaderHeight;
                int duration = smoothScroll ? PullToRefreshBase.SCROLL_DURATION : 0;
                PullToRefreshBase.this.startRefreshing();
                PullToRefreshBase.this.smoothScrollTo(newScrollValue, (long) duration, 0);
            }
        }, delayMillis);
    }

    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new HeaderLoadingLayout(context);
    }

    protected LoadingLayout createFooterLoadingLayout(Context context, AttributeSet attrs) {
        return new FooterLoadingLayout(context);
    }

    protected long getSmoothScrollDuration() {
        return 150;
    }

    protected void refreshRefreshableViewSize(int width, int height) {
        if (this.mRefreshableViewWrapper != null) {
            LayoutParams lp = (LayoutParams) this.mRefreshableViewWrapper.getLayoutParams();
            if (lp.height != height) {
                lp.height = height;
                this.mRefreshableViewWrapper.requestLayout();
            }
        }
    }

    protected void addRefreshableView(Context context, T refreshableView) {
        this.mRefreshableViewWrapper = new FrameLayout(context);
        this.mRefreshableViewWrapper.addView(refreshableView, -1, -1);
        addView(this.mRefreshableViewWrapper, new LayoutParams(-1, 10));
    }

    protected void addHeaderAndFooter(Context context) {
        LayoutParams params = new LayoutParams(-1, -2);
        LoadingLayout headerLayout = this.mHeaderLayout;
        LoadingLayout footerLayout = this.mFooterLayout;
        if (headerLayout != null) {
            if (this == headerLayout.getParent()) {
                removeView(headerLayout);
            }
            addView(headerLayout, 0, params);
        }
        if (footerLayout != null) {
            if (this == footerLayout.getParent()) {
                removeView(footerLayout);
            }
            addView(footerLayout, -1, params);
        }
    }

    protected void pullHeaderLayout(float delta) {
        int oldScrollY = getScrollYValue();
        if (delta >= 0.0f || ((float) oldScrollY) - delta < 0.0f) {
            setScrollBy(0, -((int) delta));
            if (!(this.mHeaderLayout == null || this.mHeaderHeight == 0)) {
                this.mHeaderLayout.onPull(((float) Math.abs(getScrollYValue())) / ((float) this.mHeaderHeight));
            }
            int scrollY = Math.abs(getScrollYValue());
            if (isPullRefreshEnabled() && !isPullRefreshing()) {
                if (scrollY > this.mHeaderHeight) {
                    this.mPullDownState = State.RELEASE_TO_REFRESH;
                } else {
                    this.mPullDownState = State.PULL_TO_REFRESH;
                }
                this.mHeaderLayout.setState(this.mPullDownState);
                onStateChanged(this.mPullDownState, true);
                return;
            }
            return;
        }
        setScrollTo(0, 0);
    }

    protected void pullFooterLayout(float delta) {
        int oldScrollY = getScrollYValue();
        if (delta <= 0.0f || ((float) oldScrollY) - delta > 0.0f) {
            setScrollBy(0, -((int) delta));
            if (!(this.mFooterLayout == null || this.mFooterHeight == 0)) {
                this.mFooterLayout.onPull(((float) Math.abs(getScrollYValue())) / ((float) this.mFooterHeight));
            }
            int scrollY = Math.abs(getScrollYValue());
            if (isPullLoadEnabled() && !isPullLoading()) {
                if (scrollY > this.mFooterHeight) {
                    this.mPullUpState = State.RELEASE_TO_REFRESH;
                } else {
                    this.mPullUpState = State.PULL_TO_REFRESH;
                }
                this.mFooterLayout.setState(this.mPullUpState);
                onStateChanged(this.mPullUpState, false);
                return;
            }
            return;
        }
        setScrollTo(0, 0);
    }

    protected void resetHeaderLayout() {
        int scrollY = Math.abs(getScrollYValue());
        boolean refreshing = isPullRefreshing();
        if (refreshing && scrollY <= this.mHeaderHeight) {
            smoothScrollTo(0);
        } else if (refreshing) {
            smoothScrollTo(-this.mHeaderHeight);
        } else {
            smoothScrollTo(0);
        }
    }

    protected void resetFooterLayout() {
        int scrollY = Math.abs(getScrollYValue());
        boolean isPullLoading = isPullLoading();
        if (isPullLoading && scrollY <= this.mFooterHeight) {
            smoothScrollTo(0);
        } else if (isPullLoading) {
            smoothScrollTo(this.mFooterHeight);
        } else {
            smoothScrollTo(0);
        }
    }

    protected boolean isPullRefreshing() {
        return this.mPullDownState == State.REFRESHING;
    }

    protected boolean isPullLoading() {
        return this.mPullUpState == State.REFRESHING;
    }

    protected void startRefreshing() {
        if (!isPullRefreshing()) {
            this.mPullDownState = State.REFRESHING;
            onStateChanged(State.REFRESHING, true);
            if (this.mHeaderLayout != null) {
                this.mHeaderLayout.setState(State.REFRESHING);
            }
            if (this.mRefreshListener != null) {
                postDelayed(new Runnable() {
                    public void run() {
                        PullToRefreshBase.this.mRefreshListener.onPullDownToRefresh(PullToRefreshBase.this);
                    }
                }, getSmoothScrollDuration());
            }
        }
    }

    protected void startLoading() {
        if (!isPullLoading()) {
            this.mPullUpState = State.REFRESHING;
            onStateChanged(State.REFRESHING, false);
            if (this.mFooterLayout != null) {
                this.mFooterLayout.setState(State.REFRESHING);
            }
            if (this.mRefreshListener != null) {
                postDelayed(new Runnable() {
                    public void run() {
                        PullToRefreshBase.this.mRefreshListener.onPullUpToRefresh(PullToRefreshBase.this);
                    }
                }, getSmoothScrollDuration());
            }
        }
    }

    protected void onStateChanged(State state, boolean isPullDown) {
    }

    private void setScrollTo(int x, int y) {
        scrollTo(x, y);
    }

    private void setScrollBy(int x, int y) {
        scrollBy(x, y);
    }

    private int getScrollYValue() {
        return getScrollY();
    }

    private void smoothScrollTo(int newScrollValue) {
        smoothScrollTo(newScrollValue, getSmoothScrollDuration(), 0);
    }

    private void smoothScrollTo(int newScrollValue, long duration, long delayMillis) {
        if (this.mSmoothScrollRunnable != null) {
            this.mSmoothScrollRunnable.stop();
        }
        int oldScrollValue = getScrollYValue();
        boolean post = oldScrollValue != newScrollValue;
        if (post) {
            this.mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration);
        }
        if (!post) {
            return;
        }
        if (delayMillis > 0) {
            postDelayed(this.mSmoothScrollRunnable, delayMillis);
        } else {
            post(this.mSmoothScrollRunnable);
        }
    }

    private void setInterceptTouchEventEnabled(boolean enabled) {
        this.mInterceptEventEnable = enabled;
    }

    private boolean isInterceptTouchEventEnabled() {
        return this.mInterceptEventEnable;
    }
}
