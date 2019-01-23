package com.yyspbfq.filmplay.widget;

/*
 * Android-AdvancedWebView (https://github.com/delight-im/Android-AdvancedWebView)
 * Copyright (c) delight.im (https://www.delight.im/)
 * Licensed under the MIT License (https://opensource.org/licenses/MIT)
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.yyspbfq.filmplay.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Advanced WebView component for Android that works as intended out of the box
 */
@SuppressWarnings("deprecation")
public class AdvancedWebView extends WebView {

    private static final String TAG = "AdvancedWebView";
    protected static final String DATABASES_SUB_FOLDER = "/databases";

    public interface Listener {
        void onPageStarted(String url, Bitmap favicon);

        void onPageFinished(String url);

        void onPageError(int errorCode, String description, String failingUrl);

        void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent);

        void onExternalPageRequest(String url);
    }

    protected Listener mListener;

    protected long mLastError;
    protected WebViewClient mCustomWebViewClient;
    protected WebChromeClient mCustomWebChromeClient;
    protected boolean mGeolocationEnabled;
    protected final Map<String, String> mHttpHeaders = new HashMap<>();

    public AdvancedWebView(Context context) {
        super(context);
        init(context);
    }

    public AdvancedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdvancedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void setWebViewClient(final WebViewClient client) {
        mCustomWebViewClient = client;
    }

    @Override
    public void setWebChromeClient(final WebChromeClient client) {
        mCustomWebChromeClient = client;
    }

    public void onDestroy() {
        // try to remove this view from its parent first
        try {
            ((ViewGroup) getParent()).removeView(this);
        } catch (Exception ignored) {
        }

        // then try to remove all child views from this view
        try {
            removeAllViews();
        } catch (Exception ignored) {
        }

        // and finally destroy this view
        destroy();
    }


    @SuppressLint({"SetJavaScriptEnabled"})
    protected void init(final Context context) {
        // in IDE's preview mode
        if (isInEditMode()) {
            // do not run the code from this method
            return;
        }

        final WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT < 18) {
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
        webSettings.setDatabaseEnabled(true);

        final String filesDir = context.getFilesDir().getPath();
        final String databaseDir = filesDir.substring(0, filesDir.lastIndexOf("/")) + DATABASES_SUB_FOLDER;
        Logger.d("====" + filesDir);
        Logger.d("====" + databaseDir);
        if (Build.VERSION.SDK_INT < 19) {
            webSettings.setDatabasePath(databaseDir);//修复下android.database.sqlite.SQLiteException: cannot rollback - no transaction is active
        }

        /*
        5.0 以后的WebView加载的链接为Https开头，但是链接里面的内容，比如图片为Http链接，这时候，图片就会加载不出来
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(webSettings.getMixedContentMode());
        }

        //设置UA
        webSettings.setUserAgentString(webSettings.getUserAgentString().concat(CommonUtils.getUserAgent()));
        super.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!hasError()) {
                    if (mListener != null) {
                        mListener.onPageStarted(url, favicon);
                    }
                }

                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!hasError()) {
                    if (mListener != null) {
                        mListener.onPageFinished(url);
                    }
                }

                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient.onPageFinished(view, url);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                // if there is a user-specified handler available
                if (mCustomWebViewClient != null) {
                    // if the user-specified handler asks to override the request
                    if (mCustomWebViewClient.shouldOverrideUrlLoading(view, url)) {
                        // cancel the original request
                        return true;
                    }
                }

                // route the request through the custom URL loading method
                view.loadUrl(url);

                // cancel the original request
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                setLastError();

                if (mListener != null) {
                    mListener.onPageError(errorCode, description, failingUrl);
                }

                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                if (mCustomWebViewClient != null) {
//                    mCustomWebViewClient.onReceivedSslError(view, handler, error);
//                } else {
//                    super.onReceivedSslError(view, handler, error);
//                }

                handler.proceed();// 接受所有网站的证书
                super.onReceivedSslError(view, handler, error);
            }

        });

        super.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient.onProgressChanged(view, newProgress);
                } else {
                    super.onProgressChanged(view, newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient.onReceivedTitle(view, title);
                } else {
                    super.onReceivedTitle(view, title);
                }
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient.onReceivedIcon(view, icon);
                } else {
                    super.onReceivedIcon(view, icon);
                }
            }

            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
                } else {
                    super.onReceivedTouchIconUrl(view, url, precomposed);
                }
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient.onShowCustomView(view, callback);
                } else {
                    super.onShowCustomView(view, callback);
                }
            }

            @SuppressLint("NewApi")
            @SuppressWarnings("all")
            public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
                if (Build.VERSION.SDK_INT >= 14) {
                    if (mCustomWebChromeClient != null) {
                        mCustomWebChromeClient.onShowCustomView(view, requestedOrientation, callback);
                    } else {
                        super.onShowCustomView(view, requestedOrientation, callback);
                    }
                }
            }

            @Override
            public void onHideCustomView() {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient.onHideCustomView();
                } else {
                    super.onHideCustomView();
                }
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                if (mCustomWebChromeClient != null) {
                    return mCustomWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
                } else {
                    return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
                }
            }

            @Override
            public void onRequestFocus(WebView view) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient.onRequestFocus(view);
                } else {
                    super.onRequestFocus(view);
                }
            }

            @Override
            public void onCloseWindow(WebView window) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient.onCloseWindow(window);
                } else {
                    super.onCloseWindow(window);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (mCustomWebChromeClient != null) {
                    return mCustomWebChromeClient.onJsAlert(view, url, message, result);
                } else {
                    return super.onJsAlert(view, url, message, result);
                }
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                if (mCustomWebChromeClient != null) {
                    return mCustomWebChromeClient.onJsConfirm(view, url, message, result);
                } else {
                    return super.onJsConfirm(view, url, message, result);
                }
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                if (mCustomWebChromeClient != null) {
                    return mCustomWebChromeClient.onJsPrompt(view, url, message, defaultValue, result);
                } else {
                    return super.onJsPrompt(view, url, message, defaultValue, result);
                }
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                if (mCustomWebChromeClient != null) {
                    return mCustomWebChromeClient.onJsBeforeUnload(view, url, message, result);
                } else {
                    return super.onJsBeforeUnload(view, url, message, result);
                }
            }

            @Override
            public boolean onJsTimeout() {
                if (mCustomWebChromeClient != null) {
                    return mCustomWebChromeClient.onJsTimeout();
                } else {
                    return super.onJsTimeout();
                }
            }

            @Override
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient.onConsoleMessage(message, lineNumber, sourceID);
                } else {
                    super.onConsoleMessage(message, lineNumber, sourceID);
                }
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if (mCustomWebChromeClient != null) {
                    return mCustomWebChromeClient.onConsoleMessage(consoleMessage);
                } else {
                    return super.onConsoleMessage(consoleMessage);
                }
            }

        });

        setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(final String url, final String userAgent, final String contentDisposition, final String mimeType, final long contentLength) {
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

        });

    }

    @Override
    public void loadUrl(final String url, Map<String, String> additionalHttpHeaders) {
        if (additionalHttpHeaders == null) {
            additionalHttpHeaders = mHttpHeaders;
        } else if (mHttpHeaders.size() > 0) {
            additionalHttpHeaders.putAll(mHttpHeaders);
        }

        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadUrl(final String url) {
        if (mHttpHeaders.size() > 0) {
            super.loadUrl(url, mHttpHeaders);
        } else {
            super.loadUrl(url);
        }
    }

    protected void setLastError() {
        mLastError = System.currentTimeMillis();
    }

    protected boolean hasError() {
        return (mLastError + 500) >= System.currentTimeMillis();
    }

}
