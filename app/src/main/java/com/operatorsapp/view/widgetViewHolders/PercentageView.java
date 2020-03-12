package com.operatorsapp.view.widgetViewHolders;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.operatorsapp.R;

public class PercentageView extends View {

    private Paint mBasePaint, mDegreesPaint, mCenterPaint, mBackgroundPaint, mTextPaint;
    private RectF mRect;
    private int centerX, centerY, radius;
    private float padding;
    private float percentAngle;
    private float textSize;
    private String text;
    private float startAngle;
    private int totalTimeInMinute;
    private int strokeWidth;
    private int backgroundColor;
    private int centerColor;
    private int percentBackgroundColor;
    private int percentColor;
    private int textColor;
    private TypedArray typedArray;

    public PercentageView(Context context) {
        super(context);
        init(context, null);
    }

    public PercentageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PercentageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        initVars(context, attributeSet);
        initPaints();
    }

    private void initVars(Context context, AttributeSet attributeSet) {
        float density = getResources().getDisplayMetrics().density;
        typedArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.PercentageView, 0, 0);
        if (typedArray != null) {
            padding = typedArray.getInteger(R.styleable.PercentageView_percentage_padding, 7);
            startAngle = typedArray.getInteger(R.styleable.PercentageView_percentage_startAngle, 0) - 90;//because arc start at 90 degrees and need be in radians for trigonometric functions
            percentAngle = typedArray.getInteger(R.styleable.PercentageView_percentage_percentAngle, 0);
            textSize = typedArray.getInteger(R.styleable.PercentageView_percentage_textSize, 28) * density;
            text = typedArray.getString(R.styleable.PercentageView_percentage_text);
            strokeWidth = typedArray.getInteger(R.styleable.PercentageView_percentage_strokeWidth, 5);
            backgroundColor = typedArray.getColor(R.styleable.PercentageView_percentage_backgroundColor, ContextCompat.getColor(getContext(), R.color.divider_gray));
            centerColor = typedArray.getColor(R.styleable.PercentageView_percentage_centerColor, ContextCompat.getColor(getContext(), R.color.white));
            initPercentAndPercentBackgroundColors();
        }
    }

    private void initPercentAndPercentBackgroundColors() {
        if (typedArray != null) {
            percentBackgroundColor = typedArray.getColor(R.styleable.PercentageView_percentage_percentBackgroundColor, ContextCompat.getColor(getContext(), R.color.red_line));
            percentColor = typedArray.getColor(R.styleable.PercentageView_percentage_percentColor, ContextCompat.getColor(getContext(), R.color.red_line));
            textColor = typedArray.getColor(R.styleable.PercentageView_percentage_textColor, ContextCompat.getColor(getContext(), R.color.red_line));
        } else {
            percentBackgroundColor = ContextCompat.getColor(getContext(), R.color.red_line);
            percentColor = ContextCompat.getColor(getContext(), R.color.red_line);
            textColor = ContextCompat.getColor(getContext(), R.color.red_line);
        }
    }

    private void initPaints() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(backgroundColor);

        mDegreesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDegreesPaint.setStyle(Paint.Style.STROKE);//STROKE
        mDegreesPaint.setStrokeWidth(strokeWidth);
        mDegreesPaint.setColor(percentColor);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(centerColor);
        mCenterPaint.setStyle(Paint.Style.FILL);

        mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBasePaint.setStyle(Paint.Style.STROKE);
        mBasePaint.setStrokeWidth(strokeWidth);
        mBasePaint.setColor(percentBackgroundColor);


        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
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
            centerX = getMeasuredWidth() / 2;
            centerY = getMeasuredHeight() / 2;
            radius = Math.min(centerX, centerY);

            float start = strokeWidth;
            float end = 2 * radius;

            mRect = new RectF(start + strokeWidth, start, end, end - start);
        }

        canvas.drawCircle(centerX, centerY, radius - strokeWidth / 2f, mBackgroundPaint);
        canvas.drawArc(mRect, startAngle, percentAngle, false, mDegreesPaint);//-90 because start from 90 degrees after (0 = center top)
        canvas.drawCircle(centerX, centerY, radius - strokeWidth - padding, mCenterPaint);
        canvas.drawCircle(centerX, centerY, radius - strokeWidth / 2f - padding, mBasePaint);


//        mTextPaint.setTextAlign(Paint.Align.CENTER);
//        text = text == null ? "00:00" : text;
//        canvas.drawText(text, centerX, centerY + textSize / 2, mTextPaint);
    }

    public void update(int percent) {

        percentAngle = percentToDegree(percent);
//        text = "";//todo
        invalidate();
    }

    private float percentToDegree(int percent) {
        return percent * 360 / 100f;
    }

    public float getPadding() {
        return padding;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    public float getPercentAngle() {
        return percentAngle;
    }

    public void setPercentAngle(float percentAngle) {
        this.percentAngle = percentAngle;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getTotalTimeInMinute() {
        return totalTimeInMinute;
    }

    public void setTotalTimeInMinute(int totalTimeInMinute) {
        this.totalTimeInMinute = totalTimeInMinute;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getCenterColor() {
        return centerColor;
    }

    public void setCenterColor(int centerColor) {
        this.centerColor = centerColor;
    }

    public int getPercentBackgroundColor() {
        return percentBackgroundColor;
    }

    public void setPercentBackgroundColor(int percentBackgroundColor) {
        this.percentBackgroundColor = percentBackgroundColor;
    }

    public int getPercentColor() {
        return percentColor;
    }

    public void setPercentColor(int percentColor) {
        this.percentColor = percentColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

}