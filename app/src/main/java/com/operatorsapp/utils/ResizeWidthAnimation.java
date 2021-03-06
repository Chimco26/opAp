package com.operatorsapp.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeWidthAnimation extends Animation {
    private int mWidth;
    private int mStartWidth;
    private View mLeftView;

    public ResizeWidthAnimation(View leftView, int width) {
        mLeftView = leftView;
        mWidth = width;
        mStartWidth = leftView.getWidth();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int mNewWith;
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

//    public int getNewWith() {
//        return mNewWith;
//    }
}
