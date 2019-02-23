package com.fuyou.play.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fuyou.play.R;
import com.fuyou.play.bean.UserInfo;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.util.Const;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.CircleImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018-08-09.
 */

public class InfoProfileActivity extends BaseActivity implements HttpRepListener, View.OnClickListener{

    private UserInfo mUserInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_profile);
        initStatusBar(findViewById(R.id.status_bar));
        mUserInfo = (UserInfo) getIntent().getSerializableExtra(Const.INTENT_CHAT_USER);
        if (mUserInfo==null) return;
        initTitleViews();
        initViews();
        initData();
    }

    private void initTitleViews() {
        setBackViews(R.id.iv_back_base);
//        ((TextView) findViewById(R.id.tv_title_base)).setText(getString(R.string.title_info_profile));
    }

    private CircleImageView civ;
    private TextView tv_name, tv_detail, tv_bottom;

    private void initViews() {
        civ = findViewById(R.id.civ);
        civ.setOnClickListener(this);
        tv_name = findViewById(R.id.tv_name);
        tv_detail = findViewById(R.id.tv_detail);
        tv_bottom = findViewById(R.id.tv_bottom);
        tv_bottom.setOnClickListener(this);
        if (!TextUtils.isEmpty(mUserInfo.getIcon())) {
            Glide.with(this).load(mUserInfo.getIcon())
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(civ);
        } else {
            civ.setImageResource(R.drawable.icon_chat_default);
        }
        tv_name.setText(TextUtils.isEmpty(mUserInfo.getNickName())?"":mUserInfo.getNickName());
    }

    public static final String TAG = "InfoSettingActivity---" + android.os.Process.myPid();



    private void initData() {
//        Factory.getHttpRespBiz(this, HttpFlag.USER_INFO, null).get();
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, Object> map = new HashMap<>();
        /*try {
            if (HttpFlag.EDIT_INFO == flag) {
                String[] args = (String[]) obj;
                map.put(args[0], args[1]);
            } else if (HttpFlag.USER_INFO == flag) {
                map.put("query_uid", UserDataUtil.getUserID(this));
            } else if (HttpFlag.GET_QN_TOKEN == flag) {

            } else if (HttpFlag.ACTIVE_INVITE_CODE == flag) {
                map.put("code", obj+"");
            } else if (HttpFlag.NEWS_CENTER == flag) {
                map.put("page_index", 0);
                map.put("page_size", 1);
                String last_time = UserDataUtil.getNewsLastTime(this);
                map.put("last_time", TextUtils.isEmpty(last_time)?"0":last_time);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, this.getClass().getSimpleName() + " getParamInfo");
        }*/
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ:

                break;
            case R.id.tv_bottom:
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra(Const.INTENT_CHAT_TYPE, 0);
                intent.putExtra(Const.INTENT_CHAT_USER, mUserInfo);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Factory.getHttpRespBiz(this, HttpFlag.NEWS_CENTER, null).get();
    }
}
