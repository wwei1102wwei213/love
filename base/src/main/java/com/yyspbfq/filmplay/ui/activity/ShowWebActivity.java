package com.yyspbfq.filmplay.ui.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.biz.http.HttpInterceptor;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.widget.AdvancedWebView;
import com.yyspbfq.filmplay.widget.custom.CommonExceptionView;

import org.greenrobot.eventbus.EventBus;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ShowWebActivity extends BaseActivity{

    private static final String ARG_URL = "web_url";
    private static final String EXIT_TYPE = "film_exit_type";

    private AdvancedWebView mWebView;
    private String currentUrl;
    private ProgressBar mProgressBar;
    private LinearLayout actionbar_close;
    private TextView actionbar_title;
    private CommonExceptionView mExceptionView;

    private boolean isPull = false;
    private SwipeRefreshLayout mSwipeRefresh;
    private int downX, downY;
    private View content;

    public static void actionStart(Context context, String url) {
        Logger.d(url);
        Intent intent = new Intent(context, ShowWebActivity.class);
        intent.putExtra(ARG_URL, url);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, String url, int type) {
        Logger.d(url);
        Intent intent = new Intent(context, ShowWebActivity.class);
        intent.putExtra(ARG_URL, url);
        intent.putExtra(EXIT_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_web);
        initStatusBar();
        initViews();
    }

    private void initViews() {
        content = findViewById(R.id.content);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        setBackViews(R.id.iv_base_back);
        actionbar_title = (TextView) findViewById(R.id.tv_base_title);
        actionbar_title.setText("");
        mWebView = (AdvancedWebView) findViewById(R.id.adWebview);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        /*mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPull = true;
                reloadUrl();
            }
        });*/
        mSwipeRefresh.setEnabled(false);
        mExceptionView = new CommonExceptionView(this, mSwipeRefresh);
        mExceptionView.setOnExceptionViewListner(new CommonExceptionView.OnExceptionViewListener() {
            @Override
            public void reload() {
                reloadUrl();
            }
        });
        EventBus.getDefault().register(this);
        actionbar_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(webChromeClient);
        load();
    }

    @SuppressLint("AddJavascriptInterface")
    private void load() {
        if (getIntent() != null) {
            currentUrl = getIntent().getStringExtra(ARG_URL);
        }
        mWebView.addJavascriptInterface(new JSObject(), "film");
        reloadUrl();
    }

    public void reloadUrl() {
        Logger.d("webview加载：" + currentUrl);
        if (mWebView == null) return;
        Map<String, String> heads = new HashMap<>();
        heads.put(HttpInterceptor.KEY_CHANNEL, CommonUtils.getChannelName());
//        heads.put(HttpInterceptor.KEY_TYPE, UserDataUtil.getLoginType(this));
        mWebView.stopLoading();
        mWebView.loadUrl(currentUrl, heads);
    }

    protected WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http") || url.startsWith("file:///android_asset")) {
                view.loadUrl(url);
            } else if (url.startsWith("intent")) {
                try {
                    Intent intent = Intent.parseUri(url, 0);
                    startActivity(intent);
                } catch (URISyntaxException | ActivityNotFoundException e) {
                    Logger.e(e.getMessage());
                }
            } else {
                String[] url_types = getResources().getStringArray(R.array.web_scheme_url);
                for (String type : url_types) {
                    if (url.startsWith(type)) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);

                        } catch (ActivityNotFoundException e) {
                            Logger.e(e.getMessage());
                        }
                    }
                }
            }
            return true;
        }

        private boolean isClose = false;

        @Override
        public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
            Logger.d("onPageStarted：" + url);
            super.onPageStarted(view, url, favicon);
            isClose = true;
            currentUrl = url;
//            if (!isPull) ProgressBarUtil.visible(mProgressBar);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Logger.d("onReceivedError：" + failingUrl);
            isClose = false;
            mExceptionView.showByType(CommonExceptionView.TYPE_LOAD_ERROR);
            if (mSwipeRefresh.isShown()) {
                mSwipeRefresh.setRefreshing(false);
                isPull = false;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Logger.d("onPageFinished：" + url);
//            ProgressBarUtil.gone(mProgressBar);
            if (mSwipeRefresh.isShown()) {
                mSwipeRefresh.setRefreshing(false);
                isPull = false;
            }
            if (isClose) mExceptionView.dismiss();
            /*view.loadUrl("javascript:window.bhxs.showSource("+
                    "document.getElementsByTagName('html')[0].innerHTML);");*/
        }
    };

    protected WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            actionbar_title.setText(title);
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        downX = (int) event.getRawX();
        downY = (int) event.getRawY();
        return super.dispatchTouchEvent(event);
    }

    public class JSObject {


        @JavascriptInterface
        public void goBookTicket() {
            /*AppUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UserRechargeRecordActivity.actionStart(mActivity, 1);
                }
            });*/
        }

        /*@JavascriptInterface
        public void goDownloadApp(String advUrl) {
            AppUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Factory.resp(null, HttpFlag.FLAG_INVITE_CLICK_ADS, null, null).get(null);
                        if (!TextUtils.isEmpty(advUrl) && advUrl.toLowerCase().endsWith(".apk")) {
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                BLog.s("downloadApk");
                                String apkName = advUrl.substring(advUrl.lastIndexOf("/"));
                                Intent downloadIntent = new Intent(mActivity, DownloadService.class);
                                downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_NAME, apkName);
                                downloadIntent.putExtra(DownloadService.DOWNLOAD_APK_URL, advUrl);
                                mActivity.startService(downloadIntent);
                            } else {
                                Toast.makeText(mActivity, getResources().getString(R.string.not_find_sdcard), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e){
                        BLog.e(e);
                    }
                }
            });
        }*/

    }

    @Override
    public void finish() {
        int type = 0;
        try {
            type = getIntent().getIntExtra(EXIT_TYPE, 0);
            if (type == 1) {//返回到主页
                Intent intent = new Intent(this, MainActivity.class);
                if (getIntent().getExtras() != null) {
                    intent.putExtras(getIntent().getExtras());
                }
                startActivity(intent);
            }
        } catch (Exception e){
            BLog.e(e);
        }
        super.finish();
        try {
            if (type==1) {
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
