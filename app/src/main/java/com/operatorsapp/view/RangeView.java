package com.operatorsapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.operatorsapp.R;

/**
 * Created by Admin on 02-Aug-16.
 */
public class RangeView extends View {
    private static final int CURSOR_SIZE = 50;
    private static final int SCREEN_MARGIN = 50;
    private static final float PAINT_WIDTH = 5.0f;

    private Context mContext;
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
        mContext = context;
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

    public void updatePath(int x) {
        mX = x;
        forceRedraw();
    }

//    public static Bitmap drawableToBitmap(Drawable drawable) {
//
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawable.draw(canvas);
//
//        return bitmap;
//    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
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