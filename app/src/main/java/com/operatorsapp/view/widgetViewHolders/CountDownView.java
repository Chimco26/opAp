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
import com.operatorsapp.utils.StringUtil;
import com.operatorsapp.utils.TextPaintCanvasUtils;

public class CountDownView extends View {

    private Paint mBasePaint, mDegreesPaint, mCenterPaint, mBackgroundPaint,
            mIndicatorPaint, mIndicatorStrokePaint, mTextPaint;
    private RectF mRect;
    private float centerX;
    private float centerY;
    private float radius;
    private float padding;
    private float percentAngle;
    private float indicatorRadius;
    private float textSize;
    private String text;
    private float startAngle;
    private int totalTimeInMinute;
    private float strokeWidth;
    private boolean isReverse;
    private int backgroundColor;
    private int centerColor;
    private int percentBackgroundColor;
    private int percentColor;
    private int indicatorCenterColor;
    private int indicatorStrokeColor;
    private int textColor;
    private int percentEndBackgroundColor;
    private int percentEndColor;
    private int indicatorEndCenterColor;
    private int indicatorEndStrokeColor;
    private int textEndColor;
    private int endModeTimeInMinute;
    private TypedArray typedArray;
    private float density;

    public CountDownView(Context context) {
        super(context);
        init(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        initVars(context, attributeSet);
        initPaints();
    }

    private void initVars(Context context, AttributeSet attributeSet) {
        density = getResources().getDisplayMetrics().density;
        typedArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.CountDownView, 0, 0);
        if (typedArray != null) {
            padding = typedArray.getInteger(R.styleable.CountDownView_padding, 7) * density;
            startAngle = typedArray.getInteger(R.styleable.CountDownView_startAngle, 0) - 90;//because arc start at 90 degrees and need be in radians for trigonometric functions
            percentAngle = typedArray.getInteger(R.styleable.CountDownView_percentAngle, 0);
            indicatorRadius = typedArray.getInteger(R.styleable.CountDownView_indicatorRadius, 6) * density;
            textSize = typedArray.getInteger(R.styleable.CountDownView_textSize, 28) * density;
            text = typedArray.getString(R.styleable.CountDownView_text);
            totalTimeInMinute = typedArray.getInteger(R.styleable.CountDownView_totalTimeInMinute, 60);
            strokeWidth = typedArray.getInteger(R.styleable.CountDownView_strokeWidth_2, 5) * density;
            backgroundColor = typedArray.getColor(R.styleable.CountDownView_backgroundColor, ContextCompat.getColor(getContext(), R.color.divider_gray));
            centerColor = typedArray.getColor(R.styleable.CountDownView_centerColor, ContextCompat.getColor(getContext(), R.color.white));
            initPercentAndPercentBackgroundColors();
            percentEndBackgroundColor = typedArray.getColor(R.styleable.CountDownView_percentEndBackgroundColor, ContextCompat.getColor(getContext(), R.color.grey_lite));
            percentEndColor = typedArray.getColor(R.styleable.CountDownView_percentEndColor, ContextCompat.getColor(getContext(), R.color.red_line));
            indicatorEndCenterColor = typedArray.getColor(R.styleable.CountDownView_indicatorEndCenterColor, ContextCompat.getColor(getContext(), R.color.white));
            indicatorEndStrokeColor = typedArray.getColor(R.styleable.CountDownView_indicatorEndStrokeColor, ContextCompat.getColor(getContext(), R.color.red_line));
            textEndColor = typedArray.getColor(R.styleable.CountDownView_textEndColor, ContextCompat.getColor(getContext(), R.color.red_line));
            endModeTimeInMinute = typedArray.getInteger(R.styleable.CountDownView_endModeTimeInMinute, totalTimeInMinute);
        }
    }

    private void initPercentAndPercentBackgroundColors() {
        if (typedArray != null) {
            percentBackgroundColor = typedArray.getColor(R.styleable.CountDownView_percentBackgroundColor, ContextCompat.getColor(getContext(), R.color.black));
            percentColor = typedArray.getColor(R.styleable.CountDownView_percentColor, ContextCompat.getColor(getContext(), R.color.blue1));
            indicatorCenterColor = typedArray.getColor(R.styleable.CountDownView_indicatorCenterColor, ContextCompat.getColor(getContext(), R.color.white));
            indicatorStrokeColor = typedArray.getColor(R.styleable.CountDownView_indicatorStrokeColor, ContextCompat.getColor(getContext(), R.color.blue1));
            textColor = typedArray.getColor(R.styleable.CountDownView_textColor, ContextCompat.getColor(getContext(), R.color.blue1));
        } else {
            percentBackgroundColor = ContextCompat.getColor(getContext(), R.color.black);
            percentColor = ContextCompat.getColor(getContext(), R.color.blue1);
            indicatorCenterColor = ContextCompat.getColor(getContext(), R.color.white);
            indicatorStrokeColor = ContextCompat.getColor(getContext(), R.color.blue1);
            textColor = ContextCompat.getColor(getContext(), R.color.blue1);
        }
    }

    private void initPaints() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(backgroundColor);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(centerColor);
        mCenterPaint.setStyle(Paint.Style.FILL);

        mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBasePaint.setStyle(Paint.Style.STROKE);
        mBasePaint.setStrokeWidth(strokeWidth);
        mBasePaint.setColor(percentBackgroundColor);

        mDegreesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDegreesPaint.setStyle(Paint.Style.STROKE);
        mDegreesPaint.setStrokeWidth(strokeWidth);
        mDegreesPaint.setColor(percentColor);

        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(indicatorCenterColor);
        mIndicatorPaint.setStyle(Paint.Style.FILL);

        mIndicatorStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorStrokePaint.setColor(indicatorStrokeColor);
        mIndicatorStrokePaint.setStyle(Paint.Style.STROKE);
        mIndicatorStrokePaint.setStrokeWidth(strokeWidth / 2f);

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

            float start = strokeWidth / 2 + padding;
            float end = 2 * radius - start;

            mRect = new RectF(start, start, end, end);
        }

        canvas.drawCircle(centerX, centerY, radius - strokeWidth / 2f, mBackgroundPaint);
        canvas.drawCircle(centerX, centerY, radius - strokeWidth - padding, mCenterPaint);

        canvas.drawCircle(centerX, centerY, radius - strokeWidth / 2f - padding, mBasePaint);

        canvas.drawArc(mRect, startAngle, percentAngle, false, mDegreesPaint);//-90 because start from 90 degrees after (0 = center top)
        canvas.drawCircle(getPercentArcEndPoint(percentAngle).x, getPercentArcEndPoint(percentAngle).y, indicatorRadius, mIndicatorPaint);
        canvas.drawCircle(getPercentArcEndPoint(percentAngle).x, getPercentArcEndPoint(percentAngle).y, indicatorRadius, mIndicatorStrokePaint);

        text = text == null ? "" : text;
        textSize = TextPaintCanvasUtils.setTextSizeForWidth(mTextPaint, radius - strokeWidth - padding, text);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, centerX, centerY + textSize / 2, mTextPaint);
    }

    private Point getPercentArcEndPoint(double angle) {
        angle = Math.toRadians(angle + startAngle);
        Point point = new Point();
        point.x = (float) (centerX + (radius - indicatorRadius - strokeWidth / 2f) * Math.cos(angle));
        point.y = (float) (centerY + (radius - indicatorRadius - strokeWidth / 2f) * Math.sin(angle));

        return point;
    }

    private class Point {
        private float x;
        private float y;
    }

    public void update(int minute, String minuteEndText) {
        if (isReverse(minute)) {
            percentAngle = minuteToDegree(-minute);
        } else {
            percentAngle = minuteToDegree(totalTimeInMinute - minute);
        }
        text = getMinuteText(minute, minuteEndText);
        invalidate();
    }

    private String getMinuteText(int minute, String minuteEndText) {
//        if (isReverse) {
        return StringUtil.add0ToNumber(minute) + minuteEndText;
//        } else {
//            return minuteEndText + StringUtil.add0ToNumber(minute);
//        }
    }

    private float minuteToDegree(int minute) {
        return minute * 360 / (float) totalTimeInMinute;
    }

    public boolean isReverse(int minute) {
        if (minute <= endModeTimeInMinute) {
            if (!isReverse) {
                setReverse(true);
            }
            return true;
        } else {
            if (isReverse) {
                setReverse(false);
            }
            return false;
        }
    }

    public void setReverse(boolean reverse) {
        isReverse = reverse;
        if (reverse) {
            percentBackgroundColor = percentEndBackgroundColor;
            percentColor = percentEndColor;
            indicatorCenterColor = indicatorEndCenterColor;
            indicatorStrokeColor = indicatorEndStrokeColor;
            textColor = textEndColor;
        } else {
            initPercentAndPercentBackgroundColors();
        }
        initPaints();
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

    public float getIndicatorRadius() {
        return indicatorRadius;
    }

    public void setIndicatorRadius(float indicatorRadius) {
        this.indicatorRadius = indicatorRadius;
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

    public float getStrokeWidth() {
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

    public int getIndicatorCenterColor() {
        return indicatorCenterColor;
    }

    public void setIndicatorCenterColor(int indicatorCenterColor) {
        this.indicatorCenterColor = indicatorCenterColor;
    }

    public int getIndicatorStrokeColor() {
        return indicatorStrokeColor;
    }

    public void setIndicatorStrokeColor(int indicatorStrokeColor) {
        this.indicatorStrokeColor = indicatorStrokeColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getPercentEndBackgroundColor() {
        return percentEndBackgroundColor;
    }

    public void setPercentEndBackgroundColor(int percentEndBackgroundColor) {
        this.percentEndBackgroundColor = percentEndBackgroundColor;
    }

    public int getPercentEndColor() {
        return percentEndColor;
    }

    public void setPercentEndColor(int percentEndColor) {
        this.percentEndColor = percentEndColor;
    }

    public int getIndicatorEndCenterColor() {
        return indicatorEndCenterColor;
    }

    public void setIndicatorEndCenterColor(int indicatorEndCenterColor) {
        this.indicatorEndCenterColor = indicatorEndCenterColor;
    }

    public int getIndicatorEndStrokeColor() {
        return indicatorEndStrokeColor;
    }

    public void setIndicatorEndStrokeColor(int indicatorEndStrokeColor) {
        this.indicatorEndStrokeColor = indicatorEndStrokeColor;
    }

    public int getTextEndColor() {
        return textEndColor;
    }

    public void setTextEndColor(int textEndColor) {
        this.textEndColor = textEndColor;
    }

    public int getEndModeTimeInMinute() {
        return endModeTimeInMinute;
    }

    public void setEndModeTimeInMinute(int endModeTimeInMinute) {
        this.endModeTimeInMinute = endModeTimeInMinute;
    }

    //for arc center correct position
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int min = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(min, min);
    }
}