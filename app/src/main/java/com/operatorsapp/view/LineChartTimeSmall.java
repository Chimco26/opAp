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

    private void init(Context context) {
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.activity_linechart_time, this, false);

        mTfRegular = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");

        mChart = view.findViewById(R.id.chart1);

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
        xAxis.setLabelCount(5,true); // force only 4 labels as size is constant

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
        leftAxis.setLabelCount(4, true);
        leftAxis.setAxisMaxValue(80f);
        leftAxis.setYOffset(0f);
        leftAxis.setTextSize(14f);
        leftAxis.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void setData(final ArrayList<Entry> values, String[] xValues, final Float lowLimit, final Float highLimit) {
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
        float offsetLeft = mChart.getAxisLeft().getRequiredWidthSpace(mChart.getRendererLeftYAxis()
                .getPaintAxisLabels());

        mChart.resetViewPortOffsets();
        mChart.setViewPortOffsets(offsetLeft, 8f, 0f, 25f);

//        setLimitLines(lowLimit,highLimit,standardValue);


        mChart.post(new Runnable() {
            @Override
            public void run() {
                float max = highLimit;
                float min = lowLimit;
                for (Entry entry : values)
                {
                    float entryY = entry.getY();
                    if(entryY > max)
                    {
                        max = entryY;
                    }
                    if(entryY < min)
                    {
                        min = entryY;
                    }
                }

                float addition = ((max - min) + 1) / 5; // add percentage of full range on each side for better visibility,, adding some for min = max case;

                max += addition;
                min -= addition;

                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.resetAxisMaxValue();
                leftAxis.resetAxisMinValue();
                leftAxis.setAxisMinValue(min);
                leftAxis.setAxisMaxValue(max);
                mChart.zoomOut(); // needed due to chart lib not refreshing.
                mChart.moveViewToX(values.get(values.size() - 1).getX());

                float offsetLeft = mChart.getAxisLeft().getRequiredWidthSpace(mChart.getRendererLeftYAxis()
                                .getPaintAxisLabels());

                mChart.resetViewPortOffsets();
                mChart.setViewPortOffsets(offsetLeft, 8f, 0f, 25f);
                mChart.invalidate();
                mChart.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        float offsetLeft = mChart.getAxisLeft().getRequiredWidthSpace(mChart.getRendererLeftYAxis()
                                .getPaintAxisLabels());

                        mChart.resetViewPortOffsets();
                        mChart.setViewPortOffsets(offsetLeft, 8f, 0f, 25f);
                        mChart.invalidate();
                    }
                });
            }
        });
    }

//    private void setLimitLines(Float lowLimit, Float highLimit, Float standardValue)
//    {
//
//        /* // zero line, removed due to performance cost
//        YAxis leftAxis = mChart.getAxisLeft();
//        LimitLine limitLine1 = new LimitLine(0f, "");
//        limitLine1.setLineColor(ContextCompat.getColor(mContext, R.color.C16));
//        limitLine1.setLineWidth(1f);
//        leftAxis.addLimitLine(limitLine1);
//        */
//
//
//        //addStandardLine(standardValue); // standard line, removed due to performance cost
//        //addLimitLines(lowLimit, highLimit);
//    }

    @SuppressWarnings("unused")
    private void addStandardLine(Float standardValue)
    {
        YAxis leftAxis = mChart.getAxisLeft();
        LimitLine limitLine2 = new LimitLine(standardValue, "");
        limitLine2.setLineColor(ContextCompat.getColor(mContext, R.color.C16));
        limitLine2.setTextSize(16);
        limitLine2.setTextColor(ContextCompat.getColor(mContext, R.color.red_line));
        limitLine2.setLineWidth(1f);
        limitLine2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        leftAxis.addLimitLine(limitLine2);
    }

    @SuppressWarnings("unused")
    private void addLimitLines(Float lowLimit, Float highLimit)
    {
        YAxis leftAxis = mChart.getAxisLeft();
        LimitLine limitLine3 = new LimitLine(lowLimit, "");
        limitLine3.setLineColor(ContextCompat.getColor(mContext, R.color.red_line));
        limitLine3.setTextSize(16);
        limitLine3.setTextColor(ContextCompat.getColor(mContext, R.color.red_line));
        limitLine3.setLineWidth(1f);
        limitLine3.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        leftAxis.addLimitLine(limitLine3);

        LimitLine limitLine4 = new LimitLine(highLimit, "");
        limitLine4.setLineColor(ContextCompat.getColor(mContext, R.color.red_line));
        limitLine4.setTextSize(16);
        limitLine4.setTextColor(ContextCompat.getColor(mContext, R.color.red_line));
        limitLine4.setLineWidth(1f);
        limitLine4.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        leftAxis.addLimitLine(limitLine4);
    }
}
