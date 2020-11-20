package com.demoapp.swipedemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

public class SwipebleButton extends RelativeLayout {

    private Context context;
    private AttributeSet attrs;
    private AppCompatImageView slidingButtonIv;
    private View buttonSwipeableView;
    private String checkedText;
    private String uncheckedText;
    private int checkedTextColor;
    private int uncheckedTextColor;
    private Drawable checkedIcon;
    private Drawable uncheckedIcon;
    private Drawable uncheckedToggleBackground;
    private Drawable checkedToggleBackground;
    private Drawable uncheckedBackground;
    private Drawable checkedBackground;
    private float textSize;

    public SwipebleButton(Context context) {
        super(context);
    }

    public SwipebleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }

    public SwipebleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwipebleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.button_swipe, this, true);
        slidingButtonIv = view.findViewById(R.id.slidingButtonIv);
        buttonSwipeableView = view.findViewById(R.id.buttonSwipeableView);

        /**
         * Text that displaying when button is checked
         */
        checkedText = context.getString(R.string.checked_text);

        /**
         * Text that displaying when button is unchecked
         */
        uncheckedText = context.getString(R.string.unchecked_text);

        /**
         * Color of the text that displays when button is checked
         */
        checkedTextColor = ContextCompat.getColor(context, android.R.color.white);

        /**
         * Color of the text that displays when button is unchecked
         */
        uncheckedTextColor = ContextCompat.getColor(context, android.R.color.black);

        /**
         * Icon that displays when button is checked
         */
        checkedIcon = ContextCompat.getDrawable(context, R.drawable.ic_stop);

        /**
         * Icon that displays when button is unchecked
         */
        uncheckedIcon = ContextCompat.getDrawable(context, R.drawable.ic_play);

        /**
         * Background of swipeable button that displays when button is unchecked
         */
        uncheckedToggleBackground = ContextCompat.getDrawable(context, R.drawable.shape_unchecked_toggle);

        /**
         * Background of swipeable button that displays when button is checked
         */
        checkedToggleBackground = ContextCompat.getDrawable(context, R.drawable.shape_checked_toggle);

        /**
         * Background that displays when button is unchecked
         */
        uncheckedBackground = ContextCompat.getDrawable(context, R.drawable.shape_scrolling_view_unchecked);

        /**
         * Background that displays when button is checked
         */
        checkedBackground = ContextCompat.getDrawable(context, R.drawable.shape_scrolling_view_checked);

        /**
         * The size of displaying text
         */
        textSize = context.getResources().getDimensionPixelSize(R.dimen.default_text_size);

        if (attrs != null) {
            try {
                parseAttr(attrs);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        updateState();
        updateEnableState();
    }

    private static final long ANIMATION_DURATION = 200;

    private enum StateChangeDirection {
        CHECKED_UNCHECKED,
        UNCHECKED_CHECKED
    }

    private OnSwipedActionListener onSwipedActionListener;

    public void setOnSwipedActionListener(OnSwipedActionListener onSwipedActionListener) {
        this.onSwipedActionListener = onSwipedActionListener;
    }

    interface OnSwipedActionListener {
        void onSwipedListener();

        void onSwipedOnListener();

        void onSwipedOffListener();
    }

    /**
     * Current State
     */
    boolean isChecked = false;

    public void setChecked(boolean checked) {
        isChecked = checked;
        getRootView().post(this::updateState);
    }

    /**
     * Enable click animation
     */
    boolean isClickToSwipeEnable = true;

    public void setClickToSwipeEnable(boolean clickToSwipeEnable) {
        isClickToSwipeEnable = clickToSwipeEnable;
        updateState();
    }

    /**
     * Parameter for setting swipe border for change state
     * from unchecked to checked.
     * Value must be from 0 to 1.
     */
    float swipeProgressToFinish = 0.5f;

    public void setSwipeProgressToFinish(float swipeProgressToFinish) throws Throwable {
        if (swipeProgressToFinish >= 1 || swipeProgressToFinish <= 0) {
            throw new Throwable("Illegal value argument. Available values from 0 to 1");
        }
        this.swipeProgressToFinish = swipeProgressToFinish;
        updateState();
    }

    /**
     * Parameter for setting swipe border for change state
     * from checked to unchecked.
     * Value must be from 0 to 1.
     */
    float swipeProgressToStart = 0.5f;

    public void setSwipeProgressToStart(float swipeProgressToStart) throws Throwable {
        if (swipeProgressToStart >= 1 || swipeProgressToStart <= 0) {
            throw new Throwable("Illegal value argument. Available values from 0 to 1");
        }
        this.swipeProgressToStart = 1 - swipeProgressToStart;
        updateState();
    }

    public void setCheckedText(String checkedText) {
        this.checkedText = checkedText;
        updateState();
    }

    public void setUncheckedText(String uncheckedText) {
        this.uncheckedText = uncheckedText;
        updateState();
    }



    public void setCheckedTextColor(int checkedTextColor) {
        this.checkedTextColor = checkedTextColor;
        updateState();
    }



    public void setUncheckedTextColor(int uncheckedTextColor) {
        this.uncheckedTextColor = uncheckedTextColor;
        updateState();
    }



    public void setCheckedIcon(Drawable checkedIcon) {
        this.checkedIcon = checkedIcon;
        updateState();
    }



    public void setUncheckedIcon(Drawable uncheckedIcon) {
        this.uncheckedIcon = uncheckedIcon;
        updateState();
    }



    public void setUncheckedToggleBackground(Drawable uncheckedToggleBackground) {
        this.uncheckedToggleBackground = uncheckedToggleBackground;
        updateState();
    }



    public void setCheckedToggleBackground(Drawable checkedToggleBackground) {
        this.checkedToggleBackground = checkedToggleBackground;
        updateState();
    }



    public void setUncheckedBackground(Drawable uncheckedBackground) {
        this.uncheckedBackground = uncheckedBackground;
        updateState();
    }



    public void setCheckedBackground(Drawable checkedBackground) {
        this.checkedBackground = checkedBackground;
        updateState();
    }



    public void setTextSize(float textSize) {
        this.textSize = textSize;
        updateState();
    }

    /**
     * Setting is swipeable button enabled at this moment
     */
    boolean isEnabled = true;

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        updateEnableState();
    }

    /**
     * Duration of swipe animation.
     * Time in ms.
     * Value must be greater than 0.
     */
    long animationDuration = ANIMATION_DURATION;

    public void setAnimationDuration(long animationDuration) throws Throwable {
        if (animationDuration <= 0) {
            throw new Throwable("Illegal value argument. Value must be greater than 0.");
        }
        this.animationDuration = animationDuration;
    }

    OnTouchListener onTouchListener = (view, event) -> {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                onButtonMove(event);
                this.getParent().requestDisallowInterceptTouchEvent(true);
                return true;

            case MotionEvent.ACTION_UP:
                onButtonMoved();
                return true;
        }
        return view.onTouchEvent(event);
    };

    private final OnClickListener onClickListener = view -> animateClick();

    /**
     * Setting current state of button with animation
     *
     * @param isChecked set button state
     */
    void setCheckedAnimated(boolean isChecked) {
        if (isChecked) {
            animateToggleToEnd();
        } else {
            animateToggleToStart();
        }
    }

    /**
     * Setting initial toggle coordinate in checked state
     */
    private void updateEnableState() {
        if (this.isEnabled) {
            slidingButtonIv.setOnTouchListener(onTouchListener);
            slidingButtonIv.setOnClickListener(onClickListener);
            buttonSwipeableView.setOnClickListener(onClickListener);
        } else {
            slidingButtonIv.setOnClickListener(null);
            slidingButtonIv.setOnTouchListener(null);
            buttonSwipeableView.setOnClickListener(null);
        }
        buttonSwipeableView.setEnabled(this.isEnabled);
        //buttonSwipeableTv.isEnabled = this.isEnabled
        slidingButtonIv.setEnabled(this.isEnabled);
    }

    /**
     * Update button state and style.
     * Call when attribute change.
     */
    private void updateState() {
        if (this.isChecked) {
            setActivatedStyle();
            setToggleToEnd();
            setTextStartTextPadding();
        } else {
            setDeactivatedStyle();
            setToggleToStart();
            setTextEndTextPadding();
        }
    }

    /**
     * Setting initial toggle coordinate in unchecked state
     */
    private void setToggleToEnd() {
        slidingButtonIv.setY((buttonSwipeableView.getHeight() - slidingButtonIv.getHeight()));
    }

    /**
     * Setting initial toggle coordinate in checked state
     */
    private void setToggleToStart() {
        slidingButtonIv.setY(0F);
    }

    /**
     * Setting initial padding text in unchecked state
     */
    private void setTextStartTextPadding() {
        //buttonSwipeableTv.setPadding(0, 0, slidingButtonIv.height, 0)
    }

    /**
     * Setting initial padding text in checked state
     */
    private void setTextEndTextPadding() {
        //buttonSwipeableTv.setPadding(slidingButtonIv.height, 0, 0, 0)
    }

    /**
     * Animation when toggle returns to start position without changing state
     */
    private void returnToggleToStart() {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator positionAnimator = ValueAnimator.ofFloat(slidingButtonIv.getY(), 0F);
        positionAnimator.setDuration(animationDuration);
        positionAnimator.addUpdateListener(valueAnimator -> slidingButtonIv.setY((Float) positionAnimator.getAnimatedValue()));
        animatorSet.play(positionAnimator);
        animatorSet.start();
    }

    /**
     * Animation when toggle returns to end position without changing state
     */
    private void returnToggleToEnd() {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator positionAnimator = ValueAnimator.ofFloat(
                slidingButtonIv.getY(),
                (buttonSwipeableView.getHeight() - slidingButtonIv.getHeight())
        );
        positionAnimator.setDuration(animationDuration);
        positionAnimator.addUpdateListener(valueAnimator -> {
            slidingButtonIv.setY((Float) positionAnimator.getAnimatedValue());
        });
        animatorSet.play(positionAnimator);
        animatorSet.start();
    }

    /**
     * Move the button to the start with the state changing (with animation)
     */
    private void animateToggleToStart() {
        AnimatorSet animatorSet = new AnimatorSet();

        animateBackgroundChange(StateChangeDirection.CHECKED_UNCHECKED);
        animateToggleChange(StateChangeDirection.CHECKED_UNCHECKED);

        ValueAnimator colorAnimation =
                ValueAnimator.ofObject(new ArgbEvaluator(), checkedTextColor, uncheckedTextColor);
        colorAnimation.setDuration(animationDuration);
        //colorAnimation.addUpdateListener { animator -> buttonSwipeableTv.setTextColor(animator.animatedValue as Int) }

        ValueAnimator positionAnimator = ValueAnimator.ofFloat(slidingButtonIv.getY(), 0F);
        positionAnimator.setDuration(animationDuration);
        positionAnimator.addUpdateListener(valueAnimator -> {
            slidingButtonIv.setY((Float) positionAnimator.getAnimatedValue());
        });

        ValueAnimator paddingAnimation = ValueAnimator.ofInt(0, slidingButtonIv.getHeight());
        paddingAnimation.setDuration(animationDuration);
        paddingAnimation.addUpdateListener(valueAnimator -> {
            //buttonSwipeableTv.setPadding(it.animatedValue as Int, 0, 0, 0)
        });

        ValueAnimator alphaAnimation = ValueAnimator.ofFloat(1F, 0F, 1F);
        alphaAnimation.setDuration(animationDuration);
        alphaAnimation.addUpdateListener(valueAnimator -> {
//            if (it.animatedValue as Float <= 0.3) {
//                if (buttonSwipeableTv.text != uncheckedText) {
//                    buttonSwipeableTv.text = uncheckedText
//                }
//            }
//            buttonSwipeableTv.alpha = alphaAnimation.animatedValue as Float
        });

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setDeactivatedStyle();

                if (onSwipedActionListener != null) {
                    onSwipedActionListener.onSwipedOffListener();
                    onSwipedActionListener.onSwipedListener();
                }
                setChecked(false);
            }
        });

        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(positionAnimator, colorAnimation, alphaAnimation, paddingAnimation);
        animatorSet.start();
    }

    /**
     * Move the button to the end with the state changing (with animation)
     */
    private void animateToggleToEnd() {
        AnimatorSet animatorSet = new AnimatorSet();

        animateBackgroundChange(StateChangeDirection.UNCHECKED_CHECKED);
        animateToggleChange(StateChangeDirection.UNCHECKED_CHECKED);

        ValueAnimator colorAnimation =
                ValueAnimator.ofObject(new ArgbEvaluator(), uncheckedTextColor, checkedTextColor);
        colorAnimation.setDuration(animationDuration);
        //colorAnimation.addUpdateListener { animator -> buttonSwipeableTv.setTextColor(animator.animatedValue as Int) }

        ValueAnimator positionAnimator = ValueAnimator.ofFloat(
                slidingButtonIv.getY(),
                (buttonSwipeableView.getHeight() - slidingButtonIv.getHeight())
        );
        positionAnimator.setDuration(animationDuration);
        positionAnimator.addUpdateListener(valueAnimator -> {
            slidingButtonIv.setY((Float) positionAnimator.getAnimatedValue());
        });

        ValueAnimator alphaAnimation = ValueAnimator.ofFloat(1F, 0F, 1F);
        alphaAnimation.setDuration(animationDuration);
//        alphaAnimation.addUpdateListener {
//            if (it.animatedValue as Float <= 0.3) {
//                if (buttonSwipeableTv.text != checkedText) {
//                    buttonSwipeableTv.text = checkedText
//                }
//            }
//            buttonSwipeableTv.alpha = alphaAnimation.animatedValue as Float
//        }

        ValueAnimator paddingAnimation = ValueAnimator.ofInt(0, slidingButtonIv.getHeight());
        paddingAnimation.setDuration(animationDuration);
//        paddingAnimation.addUpdateListener {
//            buttonSwipeableTv.setPadding(0, 0, it.animatedValue as Int, 0)
//        }

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setActivatedStyle();

                if (onSwipedActionListener != null) {
                    onSwipedActionListener.onSwipedOnListener();
                    onSwipedActionListener.onSwipedListener();
                }

                setChecked(true);
            }
        });

        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(positionAnimator, colorAnimation, alphaAnimation, paddingAnimation);
        animatorSet.start();
    }

    /**
     * Toggle click animation
     */
    private void animateClick() {
        if (this.isClickToSwipeEnable) {
            if (this.isChecked) {
                animateClickToActivate();
            } else {
                animateClickToDeactivate();
            }
        }
    }

    /**
     * An animation that is invoked when a user tries to click on an unchecked button
     */
    private void animateClickToActivate() {
        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(
                        (float) (buttonSwipeableView.getHeight() - slidingButtonIv.getHeight()),
                        (float) ((buttonSwipeableView.getHeight() - slidingButtonIv.getHeight()) - (slidingButtonIv.getHeight() / 2)),
                        (float) (buttonSwipeableView.getHeight() - slidingButtonIv.getHeight())
                );
        positionAnimator.addUpdateListener(valueAnimator -> slidingButtonIv.setY((Float) positionAnimator.getAnimatedValue()));
        animatorSet.play(positionAnimator);
        animatorSet.start();
    }

    /**
     * An animation that is invoked when a user tries to click on an checked button
     */
    private void animateClickToDeactivate() {
        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(
                        0F,
                        (float) (slidingButtonIv.getHeight() / 2),
                        0F
                );
        positionAnimator.addUpdateListener(valueAnimator -> {
            slidingButtonIv.setY((Float) positionAnimator.getAnimatedValue());
        });
        animatorSet.play(positionAnimator);
        animatorSet.start();
    }

    /**
     * Animation change button background.
     *
     * @param direction set animation direction
     */
    private void animateBackgroundChange(StateChangeDirection direction) {
        Drawable[] backgrounds = new Drawable[2];

        if (direction == StateChangeDirection.UNCHECKED_CHECKED) {
            backgrounds[0] = uncheckedBackground;
            backgrounds[1] = checkedBackground;
        } else {
            backgrounds[0] = checkedBackground;
            backgrounds[1] = uncheckedBackground;
        }

        TransitionDrawable backgroundTransition = new TransitionDrawable(backgrounds);
        buttonSwipeableView.setBackground(backgroundTransition);
        backgroundTransition.startTransition((int) animationDuration);
    }

    /**
     * Animation change toggle background.
     *
     * @param direction set animation direction
     */
    private void animateToggleChange(StateChangeDirection direction) {
        Drawable[] backgrounds = new Drawable[2];

        if (direction == StateChangeDirection.UNCHECKED_CHECKED) {
            backgrounds[0] = uncheckedToggleBackground;
            backgrounds[1] = checkedToggleBackground;
        } else {
            backgrounds[0] = checkedToggleBackground;
            backgrounds[1] = uncheckedToggleBackground;
        }

        TransitionDrawable backgroundTransition = new TransitionDrawable(backgrounds);
        slidingButtonIv.setBackground(backgroundTransition);
        backgroundTransition.startTransition((int) animationDuration);
    }

    /**
     * Animation change toggle background.
     *
     * @param event parameter with a new coordinate
     */
    private void onButtonMove(MotionEvent event) {
        float newCoordinates = slidingButtonIv.getY() + event.getY();

        if (slidingButtonIv.getY() >= 0
                && newCoordinates + ((float) slidingButtonIv.getHeight() / 2) < getHeight()
        ) {
            if (slidingButtonIv.getY() + ((float) slidingButtonIv.getHeight() / 2) < newCoordinates
                    || newCoordinates - ((float) slidingButtonIv.getHeight() / 2) > buttonSwipeableView.getY()
            ) {
                slidingButtonIv.setY(newCoordinates - ((float) slidingButtonIv.getHeight() / 2));
            }
        }
    }

    private void onButtonMoved() {
        if (this.isChecked) {
            if (slidingButtonIv.getY() < buttonSwipeableView.getHeight() * swipeProgressToStart) {
                animateToggleToStart();
            } else {
                returnToggleToEnd();
            }
        } else {
            if (slidingButtonIv.getY() > buttonSwipeableView.getHeight() * swipeProgressToFinish) {
                animateToggleToEnd();
            } else {
                returnToggleToStart();
            }
        }
    }

    /**
     * Parse attributes from xml.
     *
     * @param attrs passed attributes from XML file
     */
    private void parseAttr(AttributeSet attrs) throws Throwable {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeableButton);

        setChecked(typedArray.getBoolean(R.styleable.SwipeableButton_isChecked, false));
        setClickToSwipeEnable(typedArray.getBoolean(R.styleable.SwipeableButton_isClickToSwipeEnable, true));
        setSwipeProgressToFinish(typedArray.getFloat(R.styleable.SwipeableButton_thresholdEnd, swipeProgressToFinish));
        setSwipeProgressToStart(1 - typedArray.getFloat(R.styleable.SwipeableButton_thresholdStart, swipeProgressToStart));

        setCheckedText(typedArray.getString(R.styleable.SwipeableButton_textChecked) != null
                ? typedArray.getString(R.styleable.SwipeableButton_textChecked) : context.getString(
                typedArray.getResourceId(
                        R.styleable.SwipeableButton_textChecked,
                        R.string.checked_text
                )
        ));

        setUncheckedText(typedArray.getString(R.styleable.SwipeableButton_textUnchecked) != null
                ? typedArray.getString(R.styleable.SwipeableButton_textUnchecked) : context.getString(
                typedArray.getResourceId(
                        R.styleable.SwipeableButton_textUnchecked,
                        R.string.unchecked_text
                )
        ));

        setCheckedTextColor((typedArray.getInt(R.styleable.SwipeableButton_textColorChecked, 0) != 0) ?
                typedArray.getInt(R.styleable.SwipeableButton_textColorChecked, 0) :
                ContextCompat.getColor(context, typedArray.getResourceId(
                        R.styleable.SwipeableButton_textColorChecked,
                        android.R.color.white
                )));


        setUncheckedTextColor((typedArray.getInt(R.styleable.SwipeableButton_textColorUnChecked, 0) != 0) ?
                typedArray.getInt(R.styleable.SwipeableButton_textColorUnChecked, 0) :
                ContextCompat.getColor(context, typedArray.getResourceId(
                        R.styleable.SwipeableButton_textColorUnChecked,
                        android.R.color.black
                )));


        setCheckedIcon(typedArray.getDrawable(R.styleable.SwipeableButton_checkedIcon) != null
                ? typedArray.getDrawable(R.styleable.SwipeableButton_checkedIcon) : ContextCompat.getDrawable(
                context, typedArray.getResourceId(
                        R.styleable.SwipeableButton_checkedIcon,
                        R.drawable.ic_stop
                )));
        setUncheckedIcon(typedArray.getDrawable(R.styleable.SwipeableButton_uncheckedIcon) != null
                ? typedArray.getDrawable(R.styleable.SwipeableButton_uncheckedIcon) : ContextCompat.getDrawable(
                context, typedArray.getResourceId(
                        R.styleable.SwipeableButton_uncheckedIcon,
                        R.drawable.ic_play
                )));

        setUncheckedToggleBackground(ContextCompat.getDrawable(
                context,
                typedArray.getResourceId(
                        R.styleable.SwipeableButton_uncheckedToggleBackground,
                        R.drawable.shape_unchecked_toggle
                )
        ));

        setCheckedToggleBackground(ContextCompat.getDrawable(
                context,
                typedArray.getResourceId(
                        R.styleable.SwipeableButton_checkedToggleBackground,
                        R.drawable.shape_checked_toggle
                )
        ));

        setCheckedBackground(ContextCompat.getDrawable(
                context,
                typedArray.getResourceId(
                        R.styleable.SwipeableButton_checkedBackground,
                        R.drawable.shape_scrolling_view_checked
                )
        ));
        setUncheckedBackground(ContextCompat.getDrawable(
                context,
                typedArray.getResourceId(
                        R.styleable.SwipeableButton_uncheckedBackground,
                        R.drawable.shape_scrolling_view_unchecked
                )
        ));

        setTextSize((typedArray.getDimensionPixelSize(
                R.styleable.SwipeableButton_textSize, 0) != 0) ?
                typedArray.getDimensionPixelSize(R.styleable.SwipeableButton_textSize, 0) :
                context.getResources().getDimensionPixelSize(R.dimen.default_text_size)
        );

        setAnimationDuration((long) typedArray.getFloat(
                R.styleable.SwipeableButton_durationAnimation,
                animationDuration
        ));

        typedArray.recycle();
    }

    private void setActivatedStyle() {
        buttonSwipeableView.setBackground(checkedBackground);
        slidingButtonIv.setBackground(checkedToggleBackground);
        slidingButtonIv.setImageDrawable(checkedIcon);
//        if (buttonSwipeableTv.text != checkedText) {
//            buttonSwipeableTv.text = checkedText
//        }
//        buttonSwipeableTv.textSize = textSize
//        buttonSwipeableTv.setTextColor(checkedTextColor)
    }

    private void setDeactivatedStyle() {
        buttonSwipeableView.setBackground(uncheckedBackground);
        slidingButtonIv.setBackground(uncheckedToggleBackground);
        slidingButtonIv.setImageDrawable(uncheckedIcon);
//        if (buttonSwipeableTv.text != uncheckedText) {
//            buttonSwipeableTv.text = uncheckedText
//        }
//        buttonSwipeableTv.textSize = textSize
//        buttonSwipeableTv.setTextColor(uncheckedTextColor)
    }
}
