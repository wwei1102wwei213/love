package com.fuyou.play.view.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.adapter.TarotChooseAdapter;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.tarot.AnimationListenerImpl;
import com.fuyou.play.util.tarot.TarotManager;
import com.fuyou.play.util.tarot.TarotYesOrNo;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.custom.TarotAnimationView;
import com.fuyou.play.widget.tv.HeavyTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TarotChooseOneActivity extends BaseActivity {

    protected RecyclerView mRecyclerView;
    protected ImageView mCurrentCardView;
    protected ImageView mFinalCardView;
    protected HeavyTextView mCurrentCardIndexText;
    protected RelativeLayout mChooseLayout;
    protected TarotAnimationView mAnimationView;
    protected View mStartLayout;
    private TextView mAfterAnimationTv;

    private int mCurrentCardIndex = 0;
    private int mChildOffset;
    private int mItemViewWidth;
    private int mRealCardIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setFullScreen();
        setContentView(R.layout.activity_tarot_choose_one);
        initView();
    }

    private void initView() {
        setBackViews(R.id.back_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mCurrentCardView = (ImageView) findViewById(R.id.current_card_view);
        mFinalCardView = (ImageView) findViewById(R.id.image_after_animation);
        mCurrentCardIndexText = (HeavyTextView) findViewById(R.id.current_card_index_text);
        mAfterAnimationTv = (TextView) findViewById(R.id.after_animation_text);
        mAfterAnimationTv.setText("Now top the card\nabove,and flip it to\ndiscover it's meaning...");

        // for test
        final int testCount = TarotManager.getInstance().getCardCount();
        mRealCardIndex = Math.abs(new Random().nextInt()) % testCount;
        List<Integer> testList = new ArrayList<>(testCount);
        for (int i = 0; i < testCount; i++) {
            testList.add(i);
        }
        final TarotChooseAdapter adapter = new TarotChooseAdapter(this, testList);
        adapter.setLooped(true, 100);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ((i - mChildOffset + testCount) % testCount == mCurrentCardIndex) {
                    int height = getWindowManager().getDefaultDisplay().getHeight();
                    int[] locationInWindow = new int[2];
                    mCurrentCardView.getLocationInWindow(locationInWindow);
                    TranslateAnimation animation = new TranslateAnimation(0, 0, height - locationInWindow[1] - mCurrentCardView.getHeight(), 0);
                    animation.setDuration(300);
                    mCurrentCardView.setVisibility(View.VISIBLE);
                    mCurrentCardView.startAnimation(animation);
                    mRecyclerView.smoothScrollBy(mItemViewWidth, 0);

                    int[] finalViewLocationInWindow = new int[2];
                    mFinalCardView.getLocationInWindow(finalViewLocationInWindow);
                    final float offset = finalViewLocationInWindow[1] - locationInWindow[1] + (mFinalCardView.getHeight() - mCurrentCardView.getHeight()) / 2;

                    animation.setAnimationListener(new AnimationListenerImpl() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            super.onAnimationEnd(animation);
                            mChooseLayout.setVisibility(View.GONE);
                            final int top = mCurrentCardView.getTop();
                            final int bottom = mCurrentCardView.getBottom();
                            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
                            valueAnimator.setDuration(1000)
                                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    if (Float.compare(value, 1f) >= 0) {
                                        mCurrentCardView.setVisibility(View.GONE);
                                        mFinalCardView.setVisibility(View.VISIBLE);
                                        mAfterAnimationTv.setVisibility(View.VISIBLE);
                                    } else {
                                        mCurrentCardView.layout(mCurrentCardView.getLeft(), (int) (top + offset * value),
                                                mCurrentCardView.getRight(), (int) (bottom + offset * value));
                                        mCurrentCardView.setPivotX(mCurrentCardView.getWidth() / 2);
                                        mCurrentCardView.setPivotY(mCurrentCardView.getHeight() / 2);
                                        mCurrentCardView.setScaleX(1f + (0.5f * value));
                                        mCurrentCardView.setScaleY(1f + (0.5f * value));
                                    }
                                }
                            });
                            valueAnimator.start();
                            mFinalCardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        int drawable = getResources().getIdentifier("tarot_card_" + (mRealCardIndex + 1), "mipmap", getPackageName());
                                        TarotYesOrNo tarotYesOrNo = TarotManager.getInstance().getTarotYesOrNo(mRealCardIndex);
                                        String[] content = tarotYesOrNo.getContent();
                                        Intent intent = new Intent(TarotChooseOneActivity.this, TarotSingleReadActivity.class);
                                        intent.putExtra(TarotSingleReadActivity.INTENT_CARD_IMAGE, drawable);
                                        intent.putExtra(TarotSingleReadActivity.INTENT_CARD_REVERSED, content[0].equals(TarotYesOrNo.TAROT_NO));
                                        intent.putExtra(TarotSingleReadActivity.INTENT_CARD_TITLE, content[0]);
                                        intent.putExtra(TarotSingleReadActivity.INTENT_CARD_DESCRIPTION, content[1]);
                                        startActivity(intent);
                                    }catch (Exception e){
                                        ExceptionUtils.ExceptionSend(e, "TarotChooseOneActivity mFinalCardView onClick");
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.scrollToPosition(adapter.getLoopedInitializePosition());
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
                mChildOffset = mRecyclerView.getChildCount() / 2;
                mItemViewWidth = mRecyclerView.getChildAt(0).getWidth();
                mRecyclerView.removeOnLayoutChangeListener(this);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int childAdapterPosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
                int cardIndex = adapter.getRealPositionInLoop(childAdapterPosition);
                if (mCurrentCardIndex != cardIndex) {
                    mCurrentCardIndex = cardIndex;
                    mCurrentCardIndexText.setText(Integer.toString(cardIndex + 1));
                }
            }
        });
        mChooseLayout = (RelativeLayout) findViewById(R.id.choose_layout);
        mAnimationView = (TarotAnimationView) findViewById(R.id.animation_view);
        mAnimationView.setOnCompleteListener(new TarotAnimationView.OnCompleteListener() {
            @Override
            public void onComplete() {
                mAnimationView.setVisibility(View.GONE);
                mChooseLayout.setVisibility(View.VISIBLE);
            }
        });
        mStartLayout = findViewById(R.id.start_layout);
        mStartLayout.findViewById(R.id.tarot_card_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartLayout.setVisibility(View.GONE);
                mAnimationView.setVisibility(View.VISIBLE);
                mAnimationView.startAnimation();
            }
        });

        mStartLayout.findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartLayout.setVisibility(View.GONE);
                mAnimationView.setVisibility(View.VISIBLE);
                mAnimationView.startAnimation();
            }
        });
    }
}
