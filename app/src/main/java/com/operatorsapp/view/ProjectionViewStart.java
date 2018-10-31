package com.operatorsapp.view;

import android.annotation.TargetApi;
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
public class ProjectionViewStart extends View {

    private Bitmap mLeftView;
    private Bitmap mLeftViewBlue;
    private Bitmap mLeftViewGray;
    //private Paint mPaint;

    public ProjectionViewStart(Context context) {
        super(context);
        this.setDrawingCacheEnabled(true);
        init(context);
    }

    public ProjectionViewStart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
        init(context);
    }

    private void init(Context context) {
        mLeftViewBlue = drawableToBitmap(context.getDrawable(R.drawable.data_left_quantity_oval));
        mLeftViewGray = drawableToBitmap(context.getDrawable(R.drawable.data_left_quantity_oval_gray));
        //   mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLeftView != null && !mLeftView.isRecycled()) {
            canvas.drawBitmap(mLeftView, 0, 0, null);
        }
    }

    public void setCurrentView(boolean blue) {
        if (blue) {
            mLeftView = mLeftViewBlue;
        } else {
            mLeftView = mLeftViewGray;
        }
    }

    public void hideView() {
        if (mLeftView != null && !mLeftView.isRecycled()) {
            mLeftView.recycle();
            mLeftView = null;
            forceRedraw();
        }
    }

    public void forceRedraw() {
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
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