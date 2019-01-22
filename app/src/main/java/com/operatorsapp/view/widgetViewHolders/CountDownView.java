package com.operatorsapp.view.widgetViewHolders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.operatorsapp.R;

public class CountDownView extends View {

    private static final int STROKE_WIDTH = 20;
    private Paint mBasePaint, mDegreesPaint, mCenterPaint, mRectPaint;
    private RectF mRect;
    private int centerX, centerY, radius;

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
//        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mRectPaint.setColor(ContextCompat.getColor(getContext(), R.color.magenta));
//        mRectPaint.setStyle(Paint.Style.FILL);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mCenterPaint.setStyle(Paint.Style.FILL);

        mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBasePaint.setStyle(Paint.Style.STROKE);
        mBasePaint.setStrokeWidth(STROKE_WIDTH);
        mBasePaint.setColor(ContextCompat.getColor(getContext(), R.color.blue1));

        mDegreesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDegreesPaint.setStyle(Paint.Style.STROKE);
        mDegreesPaint.setStrokeWidth(STROKE_WIDTH);
        mDegreesPaint.setColor(ContextCompat.getColor(getContext(), R.color.red_line));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // getHeight() is not reliable, use getMeasuredHeight() on first run:
        // Note: mRect will also be null after a configuration change,
        // so in this case the new measured height and width values will be used:
        if (mRect == null)
        {
            // take the minimum of width and height here to be on he safe side:
            centerX = getMeasuredWidth()/ 2;
            centerY = getMeasuredHeight()/ 2;
            radius = Math.min(centerX,centerY);

            // mRect will define the drawing space for drawArc()
            // We have to take into account the STROKE_WIDTH with drawArc() as well as drawCircle():
            // circles as well as arcs are drawn 50% outside of the bounds defined by the radius (radius for arcs is calculated from the rectangle mRect).
            // So if mRect is too large, the lines will not fit into the View
            int startTop = STROKE_WIDTH / 2;
            int startLeft = startTop;

            int endBottom = 2 * radius - startTop;
            int endRight = endBottom;

            mRect = new RectF(startTop, startLeft, endRight, endBottom);
        }

        // just to show the rectangle bounds:
//        canvas.drawRect(mRect, mRectPaint);

        // subtract half the stroke width from radius so the blue circle fits inside the View
        canvas.drawCircle(centerX, centerY, radius - STROKE_WIDTH / 2, mBasePaint);
        // Or draw arc from degree 192 to degree 90 like this ( 258 = (360 - 192) + 90:
        // canvas.drawArc(mRect, 192, 258, false, mBasePaint);

        // draw an arc from 90 degrees to 192 degrees (102 = 192 - 90)
        // Note that these degrees are not like mathematical degrees:
        // they are mirrored along the y-axis and so incremented clockwise (zero degrees is always on the right hand side of the x-axis)
        canvas.drawArc(mRect, 90, 102, false, mDegreesPaint);

        // subtract stroke width from radius so the white circle does not cover the blue circle/ arc
        canvas.drawCircle(centerX, centerY, radius - STROKE_WIDTH, mCenterPaint);
    }
}