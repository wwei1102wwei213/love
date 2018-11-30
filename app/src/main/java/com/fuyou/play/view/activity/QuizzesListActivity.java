package com.fuyou.play.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fuyou.play.R;
import com.fuyou.play.util.DensityUtils;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.custom.NestedGridView;
import com.fuyou.play.widget.pullwidget.elasticity.ElasticityNestedScrollView;
import com.fuyou.play.widget.pullwidget.pullrefresh.PullElasticityNestedScrollView;

public class QuizzesListActivity extends BaseActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzer_list);
        initStatusBar(findViewById(R.id.status_bar));
        initViews();
    }

    private void initViews() {
        PullElasticityNestedScrollView pv = findViewById(R.id.pv);
        ElasticityNestedScrollView sv = pv.getRefreshableView();

        pv.setPullRefreshEnabled(false);

        NestedGridView gv = new NestedGridView(this);
        gv.setNumColumns(2);
        int space = DensityUtils.dp2px(this, 5);
        gv.setHorizontalSpacing(space);
        gv.setVerticalSpacing(space);
        gv.setPadding(10,10,10,10);


    }

}
