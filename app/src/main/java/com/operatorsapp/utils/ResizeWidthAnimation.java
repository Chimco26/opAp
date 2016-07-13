package com.operatorsapp.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeWidthAnimation extends Animation implements Animation.AnimationListener {
    private int mWidth;
    private int mStartWidth;
    private View mLeftView;
    private View mRightView;
    private boolean mOpen;
    private int mNewWith;

    public ResizeWidthAnimation(View leftView, View rightView, int width, boolean open) {
        mLeftView = leftView;
        mRightView = rightView;
        mWidth = width;
        mStartWidth = leftView.getWidth();
        mOpen = open;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newWidth = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);

        mLeftView.getLayoutParams().width = newWidth;
        mLeftView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void openWoopList(ViewGroup.LayoutParams mLeftLayoutParams, int openWidth, ViewGroup.MarginLayoutParams mRightLayoutParams) {
//        mRightLayout.setLayoutParams(mRightLayoutParams);
//        mArrowLeft.setVisibility(View.INVISIBLE);
//        mArrowRight.setVisibility(View.VISIBLE);
    }

    private void closeWoopList(ViewGroup.LayoutParams mLeftLayoutParams, int closeWidth, ViewGroup.MarginLayoutParams mRightLayoutParams) {
        mLeftLayoutParams.width = closeWidth;
        mRightLayoutParams.setMarginStart(closeWidth);
//        mLeftLayout.requestLayout();
//        mRightLayout.setLayoutParams(mRightLayoutParams);
//        mArrowLeft.setVisibility(View.VISIBLE);
//        mArrowRight.setVisibility(View.INVISIBLE);
    }
}
