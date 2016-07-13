package com.operatorsapp.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

public class ResizeWidthAnimation extends Animation implements Animation.AnimationListener {
    private int mWidth;
    private int mStartWidth;
    private View mLeftView;
    private View mRightView;
    private boolean mOpen;
    private int mNewWith;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;

    public ResizeWidthAnimation(View leftView, View rightView, int width, boolean open) {
        mLeftView = leftView;
        mRightView = rightView;
        mWidth = width;
        mStartWidth = leftView.getWidth();
        mOpen = open;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        mNewWith = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);

        mLeftView.getLayoutParams().width = mNewWith;
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

    private void openWoopList(ViewGroup.MarginLayoutParams mRightLayoutParams) {
        mRightLayoutParams.setMarginStart(mNewWith);
        mRightView.setLayoutParams(mRightLayoutParams);
        mArrowLeft.setVisibility(View.INVISIBLE);
        mArrowRight.setVisibility(View.VISIBLE);
    }

    private void closeWoopList(ViewGroup.MarginLayoutParams mRightLayoutParams) {
        mRightLayoutParams.setMarginStart(mNewWith);
        mRightView.setLayoutParams(mRightLayoutParams);
        mArrowLeft.setVisibility(View.VISIBLE);
        mArrowRight.setVisibility(View.INVISIBLE);
    }
}
