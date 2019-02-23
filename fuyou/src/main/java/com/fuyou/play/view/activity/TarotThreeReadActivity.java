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


public class TarotThreeReadActivity extends BaseActivity {

    public static final String INTENT_CARD_IMAGE = "card_image";
    public static final String INTENT_CARD_REVERSED = "card_reversed";
    public static final String INTENT_CARD_DESCRIPTION = "card_description";

    private static final int ROW_COUNT = 3;

    private RowView[] mRowViews = new RowView[ROW_COUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_tarot_three_read);
        setFullScreen();
        setStatusBarTranslucent();
        initView();
    }

    private void initView() {
        setBackViews(R.id.back_button);
        int[] cardImages = getIntent().getIntArrayExtra(INTENT_CARD_IMAGE);
        boolean[] imageReversed = getIntent().getBooleanArrayExtra(INTENT_CARD_REVERSED);
        String[] cardDescriptions = getIntent().getStringArrayExtra(INTENT_CARD_DESCRIPTION);
        for (int i = 0; i < ROW_COUNT; i++) {
            int viewIndex = i + 1;
            int realCardImageId = getResources().getIdentifier("real_card_" + viewIndex + "_image", "id", getPackageName());
            int cardImageId = getResources().getIdentifier("card_" + viewIndex + "_image", "id", getPackageName());
            int titleTextId = getResources().getIdentifier("card_" + viewIndex + "_title_text", "id", getPackageName());
            int DescriptionTextId = getResources().getIdentifier("card_" + viewIndex + "_description_text", "id", getPackageName());
            mRowViews[i] = new RowView(
                    (ImageView) findViewById(realCardImageId),
                    (ImageView) findViewById(cardImageId),
                    (HeavyTextView) findViewById(titleTextId),
                    (HeavyTextView) findViewById(DescriptionTextId),
                    cardImages[i],
                    imageReversed[i],
                    cardDescriptions[i]
            );
        }
    }

    private static class RowView {

        ImageView mRealCardImage;
        ImageView mCardImage;
        HeavyTextView mCardTitleText;
        HeavyTextView mCardDescriptionText;

        boolean mIsImageReversed;

        public RowView(ImageView realCardImage, ImageView cardImage, HeavyTextView cardTitleText, HeavyTextView cardDescriptionText,
                       int imageId, boolean isImageReversed, String cardDescription) {
            mRealCardImage = realCardImage;
            mCardImage = cardImage;
            mCardTitleText = cardTitleText;
            mCardDescriptionText = cardDescriptionText;
            mIsImageReversed = isImageReversed;
            mCardImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCardAnimation();
                }
            });
            mRealCardImage.setBackgroundResource(imageId);
            mCardDescriptionText.setText(cardDescription);
        }

        private void showCardAnimation() {
            mCardImage.setPivotX(mCardImage.getWidth() / 2);
            mCardImage.setPivotY(mCardImage.getHeight() / 2);
            mRealCardImage.setPivotX(mRealCardImage.getWidth() / 2);
            mRealCardImage.setPivotY(mRealCardImage.getHeight() / 2);
            if (mIsImageReversed) {
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
                            mRealCardImage.setRotationY(180 * (mIsImageReversed ? (1 - value) : value));
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
            mCardTitleText.setVisibility(View.GONE);
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
}
