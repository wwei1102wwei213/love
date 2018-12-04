package com.doojaa.base.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.doojaa.base.AppManager;
import com.doojaa.base.R;
import com.doojaa.base.ui.BaseActivity;
import com.doojaa.base.ui.fragment.MyFragment;
import com.doojaa.base.utils.BLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends BaseActivity{

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
        EventBus.getDefault().register(this);
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
        changeNavigationBar(1);
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
            return new MyFragment();
        } else if (tag.equals(BASE_MAIN_TAG_2)) {
            return new MyFragment();
        } else if (tag.equals(BASE_MAIN_TAG_3)) {
            return new MyFragment();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object obj) {

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                showToast("再按一次退出");
            } else {
                AppManager.getInstance().AppExit(getApplicationContext());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
        if (intent.hasExtra(TAB_CHANGE)) {
            int tab = intent.getIntExtra(TAB_CHANGE, 0);
            changeNavigationBar(tab);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
