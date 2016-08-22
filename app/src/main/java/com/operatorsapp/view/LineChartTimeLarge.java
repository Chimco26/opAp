package com.operatorsapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.FormattedStringCache;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.operatorsapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LineChartTimeLarge extends FrameLayout {

    public static final String SAMSUNG = "samsung";

    private LineChart mChart;
    private Context mContext;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    public LineChartTimeLarge(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public LineChartTimeLarge(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public LineChartTimeLarge(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context);
    }

    public void init(Context context) {
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.activity_linechart_time, this, false);

        mTfRegular = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");

        mChart = (LineChart) view.findViewById(R.id.chart1);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // set an alternative background color
        mChart.setBackgroundColor(ContextCompat.getColor(context, R.color.chart_background));

        String strManufacturer = android.os.Build.MANUFACTURER;
        if (strManufacturer.equals(SAMSUNG)) {
            mChart.setViewPortOffsets(100f, 30f, 0f, 70f);
        } else {
            mChart.setViewPortOffsets(30f, 10f, 0f, 30f);
        }

//        mChart.zoomIn();
        mChart.zoom(2, 1, 2, 1);

        setAxis(context, -10, -10, -10);

        addView(view);
    }

    public void setAxis(Context context, float min, float standard, float max) {
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(18f);
        xAxis.setLabelCount(11);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.default_gray));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(60000L * 60); // one minute in millis * 60
        xAxis.setValueFormatter(new AxisValueFormatter() {

            @SuppressLint("SimpleDateFormat")
            private FormattedStringCache.Generic<Long, Date> mFormattedStringCache = new FormattedStringCache.Generic<>(new SimpleDateFormat("HH:mm"));

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Long v = (long) value;
                return mFormattedStringCache.getFormattedValue(new Date(v), v);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisLineColor(ContextCompat.getColor(context, R.color.chart_background));
        leftAxis.setTypeface(mTfLight);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(false);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setLabelCount(15);
        leftAxis.setAxisMaxValue(140f);
        leftAxis.setYOffset(0f);
        leftAxis.setTextSize(18f);
        leftAxis.setTextColor(ContextCompat.getColor(context, R.color.default_gray));

        LimitLine limitLine1 = new LimitLine(0f, "");
        limitLine1.setLineColor(ContextCompat.getColor(context, R.color.C16));
        limitLine1.setLineWidth(1f);
        leftAxis.addLimitLine(limitLine1);

        LimitLine limitLine2 = new LimitLine(standard, "Standard");
        limitLine2.setLineColor(ContextCompat.getColor(context, R.color.C16));
        limitLine2.setTextSize(18);
        limitLine2.setTextColor(ContextCompat.getColor(context, R.color.red_line));
        limitLine2.setLineWidth(1f);
        leftAxis.addLimitLine(limitLine2);

        LimitLine limitLine3 = new LimitLine(min, "Min.");
        limitLine3.setLineColor(ContextCompat.getColor(context, R.color.red_line));
        limitLine3.setTextSize(18);
        limitLine3.setTextColor(ContextCompat.getColor(context, R.color.red_line));
        limitLine3.setLineWidth(1f);
        leftAxis.addLimitLine(limitLine3);

        LimitLine limitLine4 = new LimitLine(max, "Max.");
        limitLine4.setLineColor(ContextCompat.getColor(context, R.color.red_line));
        limitLine4.setTextSize(18);
        limitLine4.setTextColor(ContextCompat.getColor(context, R.color.red_line));
        limitLine4.setLineWidth(1f);
        leftAxis.addLimitLine(limitLine4);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }


    public void setData(final ArrayList<Entry> values) {

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ContextCompat.getColor(mContext, R.color.C16));
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(10f);
//        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setDrawCircleHole(false);


        set1.setCircleRadius(10);
        set1.setCircleColor(ContextCompat.getColor(mContext, R.color.C16));
        set1.setCircleColorHole(ContextCompat.getColor(mContext, R.color.C16));
        set1.setColor(ContextCompat.getColor(mContext, R.color.C16));
        set1.setDrawCircleHole(true);
        set1.setDrawCircles(true);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);
        mChart.animateX(300);

        mChart.post(new Runnable() {
            @Override
            public void run() {
                mChart.moveViewToX(values.get(values.size() - 1).getX());
                mChart.invalidate();
            }
        });
    }
}
