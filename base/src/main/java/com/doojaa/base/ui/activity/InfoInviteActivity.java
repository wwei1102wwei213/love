package com.doojaa.base.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doojaa.base.R;
import com.doojaa.base.bean.InviteTaskBean;
import com.doojaa.base.biz.http.HttpFlag;
import com.doojaa.base.biz.http.HttpRepListener;
import com.doojaa.base.ui.BaseActivity;
import com.doojaa.base.utils.BLog;

import java.util.List;


public class InfoInviteActivity extends BaseActivity implements HttpRepListener {


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, InfoInviteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_info_invite);
        initStatusBar(findViewById(R.id.status_bar));
        findViewById(R.id.v_base_title).setBackgroundColor(Color.parseColor("#323232"));
        initViews();
        initData();
    }

    private void initData() {
//        Factory.resp(this, HttpFlag.FLAG_INVITE_TASK, null, InviteTaskBean.class).get(null);
    }

    private void initViews() {
        try {
            //标题和布局
            ((TextView) findViewById(R.id.tv_base_title)).setText("推广");
            findViewById(R.id.iv_base_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            TextView tv_right = (TextView) findViewById(R.id.tv_base_right);
            tv_right.setText("我的推广");
            tv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    toRight();
                }
            });
            tv_right.setVisibility(View.VISIBLE);
            /*ScrollView sv = (ScrollView) findViewById(R.id.sv);
            ElasticityHelper.setUpOverScroll(sv);*/
        } catch (Exception e){
            BLog.e(e);
        }

        //个人信息
        try {
            /*ImageView civ = (ImageView) findViewById(R.id.civ);
            Glide.with(this).load(BaseApplication.userInfo.getAvatar()).into(civ);
            ImageView iv1 = (ImageView) findViewById(R.id.iv_v1);
            String v1Url = BaseApplication.userInfo.getNowIcon();
            Glide.with(this).load(TextUtils.isEmpty(v1Url)?R.mipmap.ic_level0:v1Url).into(iv1);
            ImageView iv2 = (ImageView) findViewById(R.id.iv_v2);
            String v2Url = BaseApplication.userInfo.getNextIcon();
            Glide.with(this).load(TextUtils.isEmpty(v2Url)?R.mipmap.ic_level0:v2Url).into(iv2);
            ((TextView) findViewById(R.id.tv_name)).setText(BaseApplication.userInfo.getNickname());
            ((TextView) findViewById(R.id.tv_code)).setText(
                    String.format(getString(R.string.ads_invite_code), BaseApplication.userInfo.getShareCodeID()));
            ((TextView) findViewById(R.id.tv_v1)).setText(BaseApplication.userInfo.getNowLevelName());
            ((TextView) findViewById(R.id.tv_v2)).setText(BaseApplication.userInfo.getNextLevelName());
            ((TextView) findViewById(R.id.tv_num_today)).setText(BaseApplication.userInfo.getMax_coin());
            ((TextView) findViewById(R.id.tv_not_use)).setText(BaseApplication.userInfo.getV2_bookcoin());

            int now = BaseApplication.userInfo.getNowNum();
            int next = BaseApplication.userInfo.getNextNum();
            String hintString = "";
            if (next!=0&&next==now) {
                hintString = "您已达到最高等级！";
            } else {
                ProgressBar bar = (ProgressBar) findViewById(R.id.pb);
                bar.setProgress(now*100/next);
                hintString = String.format(getString(R.string.mine_invite_level), ""+(next-now));
            }
            ((TextView) findViewById(R.id.tv_up_hint)).setText(hintString);

            findViewById(R.id.iv_er).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCodeShare();
                }
            });

            findViewById(R.id.tv_invite_now).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCodeShare();
                }
            });*/
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void setInviteData(InviteTaskBean bean) {
        try {
            InviteTaskBean.LevelEntity entity = bean.getDay();
            if (entity!=null) {
                findViewById(R.id.v_day).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.tv_title_day)).setText(entity.getName()==null?"每日任务":entity.getName());
                ((TextView) findViewById(R.id.tv_remark_day)).setText(entity.getRemark()==null?"":entity.getRemark());
            }
            List<InviteTaskBean.LevelEntity> list = bean.getWelfare();
            if (list!=null&&list.size()>0) {
                findViewById(R.id.v_fuli).setVisibility(View.VISIBLE);
                LinearLayout body = (LinearLayout) findViewById(R.id.v_fuli_body);
                for (int i=0;i<list.size();i++) {
                    entity = list.get(i);
                    View item = LayoutInflater.from(this).inflate(R.layout.item_invite_level, body, false);
                    ((TextView) item.findViewById(R.id.tv_title_item)).setText(entity.getName()==null?"":entity.getName());
                    ((TextView) item.findViewById(R.id.tv_summary_item)).setText(entity.getRemark()==null?"":entity.getRemark());
                    body.addView(item);
                }
            }
            list = bean.getGrabe();
            if (list!=null&&list.size()>0) {
                LinearLayout body = (LinearLayout) findViewById(R.id.v_invite_task);
                body.setVisibility(View.VISIBLE);
                for (int i=0;i<list.size();i++) {
                    entity = list.get(i);
                    View item = LayoutInflater.from(this).inflate(R.layout.item_invite_grade, body, false);
                    ((TextView) item.findViewById(R.id.tv_title_item)).setText(entity.getName()==null?"":entity.getName());
                    ((TextView) item.findViewById(R.id.tv_level_item)).setText(entity.getRemark()==null?"":entity.getRemark());
                    ImageView iv_level_item = (ImageView) item.findViewById(R.id.iv_level_item);
                    Glide.with(this)
                            .load(TextUtils.isEmpty(entity.getThumb())?R.mipmap.ic_level0:entity.getThumb()).into(iv_level_item);
                    body.addView(item);
                }
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void toInviteList() {
//        startActivity(new Intent(this, MyInviteActivity.class));
    }

//    private void toCodeShare() {
//        UserInviteQcodeActivity.actionStart(this);
//    }

    @Override
    public void handleResp(Object data, int flag, Object obj, String response, String hint) {
        if (flag == HttpFlag.FLAG_INVITE_TASK) {
            try {
//                setInviteData((InviteTaskBean) data);
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    public void showLoading(int flag, Object obj) {

    }

    @Override
    public void hideLoading(int flag, Object obj) {

    }

    @Override
    public void handleError(int flag, Object obj, int errorType, String response, String hint) {

    }

    @Override
    public void handleAfter(int flag, Object obj) {

    }
}

