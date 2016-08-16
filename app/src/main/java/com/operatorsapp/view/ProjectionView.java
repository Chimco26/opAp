package com.operatorsapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.operatorsapp.R;

/**
 * Created by Admin on 02-Aug-16.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ProjectionView extends View {
    private static final int CURSOR_SIZE = 50;
    private static final int SCREEN_MARGIN = 50;
    private static final float PAINT_WIDTH = 5.0f;

    private Context mContext;
    private Bitmap mCurrentQuantity;
    private Bitmap mExpectedQuantity;
    private Bitmap mLeftView;
    private Paint mPaint;
    private float mCurrentWidth;
    private float mExpectedWidth;
    private Canvas mCanvas;

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

    private void init(Context context) {
//        mCurrentQuantity = drawableToBitmap(context.getDrawable(R.drawable.data_current_quantity_oval));
//        mExpectedQuantity = drawableToBitmap(context.getDrawable(R.drawable.data_expected_quantity_oval));
        mLeftView = drawableToBitmap(context.getDrawable(R.drawable.data_left_quantity_oval));
        mPaint = new Paint();
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        if (mExpectedQuantity != null) {
            canvas.drawBitmap(mExpectedQuantity, 0, 0, mPaint);
        }
        if (mCurrentQuantity != null) {
            canvas.drawBitmap(mCurrentQuantity, 0, 0, mPaint);
        }
        if (mLeftView != null) {
            canvas.drawBitmap(mLeftView, 0, 0, mPaint);
        }
    }

    public void setIsLeftView(boolean isLeft) {
        if (isLeft) {
            mCurrentQuantity = null;
            mExpectedQuantity = null;
        } else {
            mCurrentQuantity = drawableToBitmap(mContext.getDrawable(R.drawable.data_current_quantity_oval));
            mExpectedQuantity = drawableToBitmap(mContext.getDrawable(R.drawable.data_expected_quantity_oval));
        }
    }

    public void hideViews() {
        mLeftView = null;
        mCurrentQuantity = null;
        mExpectedQuantity = null;
    }

    public void forceRedraw() {
        post(new Runnable() {
            @Override
            public void run() {

                invalidate();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void updateWidth(float projectionWidth, float targetWidth) {
//        mCurrentWidth = currentWidth;
//        mExpectedWidth = expectedWidth;

        mCurrentQuantity = Bitmap.createScaledBitmap(mCurrentQuantity, (int) projectionWidth, mCurrentQuantity.getHeight(), false);

//        Drawable d = mContext.getDrawable(R.drawable.data_current_quantity_oval);
//        d.setBounds(0,0, (int) projectionWidth, 0);
//        mCurrentQuantity = drawableToBitmap(d);

//        mCurrentQuantity.setWidth((int) projectionWidth);

//        Drawable dr = mContext.getDrawable(R.drawable.data_current_quantity_oval);
//        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
//        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) projectionWidth, 0, true));
//        mCurrentQuantity = drawableToBitmap(d);

//        ShapeDrawable shapeDrawable1 = (ShapeDrawable) mContext.getDrawable(R.drawable.data_current_quantity_oval);
//        Shape a = new OvalShape();
//        a.resize(projectionWidth, 0);
//        shapeDrawable1.setShape(a);
//
//        ShapeDrawable shapeDrawable2 = (ShapeDrawable) mContext.getDrawable(R.drawable.data_expected_quantity_oval);
//        Shape b = new OvalShape();
//        b.resize(targetWidth, 0);
//        shapeDrawable2.setShape(b);


        // todo if... change color
//        GradientDrawable bgShape = (GradientDrawable) mContext.getDrawable(R.drawable.data_current_quantity_oval);
//        bgShape.setColor(Color.BLACK);

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

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

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