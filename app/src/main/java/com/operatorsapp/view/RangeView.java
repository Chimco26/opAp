package com.operatorsapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.operatorsapp.R;

public class RangeView extends View {

    private Bitmap mDataLine;
    private Paint mPaint;
    private int mX;

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
    private void init(Context context) {
        mDataLine = drawableToBitmap(context.getDrawable(R.drawable.data_line_oval));
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mDataLine, mX, 0, mPaint);
    }

    public void forceRedraw() {
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public void updateX(int x) {
        mX = x;
        forceRedraw();
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
}