package com.operatorsapp.view.widgetViewHolders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.operatorsapp.R;

public class CountDownView extends View {

    private static final int STROKE_WIDTH = 5;
    private Paint mBasePaint, mDegreesPaint, mCenterPaint, mBackgroundPaint,
            mIndicatorPaint, mIndicatorStrokePaint, mTextPaint;
    private RectF mRect;
    private int centerX, centerY, radius;
    private float padding;
    private int percentAngle;
    private float indicatorRadius;
    private float textSize;
    private String text;

    public CountDownView(Context context) {
        super(context);
        init();
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        text = "20:00";
        float density = getResources().getDisplayMetrics().density;
        padding = 7 * density;
        percentAngle = 270;
        indicatorRadius = 6 * density;
        textSize = 28 * density;
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.divider_gray));

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mCenterPaint.setStyle(Paint.Style.FILL);

        mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBasePaint.setStyle(Paint.Style.STROKE);
        mBasePaint.setStrokeWidth(STROKE_WIDTH);
        mBasePaint.setColor(ContextCompat.getColor(getContext(), R.color.black));

        mDegreesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDegreesPaint.setStyle(Paint.Style.STROKE);
        mDegreesPaint.setStrokeWidth(STROKE_WIDTH);
        mDegreesPaint.setColor(ContextCompat.getColor(getContext(), R.color.blue1));

        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mIndicatorPaint.setStyle(Paint.Style.FILL);

        mIndicatorStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorStrokePaint.setColor(ContextCompat.getColor(getContext(), R.color.blue1));
        mIndicatorStrokePaint.setStyle(Paint.Style.STROKE);
        mIndicatorStrokePaint.setStrokeWidth(STROKE_WIDTH / 2);

        mTextPaint = new Paint();
        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.blue1));
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        mTextPaint.setTextSize(textSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // getHeight() is not reliable, use getMeasuredHeight() on first run:
        // Note: mRect will also be null after a configuration change,
        // so in this case the new measured height and width values will be used:
        if (mRect == null) {
            centerX = getMeasuredWidth()/ 2;
            centerY = getMeasuredHeight()/ 2;
            radius = Math.min(centerX,centerY);

            float start = (STROKE_WIDTH / 2) + padding;
            float end = (2 * radius) - start - padding;

            mRect = new RectF(start, start, end, end);
        }

        canvas.drawCircle(centerX, centerY, radius - STROKE_WIDTH / 2, mBackgroundPaint);
        canvas.drawCircle(centerX, centerY, radius - STROKE_WIDTH - padding, mCenterPaint);

        canvas.drawCircle(centerX, centerY, radius - STROKE_WIDTH / 2 - padding, mBasePaint);

        canvas.drawArc(mRect, -90, -percentAngle, false, mDegreesPaint);//-90 because start from 90 degrees after (0 = center top)
        canvas.drawCircle(getPercentArcEndPoint(percentAngle).x, getPercentArcEndPoint(percentAngle).y, indicatorRadius, mIndicatorPaint);
        canvas.drawCircle(getPercentArcEndPoint(percentAngle).x, getPercentArcEndPoint(percentAngle).y, indicatorRadius, mIndicatorStrokePaint);

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, centerX, centerY + textSize/2, mTextPaint);
    }

    private Point getPercentArcEndPoint(double angle){
        angle = -Math.toRadians(angle - 90);//because arc start at 90 degrees and need be in radians for trigonometric functions
        Point point = new Point();
        point.x = (int) (centerX + (radius - indicatorRadius - STROKE_WIDTH) * Math.cos(angle));
        point.y = (int) (centerY + (radius - indicatorRadius - STROKE_WIDTH) * Math.sin(angle));

        return point;
    }
}