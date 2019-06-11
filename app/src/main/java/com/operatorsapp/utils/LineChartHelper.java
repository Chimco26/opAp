package com.operatorsapp.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.example.common.reportShift.Item;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LineChartHelper {

    public static void setLimitLine(LineChart mGraphView, float minValue, float maxValue) {

        LimitLine ll1 = new LimitLine(maxValue, "");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(minValue, "");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP );
        ll2.setTextSize(10f);

        // draw limit lines behind data instead of on top
        mGraphView.getAxisLeft().setDrawLimitLinesBehindData(true);

        // add limit lines
        mGraphView.getAxisLeft().addLimitLine(ll1);
        mGraphView.getAxisLeft().addLimitLine(ll2);
    }

    public static void setXAxisStyle(LineChart graphView) {

        XAxis xAxis = graphView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(0.1f);
        xAxis.setValueFormatter(new AxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return getXFormat().format(new Date((long) value));
            }
            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
    }
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getXFormat() {
        return new SimpleDateFormat("HH:mm");
    }
    public static void setYAxisStyle(LineChart graphView) {

        YAxis leftAxis = graphView.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        YAxis rightAxis = graphView.getAxisRight();
        rightAxis.setEnabled(false);
        graphView.getAxisRight().setEnabled(false);
    }

    public static ArrayList<List<Item>> splitItemsByNull(List<Item> items) {

        ArrayList<List<Item>> listArrayList = new ArrayList<>();
        List<Item> items1 = new ArrayList<>();

        for (Item item : items) {

            if (item.getY() != null) {

                items1.add(item);

            } else {

                listArrayList.add(items1);

                items1 = new ArrayList<>();
            }

        }
        listArrayList.add(items1);

        return listArrayList;
    }
    static float lerp(float point1, float point2, float dist) {
        return point1 + dist * (point2 - point1);
    }
}
//    ArrayList<List<Item>> listArrayList1 = new ArrayList<>();
//    List<Item> items2 = new ArrayList<>();
//        for (int i = 0; i< listArrayList.size(); i++){
//        for (int j = 0; j < listArrayList.get(i).size(); j++) {
//        items2.add(listArrayList.get(i).get(j));
//        if (j < listArrayList.get(i).size() - 1) {
//        items2.add(new Item(String.valueOf(lerp(Float.valueOf(listArrayList.get(i).get(j).getX()), Float.valueOf(listArrayList.get(i).get(j + 1).getX()), 0.5f)),
//        Double.valueOf(lerp(listArrayList.get(i).get(j).getY().floatValue(), listArrayList.get(i).get(j + 1).getY().floatValue(), 0.5f))));
//        }
//        }
//        }