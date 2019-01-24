package com.operatorsapp.view.widgetViewHolders.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.operatorsapp.R;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class PercentRoundBarForNatan extends AppCompatImageView {

    private final Paint mFillPaint = new Paint();

    private final Paint mBarPaint = new Paint();

    private final Paint mPercentTextPaint = new Paint();

    private final Paint mThumbPaint = new Paint();

    private final Paint mEraser=new Paint();

    {
        mFillPaint.setStyle(Paint.Style.STROKE);

        mBarPaint.setStyle(Paint.Style.STROKE);

        mThumbPaint.setStyle(Paint.Style.STROKE);

        mEraser.setStyle(Paint.Style.FILL);

        mEraser.setColor(Color.WHITE);
    }

    private final int selectedBarColor;

    private final int defaultBarColor;

    private final int defaultFillColor;

    private final int selectedFillColor;

    private float thumbRadius;

    @SuppressLint("ResourceType")
    public PercentRoundBarForNatan(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PercentRoundBarForNatan, 0, 0);

        int percentTextColor = typedArray.getColor(R.styleable.PercentRoundBarForNatan_percentTextColor, Color.BLACK);

        selectedBarColor = typedArray.getColor(R.styleable.PercentRoundBarForNatan_selectedBarColor, Color.BLACK);

        defaultBarColor = typedArray.getColor(R.styleable.PercentRoundBarForNatan_defaultBarColor, Color.BLACK);

        selectedFillColor = typedArray.getColor(R.styleable.PercentRoundBarForNatan_selectedFillColor, Color.BLACK);

        defaultFillColor = typedArray.getColor(R.styleable.PercentRoundBarForNatan_defaultFillColor, Color.BLACK);

        mPercent = typedArray.getInteger(R.styleable.PercentRoundBarForNatan_percent, 0);

        thumbRadius = typedArray.getDimension(R.styleable.PercentRoundBarForNatan_thumbRadius, 0);

        typedArray.recycle();

    }

    private int mPercent = 0;

    public void setPercent(int percent) {
        this.mPercent = percent;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        drawPercent(canvas);

    }

    private void drawPercent(Canvas canvas) {

        boolean isSelected = mPercent >= 90;

        mFillPaint.setColor(isSelected ? selectedFillColor : defaultFillColor);

        mBarPaint.setColor(isSelected ? selectedBarColor : defaultBarColor);

        mThumbPaint.setColor(isSelected ? selectedBarColor : defaultFillColor);

        int width = getWidth() / 2;

        int height = getHeight() / 2;

        int radius = Math.min(width, height);

        int strokeWidth = radius / 7;

        mThumbPaint.setStrokeWidth(strokeWidth);

        radius -= strokeWidth / 2 + thumbRadius;

        float x, y;

        Path path = new Path();

        path.moveTo(x = width, y = height - radius);

        mBarPaint.setStrokeWidth(strokeWidth);

        for (float angle = 0; angle >= -360 * (100 - mPercent) / 100; angle--) {

            path.lineTo(
                    (float) (width + radius * sin(toRadians(angle))),
                    (float) (height - radius * cos(toRadians(angle))));

        }

        canvas.drawPath(path, mBarPaint);

        mFillPaint.setStrokeWidth(strokeWidth);

        path.reset();

        path.moveTo(x = width, y = height - radius);

        for (float angle = 0; angle <= 360 * mPercent / 100; angle++) {

            path.lineTo(
                    x = (float) (width + radius * sin(toRadians(angle))),
                    y = (float) (height - radius * cos(toRadians(angle))));

        }

        canvas.drawPath(path, mFillPaint);

        canvas.drawCircle(x, y, thumbRadius, mEraser);

        canvas.drawCircle(x, y, thumbRadius, mThumbPaint);

        drawPercentText(canvas, width, height, radius);

    }

    private void drawPercentText(Canvas canvas, int width, int height, int radius) {

        int textSize = 2 * radius / 3;

        mPercentTextPaint.setColor(Color.BLACK);

        mPercentTextPaint.setTextAlign(Paint.Align.CENTER);

        mPercentTextPaint.setTextSize(textSize);

        mPercentTextPaint.setTypeface(Typeface.DEFAULT);

        canvas.drawText(mPercent / 10f + "", width, height + 5 * textSize / 12f, mPercentTextPaint);
    }
}
