package com.merkmod.achievementtoastlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by rkodekar on 27/11/16.
 */

public class AchievementToast implements AchievementToastView.AnimationEndListener{

    public static long LENGTH_SHORT = 1000;
    public static long LENGTH_MEDIUM = 2000;
    public static long LENGTH_LONG = 3000;

    private AchievementToastView achievementToastView;
    private ViewGroup parentView;
    private int translationY = 0;
    private boolean showCalled = false;
    private boolean mToastCanceled = false;
    private boolean mInflated = false;
    private boolean mVisible = false;

    public AchievementToast setBackgroundColor(Resources resources, int color) {
        String strColor = String.format("#%06X", 0xFFFFFF & resources.getColor(color));
        System.out.println("strcolor is " + strColor);
        achievementToastView.setBackgroundColorRes(Color.parseColor(strColor));
        return this;
    }

    public AchievementToast setBackgroundColor(int color) {
        achievementToastView.setBackgroundColor(color);
        return this;
    }

    public AchievementToast setDuration(long duration) {
        achievementToastView.setDuration(duration);
        return this;
    }


    public AchievementToast setText(String text) {
        achievementToastView.setText(text);
        return this;
    }

    public AchievementToast setTextColor(Resources resources, int colorid) {
        String strColor = String.format("#%06X", 0xFFFFFF & resources.getColor(colorid));
        System.out.println("strcolor is " + strColor);
        achievementToastView.setTextColor(Color.parseColor(strColor));
        return this;
    }

    public AchievementToast setTextColor(int color) {
        achievementToastView.setTextColor(color);
        return this;
    }

    public AchievementToast setIcon(Drawable icon) {
        achievementToastView.setIcon(icon);
        return this;
    }

    public AchievementToast setTranslationY(int pixels){
        translationY = pixels;
        return this;
    }

    public AchievementToast(Context context) {
        achievementToastView = new AchievementToastView(context);
        parentView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parentView.addView(achievementToastView, params);
        ViewHelper.setAlpha(achievementToastView, 0);
        parentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationX(achievementToastView, (parentView.getWidth() - achievementToastView.getWidth()) / 2);
                ViewHelper.setTranslationY(achievementToastView, -achievementToastView.getHeight() + translationY);
                mInflated = true;
                if(!mToastCanceled && showCalled) show();
            }
        },1);

        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                checkZPosition();
            }
        });

        achievementToastView.setAnimationEndListener(this);

    }

    public AchievementToast show(){
        if(!mInflated){
            showCalled = true;
            return this;
        }
        achievementToastView.show();
        ViewHelper.setTranslationX(achievementToastView, (parentView.getWidth() - achievementToastView.getWidth()) / 2);
        ViewHelper.setAlpha(achievementToastView, 0f);
        ViewHelper.setTranslationY(achievementToastView, -achievementToastView.getHeight() + translationY);
        achievementToastView.setVisibility(View.VISIBLE);
        ViewPropertyAnimator.animate(achievementToastView).alpha(1f).translationY(25 + translationY)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300).setStartDelay(0).start();
        checkZPosition();

        return this;
    }

    private void checkZPosition(){
        // If the toast isn't visible, no point in updating all the views
        if(!mVisible) return;

        int pos = parentView.indexOfChild(achievementToastView);
        int count = parentView.getChildCount();
        if(pos != count-1){
            ((ViewGroup) achievementToastView.getParent()).removeView(achievementToastView);
            parentView.requestLayout();
            parentView.addView(achievementToastView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void slideUp(){
       ViewPropertyAnimator viewPropertyAnimator = ViewPropertyAnimator.animate(achievementToastView).setStartDelay(1000).alpha(0f)
                .translationY(-achievementToastView.getHeight() + translationY)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .setDuration(300);
        viewPropertyAnimator.start();
    }

    @Override
    public void onAnimationEndListener() {
        slideUp();

    }
    // with drawable
    public static AchievementToast makeAchievement(Context context, Resources resources,  String text, long duration, int textcolor, int bacgroundColor, Drawable icon) {
        return new AchievementToast(context).setText(text).setDuration(duration).setTextColor(resources,textcolor).setBackgroundColor(resources, bacgroundColor).setIcon(icon);
    }

    public static AchievementToast makeAchievement(Context context, Resources resources,  int resid, long duration, int textcolor, int bacgroundColor, Drawable icon) {
        return new AchievementToast(context).setText(context.getResources().getString(resid)).setDuration(duration).setTextColor(resources,textcolor).setBackgroundColor(resources, bacgroundColor).setIcon(icon);
    }


    public static AchievementToast makeAchievement(Context context, String text, long duration, int textcolor, int bacgroundColor, Drawable icon) {
        return new AchievementToast(context).setText(text).setDuration(duration).setTextColor(context.getResources(),textcolor).setBackgroundColor(context.getResources(), bacgroundColor).setIcon(icon);
    }

    public static AchievementToast makeAchievement(Context context, int resid, long duration, int textcolor, int bacgroundColor, Drawable icon) {
        return new AchievementToast(context).setText(context.getResources().getString(resid)).setDuration(duration).setTextColor(context.getResources(),textcolor).setBackgroundColor(context.getResources(), bacgroundColor).setIcon(icon);
    }


    public static AchievementToast makeAchievement(Context context, String text, long duration,Drawable icon) {
        return new AchievementToast(context).setText(text).setDuration(duration).setIcon(icon);
    }

    public static AchievementToast makeAchievement(Context context, int resid, long duration,Drawable icon) {
        return new AchievementToast(context).setText(context.getResources().getString(resid)).setDuration(duration).setIcon(icon);
    }

    // without drawable

    public static AchievementToast makeAchievement(Context context, Resources resources,  String text, long duration, int textcolor, int bacgroundColor) {
        return new AchievementToast(context).setText(text).setDuration(duration).setTextColor(resources,textcolor).setBackgroundColor(resources, bacgroundColor);
    }

    public static AchievementToast makeAchievement(Context context, Resources resources,  int resid, long duration, int textcolor, int bacgroundColor) {
        return new AchievementToast(context).setText(context.getResources().getString(resid)).setDuration(duration).setTextColor(resources,textcolor).setBackgroundColor(resources, bacgroundColor);
    }


    public static AchievementToast makeAchievement(Context context, String text, long duration, int textcolor, int bacgroundColor) {
        return new AchievementToast(context).setText(text).setDuration(duration).setTextColor(context.getResources(),textcolor).setBackgroundColor(context.getResources(), bacgroundColor);
    }

    public static AchievementToast makeAchievement(Context context, int resid, long duration, int textcolor, int bacgroundColor) {
        return new AchievementToast(context).setText(context.getResources().getString(resid)).setDuration(duration).setTextColor(context.getResources(),textcolor).setBackgroundColor(context.getResources(), bacgroundColor);
    }

    public static AchievementToast makeAchievement(Context context, String text, long duration) {
        return new AchievementToast(context).setText(text).setDuration(duration);
    }

    public static AchievementToast makeAchievement(Context context, int resid, long duration) {
        return new AchievementToast(context).setText(context.getResources().getString(resid)).setDuration(duration);
    }

}

