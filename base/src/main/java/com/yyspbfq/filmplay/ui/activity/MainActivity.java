package com.yyspbfq.filmplay.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.InviteCodeBean;
import com.yyspbfq.filmplay.bean.MessageEvent;
import com.yyspbfq.filmplay.bean.NoticeBean;
import com.yyspbfq.filmplay.bean.UpdateConfig;
import com.yyspbfq.filmplay.bean.UserInfoBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.download.DownloadTaskManager;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.DBHelper;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.ui.dialog.NoticeDialog;
import com.yyspbfq.filmplay.ui.dialog.UpdateAppDialog;
import com.yyspbfq.filmplay.ui.fragment.ChannelFragment;
import com.yyspbfq.filmplay.ui.fragment.DiscoverFragment;
import com.yyspbfq.filmplay.ui.fragment.HomeFragment;
import com.yyspbfq.filmplay.ui.fragment.MyFragment;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.CommonUtils;
import com.yyspbfq.filmplay.utils.Const;
import com.yyspbfq.filmplay.utils.UiUtils;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jzvd.JZExoPlayer;
import cn.jzvd.Jzvd;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity implements WLibHttpListener{

    public static final String TAB_CHANGE = "tag_change";
    public static final String BASE_MAIN_TAG_1 = "BASE_MAIN_TAG_1";
    public static final String BASE_MAIN_TAG_2 = "BASE_MAIN_TAG_2";
    public static final String BASE_MAIN_TAG_3 = "BASE_MAIN_TAG_3";
    public static final String BASE_MAIN_TAG_4 = "BASE_MAIN_TAG_4";
    private static final String[] fragmentTags = {BASE_MAIN_TAG_1, BASE_MAIN_TAG_2, BASE_MAIN_TAG_3, BASE_MAIN_TAG_4};
    private Fragment currentFragment;
    private FragmentManager mFragmentManager;

    private RadioGroup rg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_main);
        initViews(savedInstanceState);
        checkBaseUrl();
        Jzvd.setMediaInterface(new JZExoPlayer());
        //todo
        JPushInterface.setAlias(getApplicationContext(), CommonUtils.getUUID(), new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Logger.d("极光推送绑定:" + i + "-" + s + "-" + set);
            }
        });
        if (getIntent()!=null&&getIntent().hasExtra(Const.HANDLE_MSG_JPUSH)) {
            handleJpush(getIntent());
        }
        EventBus.getDefault().register(this);
        Factory.resp(MainActivity.this, HttpFlag.FLAG_NOTICE_SHOW, null, NoticeBean.class).post(null);
        Factory.resp(this, HttpFlag.FLAG_USER_INFO, null, UserInfoBean.class).post(null);
        Factory.resp(this, HttpFlag.FLAG_INVITE_CODE_MSG, null, InviteCodeBean.class).post(null);

    }

    private void initViews(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        rg = findViewById(R.id.tab_rg_bar);
        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.tab_rb_bookshelf:
                            replaceTab(0);
                            break;
                        case R.id.tab_rb_recommend:
                            replaceTab(1);
                            break;
                        case R.id.tab_rb_bookcity:
                            replaceTab(2);
                            break;
                        case R.id.tab_rb_me:
                            replaceTab(3);
                            break;
                    }
                }
            }
        };
        for (int i = 0; i < rg.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) rg.getChildAt(i);
            radioButton.setOnCheckedChangeListener(checkedChangeListener);
        }
        initFragments(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    private void initFragments(Bundle savedInstanceState) {
        //如果应用被杀死了，就立即清空之前fragment
        if (savedInstanceState != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            List<Fragment> fragments = mFragmentManager.getFragments();
            if (ft == null || fragments == null || fragments.size() == 0)
                return;
            boolean doCommit = false;
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    ft.remove(fragment);
                    doCommit = true;
                }
            }
            if (doCommit) {
                ft.commitNow();//立即提交， 以免后面findFragmentByTag不为null
            }
        }
        changeNavigationBar(0);
    }

    private void replaceTab(@IntRange(from = 0, to = 3) int i) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (null != currentFragment) {
            ft.hide(currentFragment);
        }
        //根据tag从事务中获取fragment
        Fragment fragment = mFragmentManager.findFragmentByTag(fragmentTags[i]);
        if (null == fragment) {
            fragment = newFragmentByTag(fragmentTags[i]);
        }
        //判断此Fragment是否已经添加到FragmentTransaction事物中
        if (!fragment.isAdded()) {
            ft.add(R.id.content, fragment, fragmentTags[i]);
        } else {
            ft.show(fragment);
        }
        currentFragment = fragment;
        ft.commitAllowingStateLoss();
    }

    private Fragment newFragmentByTag(String tag) {
        if (tag.equals(BASE_MAIN_TAG_1)) {
            return new HomeFragment();
        } else if (tag.equals(BASE_MAIN_TAG_2)) {
            return new ChannelFragment();
        } else if (tag.equals(BASE_MAIN_TAG_3)) {
            return new DiscoverFragment();
        } else {
            return new MyFragment();
        }
    }

    private void changeNavigationBar(int i) {
        try {
            RadioButton radioButton = (RadioButton) rg.getChildAt(i);
            radioButton.setChecked(true);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_USER_INFO) {
            try {
                UserInfoBean bean = (UserInfoBean) formatData;
                UserDataUtil.saveLoginType(this, bean.getType()+"");
                UserDataUtil.saveUserData(this, bean.getData());
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_SYNC_VIDEO_RECORD) {
            try {
                List<VideoEntity> list = new Gson().fromJson(formatData.toString(), new TypeToken<List<VideoEntity>>(){}.getType());
                if (list!=null&&list.size()>0) {
                    DBHelper.getInstance().syncVideoRecord(BaseApplication.getInstance(), list);
                }
                MyFragment fragment = (MyFragment) mFragmentManager.findFragmentByTag(BASE_MAIN_TAG_4);
                //刷新我的页面
                if (fragment != null) {
                    fragment.setUserView();
                    fragment.setHistoryView();
                    fragment.getCollections();
                }
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_INVITE_CODE_MSG) {
            try {
                InviteCodeBean bean = (InviteCodeBean) formatData;
                SPLongUtils.saveInviteCodeUrl(this, bean.getExtension()==null?bean.getUrl():bean.getExtension());
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_NOTICE_SHOW) {
            try {
                NoticeBean bean = (NoticeBean) formatData;
                if (bean!=null&&!TextUtils.isEmpty(bean.getContent())) {
                    NoticeDialog dialog = new NoticeDialog(this);
                    dialog.setData(bean);
                    dialog.show();
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
        if (flag == HttpFlag.FLAG_USER_INFO) {

        }
    }

    @Override
    public void handleAfter(int flag, Object tag) {
        if (flag == HttpFlag.FLAG_USER_INFO) {
            Factory.resp(this, HttpFlag.FLAG_SYNC_VIDEO_RECORD, null, null).post(null);
        } else if (flag == HttpFlag.FLAG_NOTICE_SHOW) {
            mHandler = new MyHandler(this);
            checkUpdate(SPLongUtils.getString(this, "video_update_app_url", HttpFlag.URL_UPDATE));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(MessageEvent messageEvent) {
//        Logger.e("handleEvent:"+messageEvent.getMessage());
        if (messageEvent.getMessage() == MessageEvent.MSG_GET_USER_INFO) {
            try {
                if (messageEvent.isNeedSync()) {
                    Factory.resp(this, HttpFlag.FLAG_SYNC_VIDEO_RECORD, null, null).post(null);
                } else {
                    MyFragment fragment = (MyFragment) mFragmentManager.findFragmentByTag(BASE_MAIN_TAG_4);
                    //刷新我的页面
                    if (fragment != null) {
                        fragment.setUserView();
                        fragment.getCollections();
                    }
                }
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (Jzvd.backPress()) {
                return true;
            }
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                showToast("再按一次退出");
            } else {
                DownloadTaskManager.getInstance().clearAll();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
        if (intent==null) return;
        if (intent.hasExtra(TAB_CHANGE)) {
            int tab = intent.getIntExtra(TAB_CHANGE, 0);
            changeNavigationBar(tab);
        } else if (intent.hasExtra(Const.HANDLE_MSG_JPUSH)) {
            handleJpush(intent);
        }
    }

    //处理极光推送
    private void handleJpush(Intent intent) {
        String jpush = intent.getStringExtra(Const.HANDLE_MSG_JPUSH);
        Logger.d(jpush);
        if (TextUtils.isEmpty(jpush)) return;
        try {
            JSONObject json = new JSONObject(jpush);
            int window = json.optInt("window");
            switch (window) {
                case 1://首页进入

                    break;
                case 2://播放页面
                    int id = json.optInt("id");
                    VideoPlayActivity.actionStart(this, id+"");
                    break;
                case 3://活动
//                    changeNavigationBar(0);
                    break;
                case 4://发现
                    changeNavigationBar(2);
                    break;
                default:

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private MyHandler mHandler;
    private List<String> updateUrls;

    /**
     * 检查更新
     * @param url
     */
    public void checkUpdate(String url) {
        if (TextUtils.isEmpty(url)) return;
        Logger.e("checkUpdate:"+url);
        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    if (CommonUtils.IsNetWorkEnable(BaseApplication.getInstance())) {
                        if (updateUrls==null) {
                            updateUrls = new ArrayList<>();
                            updateUrls.add("http://47.107.94.24:191/android/update.json");
                            updateUrls.add("http://47.107.94.24:91/android/update.json");
                            updateUrls.add("http://47.107.94.24:96/android/update.json");
                            updateUrls.add("http://47.107.94.24:97/android/update.json");
                        }
                        updateUrls.remove(url);
                        if (updateUrls.size()>0) {
                            checkUpdate(updateUrls.get(0));
                        } else {
//                            Logger.e("升级失败");
                            Message msg = Message.obtain();
                            msg.what = MSG_WHAT_CHECK_UPDATE_ERROR;
                            mHandler.sendMessage(msg);
                        }
                    }
                } catch (Exception ee){
                    BLog.e(ee);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResponseBody body = response.body();
                    UpdateConfig config = new Gson().fromJson(body.charStream(), UpdateConfig.class);
                    config.meHost = url;
                    Logger.e("checkUpdate："+new Gson().toJson(config));
                    if (!TextUtils.isEmpty(config.downurl)&&!TextUtils.isEmpty(config.versionCode)) {
                        Message msg = Message.obtain();
                        msg.what = MSG_WHAT_CHECK_UPDATE;
                        msg.obj = config;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleUpdate(UpdateConfig config) {
        SPLongUtils.saveString(this, "mevideo_update_config", new Gson().toJson(config));
        if (updateUrls!=null) {
            SPLongUtils.saveString(this, "video_update_app_url", config.meHost);
        }
        if (UiUtils.checkUpdate(config)) {
            UpdateAppDialog dialog = new UpdateAppDialog(this);
            dialog.setData(config);
            dialog.show();
        }
    }

    private void handleUpdateError() {
        NoticeDialog dialog = new NoticeDialog(this);
        dialog.setUpdateHint();
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private static final int MSG_WHAT_CHECK_UPDATE = 1;
    private static final int MSG_WHAT_CHECK_UPDATE_ERROR = 2;
    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> weak;
        private MyHandler(MainActivity activity) {
            weak = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if (weak.get()!=null&&msg.what==MSG_WHAT_CHECK_UPDATE) {
                weak.get().handleUpdate((UpdateConfig) msg.obj);
            } else if (weak.get()!=null&&msg.what==MSG_WHAT_CHECK_UPDATE_ERROR) {
                weak.get().handleUpdateError();
            }
        }
    }
}
