package com.operatorsapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.operatorsapp.R;

public class RangeView2 extends View {
    private Paint mGrayPaint = new Paint();
    private Paint mGreenPaint = new Paint();
    private Paint mBorderPaint = new Paint();
    private Paint mCurrentPaint = new Paint();
    private Paint mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
    private Paint mBorderTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
    private Paint emptyPaint = new Paint();


    float mLowLimit;
    float mHighLimit;
    float mCurrentValue;
    float mAvgValue;
    float border;
    float textPadding;
    float mStandardValue;
    float mHeight;
    private Bitmap avgImage;
    private Bitmap mStandardImage;
    private int mWidth = 1;

    public void setAvgValue(float avgValue) {
        this.mAvgValue = avgValue;
    }

    public float getmStandardValue() {
        return mStandardValue;
    }

    public void setmStandardValue(float mStandardValue) {
        this.mStandardValue = mStandardValue;
    }


    public void setLowLimit(float mLowLimit) {
        this.mLowLimit = mLowLimit;
    }

    public void setHighLimit(float mHighLimit) {
        this.mHighLimit = mHighLimit;
    }

    public void setCurrentValue(float mCurrentValue) {
        this.mCurrentValue = mCurrentValue;
    }

    public void setWidth(int width) {
        mWidth = width;
        postInvalidate();
    }
    public RangeView2(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RangeView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RangeView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.RangeView2, defStyle, 0);


        mLowLimit = a.getFloat(R.styleable.RangeView2_low_limit, 10);
        mHighLimit = a.getFloat(R.styleable.RangeView2_high_limit, 30);
        mCurrentValue = a.getFloat(R.styleable.RangeView2_current_value, 5);
        mAvgValue = a.getFloat(R.styleable.RangeView2_avg_value, 0);
        mStandardValue = a.getFloat(R.styleable.RangeView2_standard_value, 0);
        avgImage = BitmapFactory.decodeResource(getResources(), R.drawable.avga);
        mStandardImage = BitmapFactory.decodeResource(getResources(), R.drawable.standarda);

        a.recycle();

        mHeight = dipToPixels(context, 15);
        border = dipToPixels(context, 12);

        avgImage = Bitmap.createScaledBitmap(avgImage,(int)(mHeight), (int)(mHeight),true);
        mStandardImage = Bitmap.createScaledBitmap(mStandardImage,(int) (mHeight *1.2),(int) (mHeight *1.2),true);
        mGrayPaint.setAntiAlias(true);
        mGrayPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mGrayPaint.setColor(Color.parseColor("#cecece"));

        mGreenPaint.setAntiAlias(true);
        mGreenPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mGreenPaint.setStrokeWidth(mHeight);

        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setStrokeWidth(dipToPixels(context, 3));

        mCurrentPaint.setAntiAlias(true);
        mCurrentPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCurrentPaint.setStrokeWidth(dipToPixels(context, 6));

        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        mTextPaint.setTextSize(dipToPixels(context, 13));

        mBorderTextPaint.setStyle(Paint.Style.FILL);
        mBorderTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        mBorderTextPaint.setTextSize(dipToPixels(context, 13));
        mBorderTextPaint.setColor(Color.BLACK);

        textPadding = dipToPixels(context, 6);

    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        mWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
//        this.setMeasuredDimension(mWidth, parentHeight);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int valueColor;
        if (mCurrentValue >= mLowLimit && mCurrentValue <= mHighLimit)
            valueColor = Color.parseColor("#127510");
        else
            valueColor = Color.RED;
        mCurrentPaint.setColor(valueColor);
        mTextPaint.setColor(valueColor);
        mGreenPaint.setColor(valueColor);

        float percent = mWidth / 100;

        canvas.drawRect(0, getHeight() / 2f - mHeight / 2f, percent * 100, getHeight() / 2f + mHeight / 2f, mGrayPaint);

