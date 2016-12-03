package com.merkmod.achievementtoastlibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;


/**
 * Created by rkodekar on 27/11/16.
 */

public class AchievementToastView extends View {

    private AnimationEndListener animationEndListener;

    private boolean outOfBounds;

    private String text = "";

    private Drawable icon;

    private long previousUpdate = 0;

    private Rect iconBounds = new Rect();
    private Rect textBounds = new Rect();

    private int TEXT_WIDTH = 140; // in DP
    private int TEXT_SIZE = 24;
    private int ICON_WIDTH = 40;
    private int TOAST_HEIGHT = 60;
    private int LINE_WIDTH = 3;
    private float WIDTH_SCALE = 0f;
    private int MARQUE_STEP = 1;


    private ValueAnimator valueAnimator;
    private Path toastPath = new Path();
    Paint textPaint = new Paint();

    private RectF iconRect = new RectF();


    Paint backPaint = new Paint();
    private int result;

    public void setDuration(long duration) {
        valueAnimator.setDuration(duration);
    }

    public void setBackgroundColorRes(int backgroundColorRes) {
        backPaint.setColor(backgroundColorRes);
        iconPaint.setColor(backgroundColorRes);
    }


    public void setTextColor(int textColor) {
        textPaint.setColor(textColor);
    }

    public void setText(String text) {
        this.text = text;
        calculateBounds();
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    Paint iconPaint = new Paint();

    public AchievementToastView(Context context) {
        super(context);
        textPaint.setTextSize(22);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);

        backPaint.setColor(Color.BLACK);
        backPaint.setAntiAlias(true);

        iconPaint.setColor(Color.BLACK);
        iconPaint.setAntiAlias(true);

        TEXT_WIDTH = Utils.dp2px(context, TEXT_WIDTH);
        TEXT_SIZE = Utils.dp2px(context, TEXT_SIZE);
        ICON_WIDTH = Utils.dp2px(context, ICON_WIDTH);
        TOAST_HEIGHT = Utils.dp2px(context, TOAST_HEIGHT);
        LINE_WIDTH = Utils.dp2px(context, LINE_WIDTH);
        MARQUE_STEP = Utils.dp2px(context, MARQUE_STEP);

        int padding = (TOAST_HEIGHT - ICON_WIDTH) / 2;
        iconBounds = new Rect(TOAST_HEIGHT + TEXT_WIDTH - padding, padding, TOAST_HEIGHT + TEXT_WIDTH - padding + ICON_WIDTH, ICON_WIDTH + padding);

        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                postInvalidate();
            }
        });

       valueAnimator.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                animationEndListener.onAnimationEndListener();
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
        calculateBounds();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float ws = Math.max(1f - WIDTH_SCALE, 0f);
        if (text.length() == 0) ws = 0;

        float translateLoad = (1f - ws) * (ICON_WIDTH + TEXT_WIDTH);
        float leftMargin = translateLoad / 2;
        float textOpactity = Math.max(0, ws * 10f - 9f);
        textPaint.setAlpha((int) (textOpactity * 255));

        int circleOffset = (int) (TOAST_HEIGHT * 2 * (Math.sqrt(2) - 1) / 3);
        int th = TOAST_HEIGHT;

        float totalWidth;
        if (icon != null) {

            totalWidth = leftMargin * 2 + th + ws * (ICON_WIDTH + TEXT_WIDTH) - translateLoad;

        } else {
            totalWidth = leftMargin * 2 + th + ws * (TEXT_WIDTH) - translateLoad;
        }
        toastPath.reset();
        toastPath.moveTo(leftMargin + th / 2, 0);
        if (icon != null) {
            toastPath.rLineTo(ws * (ICON_WIDTH + TEXT_WIDTH), 0);
        } else {
            toastPath.rLineTo(ws * (TEXT_WIDTH), 0);
        }
        toastPath.rCubicTo(circleOffset, 0, th / 2, th / 2 - circleOffset, th / 2, th / 2);

        toastPath.rCubicTo(0, circleOffset, circleOffset - th / 2, th / 2, -th / 2, th / 2);
        if (icon != null) {
            toastPath.rLineTo(ws * (-ICON_WIDTH - TEXT_WIDTH), 0);
        } else {
            toastPath.rLineTo(ws * (-TEXT_WIDTH), 0);
        }
        toastPath.rCubicTo(-circleOffset, 0, -th / 2, -th / 2 + circleOffset, -th / 2, -th / 2);
        toastPath.rCubicTo(0, -circleOffset, -circleOffset + th / 2, -th / 2, th / 2, -th / 2);
        canvas.drawPath(toastPath, backPaint);
        toastPath.reset();

        if (icon != null) {
            canvas.drawCircle(iconRect.centerX(), iconRect.centerY(), iconRect.height() / 1.9f, backPaint);
            icon.setBounds(iconBounds);
            icon.draw(canvas);
            toastPath.reset();
        }

        canvas.save();

        canvas.translate((totalWidth - TOAST_HEIGHT) / 2, 0);
        super.onDraw(canvas);
        canvas.restore();
        int yPos = (int) ((th / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));

        if (outOfBounds) {
            float shift = 0;
            if (previousUpdate == 0) {
                previousUpdate = System.currentTimeMillis();
            } else {
                shift = ((float) (System.currentTimeMillis() - previousUpdate) / 16f) * MARQUE_STEP;

                if (shift - TEXT_WIDTH > textBounds.width()) {
                    previousUpdate = 0;
                }
            }
            canvas.clipRect(th / 2, 0, th / 2 + TEXT_WIDTH, TOAST_HEIGHT);
            canvas.drawText(text, th / 2 - shift + TEXT_WIDTH, yPos, textPaint);
        } else {
            canvas.drawText(text, 0, text.length(), th / 2 + (TEXT_WIDTH - textBounds.width()) / 2, yPos, textPaint);
        }
    }

    private void calculateBounds() {
        outOfBounds = false;
        previousUpdate = 0;
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        if (textBounds.width() > TEXT_WIDTH) {
            int textSize = TEXT_SIZE;
            while (textSize > Utils.dp2px(getContext(), 13) && textBounds.width() > TEXT_WIDTH) {
                textSize--;
                textPaint.setTextSize(textSize);
                textPaint.getTextBounds(text, 0, text.length(), textBounds);
            }
            if (textBounds.width() > TEXT_WIDTH) {
                outOfBounds = true;
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text

            if (icon != null) {
                result = ICON_WIDTH + TEXT_WIDTH + TOAST_HEIGHT;
            } else {
                result = TEXT_WIDTH + TOAST_HEIGHT;
            }
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            result = TOAST_HEIGHT;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    public void show() {
        WIDTH_SCALE = 0f;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) valueAnimator.removeAllUpdateListeners();
    }


    public abstract class AnimatorEndListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {

        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }

    public void setAnimationEndListener(AnimationEndListener animationEndListener) {
        this.animationEndListener = animationEndListener;
    }

    public interface AnimationEndListener {
        void onAnimationEndListener();
    }
}
