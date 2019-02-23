package com.fuyou.play.view.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.fuyou.play.R;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.tv.HeavyTextView;


public class TarotSingleReadActivity extends BaseActivity {

    public static final String INTENT_CARD_IMAGE = "card_image";
    public static final String INTENT_CARD_REVERSED = "card_reversed";
    public static final String INTENT_CARD_TITLE = "card_title";
    public static final String INTENT_CARD_DESCRIPTION = "card_description";

    protected ImageView mCardImage;
    protected ImageView mRealCardImage;
    protected HeavyTextView mCardDescriptionText;
    protected HeavyTextView mCardTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_tarot_single_read);
        setFullScreen();
        initView();
        mCardImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                showCardAnimation();
            }
        }, 800);
    }

    private void initView() {
        setBackViews(R.id.back_button);
        mCardImage = (ImageView) findViewById(R.id.card_image);
        mRealCardImage = (ImageView) findViewById(R.id.real_card_image);
        mCardTitleText = (HeavyTextView) findViewById(R.id.card_title_text);
        mCardDescriptionText = (HeavyTextView) findViewById(R.id.card_description_text);
        mRealCardImage.setBackgroundResource(getIntent().getIntExtra(INTENT_CARD_IMAGE, 0));
        mCardTitleText.setText(getIntent().getStringExtra(INTENT_CARD_TITLE));
        mCardDescriptionText.setText(getIntent().getStringExtra(INTENT_CARD_DESCRIPTION));
    }

    private void showCardAnimation() {
        mCardImage.setPivotX(mCardImage.getWidth() / 2);
        mCardImage.setPivotY(mCardImage.getHeight() / 2);
        mRealCardImage.setPivotX(mRealCardImage.getWidth() / 2);
        mRealCardImage.setPivotY(mRealCardImage.getHeight() / 2);
        final boolean isReversed = getIntent().getBooleanExtra(INTENT_CARD_REVERSED, false);
        if (isReversed) {
            mRealCardImage.setRotationX(180);
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (Float.compare(value, 1f) == 0) {
                    showTextAnimation();
                } else {
                    if (value > 0.5f) {
                        mRealCardImage.setVisibility(View.VISIBLE);
                        mRealCardImage.setRotationY(180 * (isReversed ? (1 - value) : value));
                        mCardImage.setVisibility(View.GONE);
                    } else {
                        mCardImage.setRotationY(180 * value);
                    }
                }
            }
        });
        valueAnimator.start();
    }

    private void showTextAnimation() {
        mCardTitleText.setVisibility(View.VISIBLE);
        mCardDescriptionText.setVisibility(View.VISIBLE);
        final ViewGroup.LayoutParams layoutParams = mCardDescriptionText.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mCardDescriptionText.getHeight());
        valueAnimator
                .setDuration(1000)
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        layoutParams.height = (int) animation.getAnimatedValue();
                        mCardDescriptionText.setLayoutParams(layoutParams);
                    }
                });
        valueAnimator.start();
    }
}