        if (mCurrentValue >= mLowLimit && mCurrentValue <= mHighLimit) {
            canvas.drawLine(percent * 20, getHeight() / 2f, percent * 80, getHeight() / 2f, mGreenPaint);

            canvas.drawLine(((mCurrentValue - mLowLimit) / (mHighLimit - mLowLimit)) * 60 * percent + percent * 20, getHeight() / 2 - border, percent * 20 + ((mCurrentValue - mLowLimit) / (mHighLimit - mLowLimit)) * 60 * percent, getHeight() / 2 + mHeight / 2, mCurrentPaint);
            canvas.drawText(String.valueOf(mCurrentValue), (((mCurrentValue - mLowLimit) / (mHighLimit - mLowLimit)) * 60 * percent + percent * 20) - mTextPaint.measureText(String.valueOf(mCurrentValue)) / 2, getHeight() / 2 - border - textPadding, mTextPaint);
        } else if (mCurrentValue < mLowLimit) {
            canvas.drawLine(0, getHeight() / 2f, percent * 20, getHeight() / 2f, mGreenPaint);

            canvas.drawLine(percent * 10, getHeight() / 2 - border, percent * 10, getHeight() / 2 + mHeight / 2, mCurrentPaint);
            canvas.drawText(String.valueOf(mCurrentValue), percent * 10 - mTextPaint.measureText(String.valueOf(mCurrentValue)) / 2, getHeight() / 2 - border - textPadding, mTextPaint);
        } else {

            canvas.drawLine(percent * 80, getHeight() / 2f, percent * 100, getHeight() / 2f, mGreenPaint);
            canvas.drawLine(percent * 90, getHeight() / 2 - border, percent * 90, getHeight() / 2 + mHeight / 2, mCurrentPaint);
            canvas.drawText(String.valueOf(mCurrentValue), percent * 90 - mTextPaint.measureText(String.valueOf(mCurrentValue)) / 2, getHeight() / 2 - border - textPadding, mTextPaint);

        }

        if (mAvgValue >= mLowLimit && mAvgValue <= mHighLimit) {
            canvas.drawBitmap(avgImage, (((mAvgValue - mLowLimit) / (mHighLimit - mLowLimit)) * 60 * percent + percent * 20) - avgImage.getWidth() / 2, getHeight() / 2 - avgImage.getHeight() / 2, emptyPaint);
        } else if (mAvgValue < mLowLimit && mAvgValue != 0) {
            canvas.drawBitmap(avgImage, percent * 10 - avgImage.getWidth() / 2, getHeight() / 2 - avgImage.getHeight() / 2, emptyPaint);
        } else if (mAvgValue != 0) {
            canvas.drawBitmap(avgImage, percent * 90 - avgImage.getWidth() / 2, getHeight() / 2 - avgImage.getHeight() / 2, emptyPaint);

        }
        if (mStandardValue >= mLowLimit && mStandardValue <= mHighLimit && mStandardValue != 0) {

            canvas.drawBitmap(mStandardImage, (((mStandardValue - mLowLimit) / (mHighLimit - mLowLimit)) * 60 * percent + percent * 20) - mStandardImage.getWidth() / 2, getHeight() / 2 - mStandardImage.getHeight() / 2, emptyPaint);

        }

        canvas.drawLine(percent * 20, getHeight() / 2f - mHeight / 2f, percent * 20, getHeight() / 2f + mHeight / 2f, mBorderPaint);
        canvas.drawLine(percent * 80, getHeight() / 2f - mHeight / 2f, percent * 80, getHeight() / 2f + mHeight / 2f, mBorderPaint);
        canvas.drawText(String.valueOf(mLowLimit), percent * 20 - mTextPaint.measureText(String.valueOf(mLowLimit)) / 2, getHeight() / 2 + border + textPadding * 2, mBorderTextPaint);
        canvas.drawText(String.valueOf(mHighLimit), percent * 80 - mTextPaint.measureText(String.valueOf(mHighLimit)) / 2, getHeight() / 2 + border + textPadding * 2, mBorderTextPaint);
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

}