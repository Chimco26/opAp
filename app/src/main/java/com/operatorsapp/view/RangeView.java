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

import androidx.annotation.RequiresApi;

import com.operatorsapp.R;

public class RangeView extends View {


    private float mX;
    private boolean isRed;

    public RangeView(Context context) {
        super(context);
        this.setDrawingCacheEnabled(true);
    }

    public RangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap mCurrentLine;
        if (isRed) {
            mCurrentLine = drawableToBitmap(this.getContext().getResources().getDrawable(R.drawable.data_line_oval_red));
        } else {
            mCurrentLine = drawableToBitmap(this.getContext().getResources().getDrawable(R.drawable.data_line_oval_blue));
        }
        if (mCurrentLine != null && !mCurrentLine.isRecycled()) {
            canvas.drawBitmap(mCurrentLine, mX, 0, null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setCurrentLine(Context context, boolean red) {
        this.isRed = red;
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

    public void updateX(float x) {
        mX = x;
        forceRedraw();
    }

//    public void hideView() {
//        if (mCurrentLine != null && !mCurrentLine.isRecycled()) {
//            mCurrentLine.recycle();
//            mCurrentLine = null;
//            forceRedraw();
//        }
//    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public float getX() {
        return mX;
    }
}