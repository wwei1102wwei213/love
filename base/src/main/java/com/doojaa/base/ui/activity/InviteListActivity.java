package com.doojaa.base.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.doojaa.base.R;
import com.doojaa.base.ui.BaseActivity;

public class InviteListActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_invite_list);
        initStatusBar(findViewById(R.id.status_bar));
        initViews();
        initData();
    }

    private void initViews() {

    }

    private void initData() {

    }

}
