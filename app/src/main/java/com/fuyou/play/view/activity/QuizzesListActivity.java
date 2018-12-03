package com.fuyou.play.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.fuyou.play.R;
import com.fuyou.play.adapter.QuizzesGvAdapter;
import com.fuyou.play.util.DensityUtils;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.custom.NestedGridView;
import com.fuyou.play.widget.pullwidget.elasticity.ElasticityHelper;
import com.fuyou.play.widget.pullwidget.elasticity.ElasticityNestedScrollView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizzesListActivity extends BaseActivity{

    private int type;
    private String baseUrl;
    private List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzer_list);
        initStatusBar(findViewById(R.id.status_bar));
        initViews();
    }

    private void initViews() {
        setBackViews(R.id.iv_back_base);
        setTitle("Quizzes");

        type = getIntent().getIntExtra("quizzes_type", 0);
        String[] fileNames = null;
        try {
            if (type==1){
                fileNames = getAssets().list("quizzes/2");
            } else if (type==2){
                fileNames = getAssets().list("quizzes/3");
            } else {
                fileNames = getAssets().list("quizzes/1");
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }

        if (fileNames==null) return;

        list = new ArrayList<>();
        if (fileNames.length>0){
            for (int i = 0; i < fileNames.length; i++) {
                list.add(fileNames[i]);
            }
        }

        if (type==1){
            baseUrl = "https://s3.amazonaws.com/hammerpark/image/divinationImages/Personality+Tests/";
        } else if (type==2){
            baseUrl = "https://s3.amazonaws.com/hammerpark/image/divinationImages/Love+and+Sex+Quizzes/";
        } else {
            baseUrl = "https://s3.amazonaws.com/hammerpark/image/divinationImages/Astrology+and+Oracle+Quizzes/";
        }
        ElasticityNestedScrollView sv = findViewById(R.id.sv);

        NestedGridView gv = new NestedGridView(this);
        gv.setNumColumns(2);
        int space = DensityUtils.dp2px(this, 10);
        gv.setHorizontalSpacing(space*4/5);
        gv.setVerticalSpacing(space*6/5);
        gv.setPadding(space,space*3/2,space,space*3/2);

        QuizzesGvAdapter adapter = new QuizzesGvAdapter(this, fileNames, baseUrl);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toQuizzes(position);
            }
        });

        sv.addView(gv);

        ElasticityHelper.setUpOverScroll(sv);
    }

    private void toQuizzes(int position){
        Intent intent = new Intent(this, QuizzesDetailActivity.class);
        intent.putExtra("quizzes_type",type);
        intent.putExtra("imageUrlHead", baseUrl);
        intent.putExtra("quizzes_list_data", (Serializable)list);
        intent.putExtra("quizzes_position", position);
        startActivity(intent);
    }

}
