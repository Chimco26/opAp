package com.operatorsapp.utils;

public class TimeUtils
{
    public static final int ONE_MINUTE_IN_SECONDS = 60;
    public static final int ONE_HOUR_IN_SECONDS = ONE_MINUTE_IN_SECONDS * 60;
    public static final int ONE_DAY_IN_SECONDS = ONE_HOUR_IN_SECONDS * 24;

    public static String getStoppedTimeToDisplay(long timeInSeconds)
    {

        if (timeInSeconds < ONE_MINUTE_IN_SECONDS)
        {
            return 1 + " m";
        }
        else if (timeInSeconds < ONE_HOUR_IN_SECONDS)
        {
            return (timeInSeconds / ONE_MINUTE_IN_SECONDS) + " m";
        }
        else if (timeInSeconds < ONE_DAY_IN_SECONDS)
        {
            int minutes = (int) ((timeInSeconds - ((timeInSeconds / ONE_HOUR_IN_SECONDS) * ONE_HOUR_IN_SECONDS)) / ONE_MINUTE_IN_SECONDS);
            return timeInSeconds / ONE_HOUR_IN_SECONDS + ":" + minutes + " h";
        }
        else
        {
            int day = (int) (timeInSeconds / ONE_DAY_IN_SECONDS);
            if (day >= 99)
            {
                return 99 + "+ d";
            }
            else
            {
                return day + " d";

            }
        }

    }
}
