package com.yyspbfq.filmplay.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.biz.download.DownloadTaskManager;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.sp.SPLongUtils;

public class DownloadCountActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_count);
        initStatusBar();
        ((TextView) findViewById(R.id.tv_base_title)).setText("同时缓存个数");
        setBackViews(R.id.iv_base_back);
        initViews();
    }

    private View iv1, iv2, iv3;
    private void initViews() {
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        findViewById(R.id.v_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChange(1);
            }
        });
        findViewById(R.id.v_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChange(2);
            }
        });
        findViewById(R.id.v_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChange(3);
            }
        });
        int count  = SPLongUtils.getInt(this, "film_download_count", 3);
        if (count==1) {
            iv1.setVisibility(View.VISIBLE);
        }
        if (count==2) {
            iv2.setVisibility(View.VISIBLE);
        }
        if (count==3) {
            iv3.setVisibility(View.VISIBLE);
        }
        temp = count;
    }

    private int temp;
    private void toChange(int count) {
        if (temp==count) return;
        temp = count;
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        if (count==1) {
            iv1.setVisibility(View.VISIBLE);
        }
        if (count==2) {
            iv2.setVisibility(View.VISIBLE);
        }
        if (count==3) {
            iv3.setVisibility(View.VISIBLE);
        }
        SPLongUtils.saveInt(this, "film_download_count", count);
        DownloadTaskManager.getInstance().setMaxTask(count);
    }

}
