package com.operatorsapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ParseException;
import android.util.Log;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static final String SQL_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String SQL_NO_T_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SIMPLE_FORMAT_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static final int ONE_MINUTE_IN_SECONDS = 60;
    private static final int ONE_HOUR_IN_SECONDS = ONE_MINUTE_IN_SECONDS * 60;
    private static final int ONE_DAY_IN_SECONDS = ONE_HOUR_IN_SECONDS * 24;
    private static final String LOG_TAG = TimeUtils.class.getSimpleName();

    @SuppressWarnings("unused")
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
                return 99 + "+ clearPollingRequest";
            } else {
                return day + " clearPollingRequest";

            }
        }

    }

    public static String updateDateForRtl(Property property, SimpleDateFormat actualFormat, SimpleDateFormat newFormat) {

        if (property.getKey().contains("Time") && property.getValue() != null && property.getValue().length() > 0) {

            try {
                Date date = actualFormat.parse(property.getValue());
                return newFormat.format(date);

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        return property.getValue();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
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
            return /*days + " " +*/ context.getResources().getQuantityString(R.plurals.days, (int) days, (int) days) + " " + String.format(Locale.getDefault(), "%02d:%02d", hours, minutes) + " " + context.getResources().getQuantityString(R.plurals.hours, (int) hours);
        } else {
            String h;
            if (hours == 0) {

                if (PersistenceManager.getInstance().getCurrentLang().equals("en")) {
                    h = "Hour";
                } else {
                    // Ohad change 6/6/17
                    h = context.getResources().getQuantityString(R.plurals.hours,1);
                    //h = "שעה";
                }
            } else {
                h = context.getResources().getQuantityString(R.plurals.hours, (int) hours);
            }
            return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes) + " " + h;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTimeFromString(String time) {
        if (time != null && !time.equals("")) {
            String out = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
            try {
                Date date = dateFormat.parse(time);
                out = dateFormat2.format(date);
            } catch (ParseException ignored) {
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            return out;
        } else {
            return "--";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateFromString(String time) {
        if (time != null && !time.equals("")) {
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
        } else {
            return "--";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateForJob(String time) {
        if (time != null && !time.equals("")) {
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
        } else {
            return "--";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getStringNoTFormatForNotification(String time) {

        if (time != null && time != "") {
            SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_FORMAT_FORMAT);
            SimpleDateFormat dateFormatSql = new SimpleDateFormat(SQL_NO_T_FORMAT);
            SimpleDateFormat dateFormatSqlT = new SimpleDateFormat(SQL_T_FORMAT);
            Date date;

            try {
                date = dateFormat.parse(time);
                return time;
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            try {
                date = dateFormatSql.parse(time);
                return dateFormat.format(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            try {
                date = dateFormatSqlT.parse(time);
                return dateFormat.format(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }


//
//
//
//        if (time.contains("T")) {
//            dateFormat = new SimpleDateFormat(SQL_T_FORMAT);
//            try {
//                Date date = dateFormat.parse(time);
//                dateFormat = new SimpleDateFormat(SQL_NO_T_FORMAT);
//
//                return dateFormat.format(date);
//            } catch (java.text.ParseException e) {
//
//            }
//
//        }
        return "";


    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDateForNotification(String time) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_FORMAT_FORMAT);
        SimpleDateFormat dateFormatSql = new SimpleDateFormat(SQL_NO_T_FORMAT);
        SimpleDateFormat dateFormatSqlT = new SimpleDateFormat(SQL_T_FORMAT);

        try {
            return dateFormat.parse(time);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        try {
            return dateFormatSql.parse(time);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        try {
            return dateFormatSqlT.parse(time);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return null;

//        SimpleDateFormat dateFormat = new SimpleDateFormat(SQL_NO_T_FORMAT);
//        try {
//            Date date = dateFormat.parse(time);
//            return date;
//        } catch (java.text.ParseException e) {
//
//            dateFormat = new SimpleDateFormat(SQL_T_FORMAT);
//            try {
//                Date date2 = dateFormat.parse(time);
//                return date2;
//            } catch (java.text.ParseException e1) {
//
//            }
//
//        }
//        return null;

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date date = dateFormat.parse(time);
//            return date;
//        } catch (java.text.ParseException e) {
//
//            dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//            try {
//                Date date2 = dateFormat.parse(time);
//                return date2;
//            } catch (java.text.ParseException e1) {
//
//            }
//
//        }
//        return null;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateForChart(String time) {
        if (time != null && !time.equals("")) {
            String out = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
            try {
                Date date = dateFormat.parse(time);
                out = dateFormat2.format(date);
            } catch (ParseException ignored) {
            } catch (java.text.ParseException e) {
                if(e.getMessage()!=null)

                Log.e(LOG_TAG,e.getMessage());
            }

            return out;
        } else {
            return "--";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static long getLongFromDateString(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long timeInMilliseconds = 0;
        try {
            Date mDate = sdf.parse(date);
            timeInMilliseconds = mDate.getTime();
        } catch (java.text.ParseException e) {
            if(e.getMessage()!=null)

            Log.e(LOG_TAG,e.getMessage());
        }
        return timeInMilliseconds;
    }
}
