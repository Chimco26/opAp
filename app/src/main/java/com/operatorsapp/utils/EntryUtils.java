package com.operatorsapp.utils;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class EntryUtils {

    private static final String TAG = EntryUtils.class.getSimpleName();

    public static ArrayList<ArrayList<Entry>> getAveragedPoints(ArrayList<ArrayList<Entry>> entries) {
        ArrayList<ArrayList<Entry>> toReturn = new ArrayList<>();
        for (ArrayList<Entry> entries1: entries) {

            ArrayList<Entry> averagedItems = new ArrayList<>();
            List<List<Entry>> smallerLists = splitAndReturn(entries1, 10);

            if (entries1.size() > 0) {

                for (List<Entry> itemList : smallerLists) {
                    double mean = getMean(itemList);
                    double standardDeviation = calculateStandardDeviation(itemList, mean);
                    for (int i = 0; i < itemList.size(); i++) {

                        if (itemList.get(i) != null && itemList.get(i).getY() >= mean - standardDeviation
                                && itemList.get(i).getY() <= mean + standardDeviation) {
                            averagedItems.add(itemList.get(i));
                        }else {
                            if (itemList.get(i) == null){
                                averagedItems.add(itemList.get(i));
                            }
                        }
                    }
                }
            }
            toReturn.add(averagedItems);
        }
        return toReturn;
    }

    private static List<List<Entry>> splitAndReturn(ArrayList<Entry> numbers, int size) {
        List<List<Entry>> smallList = new ArrayList<>();
        int i = 0;
        while (i + size < numbers.size()) {
            smallList.add(numbers.subList(i, i + size));
            i = i + size;
        }
        smallList.add(numbers.subList(i, numbers.size()));
        return smallList;
    }

    public static double calculateStandardDeviation(List<Entry> sd, double mean) {

        double newSum = 0;
        int counter = 0;
        for (int j = 0; j < sd.size(); j++) {
            if (sd.get(j) != null) {
                newSum += ((sd.get(j).getY() - mean) * (sd.get(j).getY() - mean));
                counter++;
            }
        }
        try {
            double squaredDiffMean = newSum / counter;
            return (Math.sqrt(squaredDiffMean));
        }catch (Exception e){
            return 0;
        }
    }

    public static double getMean(List<Entry> sd) {
        double sum = 0;
        int counter = 0;
        for (int i = 0; i < sd.size(); i++) {
            if (sd.get(i) != null) {
                sum = sum + sd.get(i).getY();
                counter++;
            }
        }
        try {
            return sum / counter;
        }catch (Exception e){
            return sd.get(0).getY();
        }
    }
}
