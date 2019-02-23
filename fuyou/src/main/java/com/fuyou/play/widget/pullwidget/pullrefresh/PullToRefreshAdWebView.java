package com.fuyou.play.widget.pullwidget.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.fuyou.play.widget.webview.AdvancedWebView;


public class PullToRefreshAdWebView extends PullToRefreshBase<AdvancedWebView> {
    public PullToRefreshAdWebView(Context context) {
        this(context, null);
    }

    public PullToRefreshAdWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshAdWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected AdvancedWebView createRefreshableView(Context context, AttributeSet attrs) {
        return new AdvancedWebView(context);
    }

    protected boolean isReadyForPullDown() {
        return ((AdvancedWebView) this.mRefreshableView).getScrollY() == 0;
    }

    protected boolean isReadyForPullUp() {
        return ((float) ((AdvancedWebView) this.mRefreshableView).getScrollY()) >= ((float) Math.floor((double) (((AdvancedWebView) this.mRefreshableView).getScale() * ((float) ((AdvancedWebView) this.mRefreshableView).getContentHeight())))) - ((float) ((AdvancedWebView) this.mRefreshableView).getHeight());
    }
}
