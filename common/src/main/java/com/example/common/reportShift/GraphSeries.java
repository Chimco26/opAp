package com.example.common.reportShift;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GraphSeries {
    private static final String TAG = GraphSeries.class.getSimpleName();
    @SerializedName("Items")
    @Expose
    private List<Item> items = null;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("MaxValue")
    @Expose
    private Float maxValue;
    @SerializedName("MinValue")
    @Expose
    private Float minValue;
    private List<Item> standardItems = null;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Float maxValue) {
        this.maxValue = maxValue;
    }

    public Float getMinValue() {
        return minValue;
    }

    public void setMinValue(Float minValue) {
        this.minValue = minValue;
    }

    public List<Item> getStandardItems() {
        return standardItems;
    }

    public void setStandardItems(List<Item> standardItems) {
        this.standardItems = standardItems;
    }

    public static void addAveragedPoints(List<Graph> graphs) {
        for (Graph graphItem : graphs) {

//            ArrayList<List<Item>> listArrayList = splitItemsByNull(graphItem.getGraphSeries().get(0).getItems());
            List<Item> listArrayList = graphItem.getGraphSeries().get(0).getItems();

            ArrayList<Item> averagedItems = new ArrayList<>();
            List<List<Item>> smallerLists = splitAndReturn((ArrayList<Item>) listArrayList, 10);

            if (listArrayList.size() > 0) {

                for (List<Item> itemList : smallerLists) {
                    double mean = getMean(itemList);
                    double standardDeviation = calculateStandardDeviation(itemList, mean);
                    for (int i = 0; i < itemList.size(); i++) {

                        if (itemList.get(i).getY() != null && itemList.get(i).getY() >= mean - standardDeviation
                                && itemList.get(i).getY() <= mean + standardDeviation) {
                            averagedItems.add(itemList.get(i));
                        }else {
                            if (itemList.get(i).getY() == null){
                                averagedItems.add(itemList.get(i));
                            }
                        }
                    }
                }
            }
            graphItem.getGraphSeries().get(0).setStandardItems(averagedItems);
            Log.d(TAG, "addAveragedPoints: series: " + graphItem.getGraphSeries().get(0).getItems().size());
            Log.d(TAG, "addAveragedPoints: standard series: " + graphItem.getGraphSeries().get(0).getStandardItems().size());
        }
    }

    private static List<List<Item>> splitAndReturn(ArrayList<Item> numbers,
                                                   int size) {
        List<List<Item>> smallList = new ArrayList<List<Item>>();
        int i = 0;
        while (i + size < numbers.size()) {
            smallList.add(numbers.subList(i, i + size));
            i = i + size;
        }
        smallList.add(numbers.subList(i, numbers.size()));
        return smallList;
    }

    public static double calculateStandardDeviation(List<Item> sd, double mean) {

        double newSum = 0;

        for (int j = 0; j < sd.size(); j++) {
            // put the calculation right in there
            if (sd.get(j).getY() != null) {
                newSum = newSum + ((sd.get(j).getY() - mean) * (sd.get(j).getY() - mean));
            }
        }
        double squaredDiffMean = (newSum) / (sd.size());
        double standardDev = (Math.sqrt(squaredDiffMean));

        return standardDev;
    }

    public static double getMean(List<Item> sd) {
        double sum = 0;

        for (int i = 0; i < sd.size(); i++) {
            if (sd.get(i).getY() != null) {
                sum = sum + sd.get(i).getY();
            }
        }
        return (sum) / (sd.size());
    }
}


