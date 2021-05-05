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
public class ProjectionView extends View {

    private Bitmap mCurrentQuantity;
    private Bitmap mProjectionQuantity;
    // private Paint mPaint;

    public ProjectionView(Context context) {
        super(context);
        this.setDrawingCacheEnabled(true);
        init(context);
    }

    public ProjectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
        init(context);
    }

    public void init(Context context) {
        mProjectionQuantity = drawableToBitmap(context.getDrawable(R.drawable.data_projection_quantity_oval));
        mCurrentQuantity = drawableToBitmap(context.getDrawable(R.drawable.data_current_quantity_oval));
        //  mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mProjectionQuantity != null && !mProjectionQuantity.isRecycled()) {
            canvas.drawBitmap(mProjectionQuantity, 0, 0, null);
        }
        if (mCurrentQuantity != null && !mCurrentQuantity.isRecycled()) {
            canvas.drawBitmap(mCurrentQuantity, 0, 0, null);
        }
    }

    public void hideViews() {
        if (mCurrentQuantity != null && !mCurrentQuantity.isRecycled() &&
                mProjectionQuantity != null && !mProjectionQuantity.isRecycled()) {
            mCurrentQuantity.recycle();
            mCurrentQuantity = null;
            mProjectionQuantity.recycle();
            mProjectionQuantity = null;
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void updateWidth(float currentWidth, float projectionWidth) {
        if (currentWidth < 1) {
            currentWidth = 1f;
        }
        if (projectionWidth < 1) {
            projectionWidth = 1f;
        }
        if (mProjectionQuantity != null) {
            mCurrentQuantity = Bitmap.createScaledBitmap(mCurrentQuantity, (int) currentWidth, mCurrentQuantity.getHeight(), false);
        }
        if (mCurrentQuantity != null) {
            if (mProjectionQuantity != null) {
                mProjectionQuantity = Bitmap.createScaledBitmap(mProjectionQuantity, (int) projectionWidth, mProjectionQuantity.getHeight(), false);
            }
        }

        forceRedraw();
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