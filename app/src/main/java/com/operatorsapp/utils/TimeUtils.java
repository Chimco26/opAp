package com.operatorsapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ParseException;

import com.operatorsapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public static String getDurationTime(Context context, long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
//        millis -= TimeUnit.MINUTES.toMillis(minutes);
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        if (days > 0) {
            return days + context.getString(R.string.days) + String.format(Locale.getDefault(), "%02d:%02d", hours, minutes) + context.getString(R.string.hours);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes) + context.getString(R.string.hours);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTimeFromString(String time) {

        String out = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm");
        try {
            Date date = dateFormat.parse(time);
            out = dateFormat2.format(date);
        } catch (ParseException ignored) {
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return out;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateFromString(String time) {

        String out = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = dateFormat.parse(time);
            out = dateFormat2.format(date);
        } catch (ParseException ignored) {
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return out;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateForJob(String time) {

        String out = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(time);
            out = dateFormat2.format(date);
        } catch (ParseException ignored) {
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return out;
    }


    public static String secondsToTimeFormat(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long sec = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, sec);
    }

}
