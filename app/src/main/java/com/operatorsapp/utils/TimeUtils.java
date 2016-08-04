package com.operatorsapp.utils;

import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static final int ONE_MINUTE_IN_SECONDS = 60;
    public static final int ONE_HOUR_IN_SECONDS = ONE_MINUTE_IN_SECONDS * 60;
    public static final int ONE_DAY_IN_SECONDS = ONE_HOUR_IN_SECONDS * 24;

    public static String getStoppedTimeToDisplay(long timeInSeconds) {

        if (timeInSeconds < ONE_MINUTE_IN_SECONDS) {
            return 1 + " m";
        } else if (timeInSeconds < ONE_HOUR_IN_SECONDS) {
            return (timeInSeconds / ONE_MINUTE_IN_SECONDS) + " m";
        } else if (timeInSeconds < ONE_DAY_IN_SECONDS) {
            int minutes = (int) ((timeInSeconds - ((timeInSeconds / ONE_HOUR_IN_SECONDS) * ONE_HOUR_IN_SECONDS)) / ONE_MINUTE_IN_SECONDS);
            return timeInSeconds / ONE_HOUR_IN_SECONDS + ":" + minutes + " h";
        } else {
            int day = (int) (timeInSeconds / ONE_DAY_IN_SECONDS);
            if (day >= 99) {
                return 99 + "+ d";
            } else {
                return day + " d";

            }
        }

    }

    public static String getTimeFromString(String time) {

        String out = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm");
        try {
            Date date = dateFormat.parse(time);
            out = dateFormat2.format(date);
        } catch (ParseException e) {
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return out;
    }

    public static String getDateFromString(String time) {

        String out = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = dateFormat.parse(time);
            out = dateFormat2.format(date);
        } catch (ParseException e) {
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return out;
    }

    public static String getDateForJob(String time) {

        String out = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(time);
            out = dateFormat2.format(date);
        } catch (ParseException e) {
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return out;
    }
}
