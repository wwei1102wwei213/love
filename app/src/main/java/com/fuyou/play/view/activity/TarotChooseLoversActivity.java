package com.fuyou.play.view.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.adapter.TarotChooseAdapter;
import com.fuyou.play.util.tarot.AnimationListenerImpl;
import com.fuyou.play.util.tarot.TarotManager;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.custom.TarotAnimationView;
import com.fuyou.play.widget.tv.HeavyTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TarotChooseLoversActivity extends BaseActivity {

    protected RecyclerView mRecyclerView;
    protected ImageView mCurrentCardView;
    protected HeavyTextView mCurrentCardIndexText;
    protected RelativeLayout mChooseLayout;
    protected TarotAnimationView mAnimationView;
    protected View mStartLayout;
    protected ImageView mPastCardView;
    protected ImageView mFutureCardView;
    protected ImageView mPastCardFinalView;
    protected ImageView mCurrentCardFinalView;
    protected ImageView mFutureCardFinalView;
    protected HeavyTextView mPastCardTv;
    protected HeavyTextView mCurrentCardTv;
    protected HeavyTextView mFutureCardTv;
    protected TextView mReadingText;

    private int mCurrentCardIndex = 0;
    private int mChildOffset;
    private int mItemViewWidth;

    private int mChoosedCardCount = 0;
    private View[][] mAnimationViews;
    private int[] mGroupIndexes = new int[] {
            TarotManager.TAROT_LOVER_GROUP_INDEX_FIRST_LOVER,
            TarotManager.TAROT_LOVER_GROUP_INDEX_YOUR,
            TarotManager.TAROT_LOVER_GROUP_INDEX_SECOND_LOVER,
    };
    private int[] mCardIndexes = new int[3];
    private boolean[] mCardReversed = new boolean[3];
    private String[] mCardTitles = new String[]{
            "LOVER1 IS RIGHT ?",
            "My Soul Tells",
            "LOVER2 IS RIGHT ?",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setFullScreen();
        setContentView(R.layout.activity_tarot_choose_lovers);
        initView();
    }

    private void initView() {
        setBackViews(R.id.back_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mReadingText = (TextView) findViewById(R.id.reading_text);
        mReadingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] imageIds = new int[3];
                String[] descriptions = new String[3];
                for (int i = 0; i < imageIds.length; i++) {
                    imageIds[i] = getResources().getIdentifier("tarot_card_" + (mCardIndexes[i] + 1), "mipmap", getPackageName());
                    descriptions[i] = TarotManager.getInstance().getTarotLover(mGroupIndexes[i], mCardIndexes[i]);
                }
                /*Intent intent = new Intent(TarotChooseLoversActivity.this, TarotThreeReadActivity.class);
                intent.putExtra(TarotThreeReadActivity.INTENT_CARD_IMAGE, imageIds);
                intent.putExtra(TarotThreeReadActivity.INTENT_CARD_REVERSED, mCardReversed);
                intent.putExtra(TarotThreeReadActivity.INTENT_CARD_DESCRIPTION, descriptions);
                startActivity(intent);*/
            }
        });
        mCurrentCardView = (ImageView) findViewById(R.id.current_card_view);
        mPastCardView = (ImageView) findViewById(R.id.past_card_view);
        mFutureCardView = (ImageView) findViewById(R.id.future_card_view);
        mPastCardFinalView = (ImageView) findViewById(R.id.past_card_final_view);
        mPastCardTv = (HeavyTextView) findViewById(R.id.past_card_text);
        mCurrentCardTv = (HeavyTextView) findViewById(R.id.current_card_text);
        mFutureCardTv = (HeavyTextView) findViewById(R.id.future_card_text);
        mPastCardFinalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardContent(0);
            }
        });
        mCurrentCardFinalView = (ImageView) findViewById(R.id.current_card_final_view);
        mCurrentCardFinalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardContent(1);
            }
        });
        mFutureCardFinalView = (ImageView) findViewById(R.id.future_card_final_view);
        mFutureCardFinalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardContent(2);
            }
        });
        mAnimationViews = new View[][]{
                new View[] { mPastCardView, mPastCardFinalView },
                new View[] { mCurrentCardView, mCurrentCardFinalView },
                new View[] { mFutureCardView, mFutureCardFinalView }, };
        mCurrentCardIndexText = (HeavyTextView) findViewById(R.id.current_card_index_text);

        // for test
        final int testCount = TarotManager.getInstance().getCardCount();
        for (int i = 0; i < mCardIndexes.length; i++) {
            mCardIndexes[i] = new Random().nextInt(testCount - 1);
            mCardReversed[i] = new Random().nextBoolean();
        }
        List<Integer> testList = new ArrayList<>(testCount);
        for (int i = 0; i < testCount; i++) {
            testList.add(i);
        }
        final TarotChooseAdapter adapter = new TarotChooseAdapter(this, testList);
        adapter.setLooped(true, 100);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ((i + testCount - mChildOffset) % testCount == mCurrentCardIndex) {
                    showChooseAnimation();
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

    private void showCardContent(int index) {
        String tarotLover = TarotManager.getInstance().getTarotLover(mGroupIndexes[index], mCardIndexes[index]);
        int drawable = getResources().getIdentifier("tarot_card_" + (mCardIndexes[index] + 1), "mipmap", getPackageName());
        /*Intent intent = new Intent(TarotChooseLoversActivity.this, TarotSingleReadActivity.class);
        intent.putExtra(TarotSingleReadActivity.INTENT_CARD_IMAGE, drawable);
        intent.putExtra(TarotSingleReadActivity.INTENT_CARD_REVERSED, mCardReversed[index]);
        intent.putExtra(TarotSingleReadActivity.INTENT_CARD_TITLE, mCardTitles[index]);
        intent.putExtra(TarotSingleReadActivity.INTENT_CARD_DESCRIPTION, tarotLover);
        startActivity(intent);*/
    }

    private void showChooseAnimation() {
        if (mChoosedCardCount < mAnimationViews.length) {
            View animView = mAnimationViews[mChoosedCardCount++][0];
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            int width = defaultDisplay.getWidth();
            int height = defaultDisplay.getHeight();
            int[] locationInWindow = new int[2];
            animView.getLocationInWindow(locationInWindow);
            TranslateAnimation animation = new TranslateAnimation(
                    (width - animView.getWidth()) / 2 - locationInWindow[0],
                    0,
                    height - locationInWindow[1] - animView.getHeight(),
                    0);
            animation.setDuration(300);
            animView.setVisibility(View.VISIBLE);
            animView.startAnimation(animation);
            mRecyclerView.smoothScrollBy(mItemViewWidth, 0);

            if (mChoosedCardCount == mAnimationViews.length) {
                animation.setAnimationListener(new AnimationListenerImpl() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        showPreReadingAnimation();
                    }
                });
            }
        }
    }

    private void showPreReadingAnimation() {
        final AtomicInteger finishCount = new AtomicInteger(mAnimationViews.length);
        mChooseLayout.setVisibility(View.GONE);
        for (int i = 0; i < mAnimationViews.length; i++) {
            final View animationView = mAnimationViews[i][0];
            final View finalView = mAnimationViews[i][1];
            final int top = animationView.getTop();
            final int bottom = animationView.getBottom();
            int[] locationInWindow = new int[2];
            animationView.getLocationInWindow(locationInWindow);
            animationView.setPivotX(i / 2f * animationView.getWidth());
            animationView.setPivotY(animationView.getHeight() / 2);
            int[] finalViewLocationInWindow = new int[2];
            finalView.getLocationInWindow(finalViewLocationInWindow);
            final float offset = finalViewLocationInWindow[1] - locationInWindow[1] + (finalView.getHeight() - animationView.getHeight()) / 2;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
            valueAnimator.setDuration(1000)
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            if (Float.compare(value, 1f) >= 0) {
                                animationView.setVisibility(View.GONE);
                                finalView.setVisibility(View.VISIBLE);
                                mPastCardTv.setVisibility(View.VISIBLE);
                                mCurrentCardTv.setVisibility(View.VISIBLE);
                                mFutureCardTv.setVisibility(View.VISIBLE);
                                if (finishCount.decrementAndGet() == 0) {
                                    mReadingText.setVisibility(View.VISIBLE);
                                }
                            } else {
                                animationView.layout(animationView.getLeft(), (int) (top + offset * value),
                                        animationView.getRight(), (int) (bottom + offset * value));
                                animationView.setScaleX(1f + (0.3f * value));
                                animationView.setScaleY(1f + (0.3f * value));
                            }
                        }
                    });
            valueAnimator.start();
        }
    }
}
