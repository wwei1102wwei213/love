package com.fuyou.play.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.adapter.TarotChooseTypeAdapter;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.tarot.TarotManager;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.tv.HeavyTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择塔罗类型页面
 */

public class TarotChooseTypeActivity extends BaseActivity {

    private Context mContext;
    ViewPager mChooseTarotVp;
    ImageView mSelectBackIv;
    ImageView mSelectNextIv;
//    ImageView mBackBtn;
    private TarotChooseTypeAdapter mTarotTypeAdapter;
    private List<View> mTarotViews = new ArrayList<>();
    HeavyTextView textView3;

    String[] tv1 = new String[]{"Yes or No", "Past Present Future", "Love triangle decision"};
//    String[] tv4 = new String[]{"Generally Useful", "General Insight", "Weigh Two Choices"};
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBarTranslucent();
        setFullScreen();
        setContentView(R.layout.activity_tarot_choose_type);
        mContext = this;
        TarotManager.getInstance().loadContent(getApplicationContext());
        initView();
        initListener();
    }

    private void initView() {
        setBackViews(R.id.iv_back_base);
        initStatusBar(findViewById(R.id.status_bar), true);
        tv_title = findViewById(R.id.tv_title_base);
        mChooseTarotVp = (ViewPager) findViewById(R.id.choose_tarot_type_vp);
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.view_tarot_choose_one, null);
        View view2 = LayoutInflater.from(mContext).inflate(R.layout.view_tarot_choose_future, null);
        View view3 = LayoutInflater.from(mContext).inflate(R.layout.view_tarot_choose_loves, null);
        mTarotViews.add(view1);
        mTarotViews.add(view2);
        mTarotViews.add(view3);

        mTarotTypeAdapter = new TarotChooseTypeAdapter(mContext, mTarotViews);
        mChooseTarotVp.setAdapter(mTarotTypeAdapter);

        mSelectBackIv = (ImageView) findViewById(R.id.select_back_iv);
        mSelectNextIv = (ImageView) findViewById(R.id.select_next_iv);

        textView3 = (HeavyTextView) findViewById(R.id.textView3);
//        textView4 = (HeavyTextView) findViewById(R.id.textView4);
    }

    private void initListener() {

        mSelectBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = 0;
                if (mChooseTarotVp.getCurrentItem() == 0) {
                    currentItem = 2;
                } else {
                    currentItem = mChooseTarotVp.getCurrentItem() - 1;
                }
                mChooseTarotVp.setCurrentItem(currentItem);
            }
        });

        mSelectNextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = 0;
                if (mChooseTarotVp.getCurrentItem() == 2) {
                    currentItem = 0;
                } else {
                    currentItem = mChooseTarotVp.getCurrentItem() + 1;
                }
                mChooseTarotVp.setCurrentItem(currentItem);
            }
        });

        mChooseTarotVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_title.setText(tv1[position]);
                if (position==0) {
                    mSelectBackIv.setVisibility(View.INVISIBLE);
                } else {
                    mSelectBackIv.setVisibility(View.VISIBLE);
                }
                if (position==2){
                    mSelectNextIv.setVisibility(View.INVISIBLE);
                } else {
                    mSelectNextIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mChooseTarotVp.setCurrentItem(1);

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mChooseTarotVp.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        startActivity(new Intent(mContext, TarotChooseOneActivity.class));
                        break;
                    case 1:
                        toJump(1);

                        break;
                    case 2:
                        toJump(2);

                        break;
                }
            }
        });
    }

    private void toJump(int index){
        if (index==1){
            startActivity(new Intent(mContext, TarotChooseFutureActivity.class));
        } else if (index==2){
            startActivity(new Intent(mContext, TarotChooseLoversActivity.class));
        }
        /*if (Factory.checkUserPermission(this, Const.USER_PERMISSION_TYPE_2, Const.PERMISSION_REQUEST_CODE_306)){
            if (index==1){
                startActivity(new Intent(mContext, TarotChooseFutureActivity.class));
            } else if (index==2){
                startActivity(new Intent(mContext, TarotChooseLoversActivity.class));
            }
        } else {
            startActivityForResult(new Intent(this, AuthorityHintActivity.class)
                    .putExtra("User_Permission_Type",Const.USER_PERMISSION_TYPE_2)
                    .putExtra("User_Permission_Result_Type", index), Const.PERMISSION_REQUEST_CODE_306);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }
}
