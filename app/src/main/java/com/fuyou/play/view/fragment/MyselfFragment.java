package com.fuyou.play.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fuyou.play.R;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseFragment;
import com.fuyou.play.view.activity.SystemSettingActivity;
import com.fuyou.play.widget.CircleImageView;

import cn.wwei.sdktest.SdkTestActivity;

/**
 * Created by Administrator on 2018-07-20.
 *
 * @author wwei
 */
public class MyselfFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_myself, container, false);
        initStatusBar(findViewById(R.id.status_bar));
        initTitleViews();
        initViews();
        return rootView;
    }

    private void initTitleViews() {
        findViewById(R.id.iv_back_base).setVisibility(View.INVISIBLE);
        TextView tv_right = (TextView)findViewById(R.id.tv_right_base);
        Drawable drawable = getResources().getDrawable(R.mipmap.system_setting_icon);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_right.setCompoundDrawables(drawable, null, null, null);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SystemSettingActivity.class);
            }
        });
        tv_right.setVisibility(View.VISIBLE);

    }

    private CircleImageView civ;
    private TextView tv_name, tv_detail, tv_bottom;
    private void initViews() {
        civ = (CircleImageView)findViewById(R.id.civ);
        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(InfoSettingActivity.class);
                startActivity(SdkTestActivity.class);
            }
        });
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_detail = (TextView)findViewById(R.id.tv_detail);
        if (!TextUtils.isEmpty(UserDataUtil.getAvatar(context))) {
            Glide.with(this).load(UserDataUtil.getAvatar(context))
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(civ);
        } else {
            civ.setImageResource(R.drawable.icon_chat_default);
        }
        tv_name.setText(TextUtils.isEmpty(UserDataUtil.getUserName(context))?"":UserDataUtil.getUserName(context));
    }



}
