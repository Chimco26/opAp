package com.operatorsapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ParseException;
import android.util.Log;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static final String SQL_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String SQL_NO_T_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SIMPLE_FORMAT_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String SIMPLE_HMS_FORMAT = "HH:mm:ss";
    public static final String SIMPLE_HM_FORMAT = "HH:mm";
    public static final String COMMON_DATE_FORMAT = "HH:mm dd/MM/yyyy";
    public static final String COMMON_WITH_2_CHARS_YEAR = "dd/MM/yy HH:mm";
    public static final String ONLY_DATE_FORMAT = "dd/MM/yyyy";
    public static final int ONE_MINUTE_IN_SECONDS = 60;
    private static final int ONE_HOUR_IN_SECONDS = ONE_MINUTE_IN_SECONDS * 60;
    private static final int ONE_DAY_IN_SECONDS = ONE_HOUR_IN_SECONDS * 24;
    private static final int ONE_DAY_IN_MINUTS = ONE_DAY_IN_SECONDS / 60;
    private static final int ONE_HOUR_IN_MINUTS = ONE_HOUR_IN_SECONDS / 60;
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
//            throw new IllegalArgumentException("Duration must be greater than zero!");
            return " ";
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
                    h = context.getResources().getQuantityString(R.plurals.hours, 1);
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
                date = dateFormatSqlT.parse(time);
                return dateFormat.format(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
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
        }

        try {
            return dateFormatSql.parse(time);
        } catch (java.text.ParseException e) {
        }

        try {
            return dateFormatSqlT.parse(time);

        } catch (java.text.ParseException e) {
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
                if (e.getMessage() != null)

                    Log.e(LOG_TAG, e.getMessage());
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
            if (e.getMessage() != null)

                Log.e(LOG_TAG, e.getMessage());
        }
        return timeInMilliseconds;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNoTFromDateString(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        SimpleDateFormat sdf2 = new SimpleDateFormat(SQL_NO_T_FORMAT);
        String finalDate = "";
        try {
            Date mDate = sdf.parse(date);
            finalDate = sdf2.format(mDate);
        } catch (java.text.ParseException e) {
            if (e.getMessage() != null)

                Log.e(LOG_TAG, e.getMessage());
        }
        return finalDate;
    }

    public static String getTimeFromMinute(int minute) {

        String result = "";

        int days = minute / ONE_DAY_IN_MINUTS;

        if (days > 0) {
            result += days + "d" + " ";
            minute -= days * ONE_DAY_IN_MINUTS;
        }

        int hours = minute / ONE_HOUR_IN_MINUTS;

        if (hours > 0) {
            result += hours + "hr" + " ";
            minute -= hours * ONE_HOUR_IN_MINUTS;
        }

        if (minute > 0) {
            result += minute + "min";
        }

        return result;
    }

    public static Date getTodayMidnightDate() {
        // today
        Calendar date = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return date.getTime();
    }

    public static String getDateFromFormat(Date date, String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        String dateToString = simpleDateFormat.format(date);
        return dateToString;
    }

    public static long getMillisFromHMS(String mDuration) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_HMS_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-0:00"));
        long millis = 0;
        try {
            Date date = simpleDateFormat.parse(mDuration);
            millis = date.getTime();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }

    @SuppressLint("DefaultLocale")
    public static String getHMSFromMillis(double millis) {
        long ms = (long) millis;
        String output = "";

        if (millis >= 1000) {
            output = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(ms),
                    TimeUnit.MILLISECONDS.toMinutes(ms) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(ms) % TimeUnit.MINUTES.toSeconds(1));

        }

        return output;
    }

    @SuppressLint("DefaultLocale")
    public static String getHMFromMillis(double millis) {
        long ms = (long) millis;
        String output = "";

        if (millis >= 1000) {
            output = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(ms),
                    TimeUnit.MILLISECONDS.toMinutes(ms) % TimeUnit.HOURS.toMinutes(1));

        }

        return output;
    }

    public static String getDecimalHourFromMillis(double millis, String hr, String min) {
        String output = "";

        if (millis >= ONE_HOUR_IN_SECONDS * 1000){
            DecimalFormat df = new DecimalFormat("#.##");
            int hours = (int) (millis / (ONE_HOUR_IN_SECONDS * 1000f));
            output = df.format(hours + ((millis / (ONE_HOUR_IN_SECONDS * 1000f) - hours) * 60 /100)) + hr;
        }else {
            output = (int)(millis / (ONE_MINUTE_IN_SECONDS * 1000f)) + min;
        }

        return output;
    }

    public static Long convertDateToMillisecond(String dateToConvert, String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if (dateToConvert != null && dateToConvert.length() > 0) {
            try {
                Date date = simpleDateFormat.parse(dateToConvert);
                return date.getTime();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }

    public static Long convertDateToMillisecond(String dateToConvert) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(SIMPLE_FORMAT_FORMAT);
        try {
            Date date = format.parse(dateToConvert);
            return castToMin(date.getTime());
        } catch (java.text.ParseException e) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatT = new SimpleDateFormat(SQL_T_FORMAT);
            try {
                Date date = formatT.parse(dateToConvert);
                return castToMin(date.getTime());
            } catch (java.text.ParseException e1) {
                e1.printStackTrace();
            }
        }
        return 0L;
    }

    private static Long castToMin(long time) {
        return (time / 60000) * 60000;
    }


    public static String convertMillisecondDateTo(Long millis) {

        Date date = new Date(millis);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(COMMON_WITH_2_CHARS_YEAR);


        return format.format(date);


    }
}
