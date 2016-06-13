package com.operatorsapp.utils;

import com.zemingo.logrecorder.ZLogger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by slava-android on 1/31/2016.
 */
public class DateUtils
{
    private static final String LOG_TAG = DateUtils.class.getSimpleName();
    private static final int DAY = 1000 * 60 * 60 * 24;

    public static String getHistoryDate(int numberOfDaysBack)
    {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("d MMM");
        String date = format.format(new Date(currentTime - numberOfDaysBack * DAY));
        ZLogger.v(LOG_TAG, "getHistoryDate(), " + date);
        return date;
    }

    public static String getFormattedToYesterday()
    {
        long currentTime = System.currentTimeMillis() - DAY;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date(currentTime));
        ZLogger.v(LOG_TAG, "getToday(), " + date);
        return date;
    }

}
