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

public class RangeView extends View {

    private Bitmap mDataLineBlue;
    private Bitmap mDataLineRed;
    private Bitmap mCurrentLine;
    //  private Paint mPaint;
    private float mX;

    public RangeView(Context context) {
        super(context);
        this.setDrawingCacheEnabled(true);
        init(context);
    }

    public RangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void init(Context context) {
        mDataLineBlue = drawableToBitmap(context.getDrawable(R.drawable.data_line_oval_blue));
        mDataLineRed = drawableToBitmap(context.getDrawable(R.drawable.data_line_oval_red));
        mCurrentLine = mDataLineBlue;
        //   mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentLine != null && !mCurrentLine.isRecycled()) {
            canvas.drawBitmap(mCurrentLine, mX, 0, null);
        }
    }

    public void setCurrentLine(boolean red) {
        if (red) {
            mCurrentLine = mDataLineRed;
        } else {
            mCurrentLine = mDataLineBlue;
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

    public void updateX(float x) {
        mX = x;
        forceRedraw();
    }

    public void hideView() {
        if (mCurrentLine != null && !mCurrentLine.isRecycled()) {
            mCurrentLine.recycle();
            mCurrentLine = null;
            forceRedraw();
        }
    }

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