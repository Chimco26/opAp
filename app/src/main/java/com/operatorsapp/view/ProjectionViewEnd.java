package com.operatorsapp.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.operatorsapp.R;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ProjectionViewEnd extends View {

    private Bitmap mRightViewBlue;
    private Bitmap mRightViewGray;
    private Bitmap mCurrentView;
    //private Paint mPaint;

    public ProjectionViewEnd(Context context) {
        super(context);
        this.setDrawingCacheEnabled(true);
        init(context);
    }

    public ProjectionViewEnd(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
        init(context);
    }

    public void init(Context context) {
        mRightViewBlue = drawableToBitmap(context.getDrawable(R.drawable.data_right_quantity_oval_blue));
        mRightViewGray = drawableToBitmap(context.getDrawable(R.drawable.data_right_quantity_oval_gray));
       // mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentView != null && !mCurrentView.isRecycled()) {
            canvas.drawBitmap(mCurrentView, 0, 0, null);
        }
    }

    public void setCurrentView(boolean blue) {
        if (blue) {
            mCurrentView = mRightViewBlue;
        } else {
            mCurrentView = mRightViewGray;
        }
    }

    public void hideView() {
        if (mCurrentView != null && !mCurrentView.isRecycled()) {
            mCurrentView.recycle();
            mCurrentView = null;
            forceRedraw();
        }
    }

    public void forceRedraw() {
        post(new Runnable() {
            @Override
            public void run() {
                if (getContext() != null && getContext() instanceof Activity && !((Activity) getContext()).isDestroyed()) {

                    invalidate();
                }
            }
        });
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}