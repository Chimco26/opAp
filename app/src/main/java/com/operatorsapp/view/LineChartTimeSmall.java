package com.operatorsapp.view;

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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.operatorsapp.R;

import java.util.ArrayList;

public class LineChartTimeSmall extends FrameLayout {

    public static final String SAMSUNG = "samsung";

    private LineChart mChart;
    private Context mContext;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private String[] mXValues;
    private String mXVal = "";

    public LineChartTimeSmall(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public LineChartTimeSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public LineChartTimeSmall(Context context, AttributeSet attrs, int defStyle) {
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
        mChart.setNoDataTextDescription(getContext().getString(R.string.no_data_chart));

        // enable touch gestures
        mChart.setTouchEnabled(false);

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
            mChart.setViewPortOffsets(50f/*55f*/, 15f/*17f*/, 0f, 50f);
        } else {
            mChart.setViewPortOffsets(25f, 8f, 0f, 25f);
        }

//        mChart.zoom(3, 1, 3, 1);
        mChart.zoom(10, 5, 10, 5);

        setAxis(context);

        addView(view);
    }

    private void setAxis(Context context) {
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(14f);
//        xAxis.setLabelCount(5);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        xAxis.setCenterAxisLabels(true);
//        xAxis.setGranularity(60000L * 60); // one minute in millis * 60
//        xAxis.setValueFormatter(new AxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return "" + value;
//            }
//
//            @Override
//            public int getDecimalDigits() {
//                return 0;
//            }
//        });
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if ((int) value % mXValues.length >= 0) {
                    if (!mXValues[(int) value % mXValues.length].equals(mXVal)) {
                        mXVal = mXValues[(int) value % mXValues.length];
                        return mXVal;
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
       /* xAxis.setValueFormatter(new AxisValueFormatter() {

            @SuppressLint("SimpleDateFormat")
            private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mFormat.format(new Date((long) value));
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });*/

        /*xAxis.setValueFormatter(new AxisValueFormatter() {

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
        });*/

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisLineColor(ContextCompat.getColor(context, R.color.chart_background));
        leftAxis.setTypeface(mTfLight);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(false);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setLabelCount(4);
        leftAxis.setAxisMaxValue(80f);
        leftAxis.setYOffset(0f);
        leftAxis.setTextSize(14f);
        leftAxis.setTextColor(ContextCompat.getColor(context, android.R.color.black));

        LimitLine limitLine = new LimitLine(40f, "");
        limitLine.setLineColor(ContextCompat.getColor(context, R.color.C16));
        limitLine.setLineWidth(1f);
        leftAxis.addLimitLine(limitLine);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }


    public void setData(final ArrayList<Entry> values, String[] xValues) {
        mXValues = xValues;
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ContextCompat.getColor(mContext, R.color.C16));
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(3f);
//        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setDrawCircleHole(false);
        set1.setCircleRadius(3);
        set1.setCircleColor(ContextCompat.getColor(mContext, R.color.C16));
        set1.setCircleColorHole(ContextCompat.getColor(mContext, R.color.C16));
        set1.setColor(ContextCompat.getColor(mContext, R.color.C16));
        set1.setDrawCircleHole(true);
        set1.setDrawCircles(true);

        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                //rather than diaplaying value show label
                return entry.getData().toString();
            }
        });

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
