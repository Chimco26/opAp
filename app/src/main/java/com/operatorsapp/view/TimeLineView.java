package com.operatorsapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.operatorsapp.R;
import com.operatorsapp.fragments.ActionBarAndEventsFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class TimeLineView extends View {

    private static final String SQL_NO_T_FORMAT = "dd/MM/yyyy HH:mm:ss";


    private ArrayList<String> mTimes;
    private Paint mPaint;
    private int textPosition = 25;
    private int mStartTimeSpace = 0;
    private int mEndTimeSpace = 0;


    public TimeLineView(Context context) {
        super(context);
        init();
    }


    public TimeLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(10);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textPosition = 25;

        if (mTimes != null)

            for (int i = 0; i < mTimes.size(); i++) {

                canvas.drawText(mTimes.get(i), 5, textPosition, mPaint);
//                canvas.drawLine(mStartLinePosition, textPosition - 10, mEndLinePosition, mTextPositionY - 10, mLinePaint);
                if (i == 0)
                    textPosition += mStartTimeSpace * ActionBarAndEventsFragment.PIXEL_FOR_MINUTE;
                else if (i == mTimes.size() - 2)
                    textPosition += mEndTimeSpace;// * ActionBarAndEventsFragment.PIXEL_FOR_MINUTE;
//                    textPosition += ActionBarAndEventsFragment.PIXEL_FOR_MINUTE;
                else
                    textPosition += 30 * ActionBarAndEventsFragment.PIXEL_FOR_MINUTE;

//                canvas.drawText(mTimes.get(i), 50, textPosition, mPaint);
//
//                textPosition += 30 * ActionBarAndEventsFragment.PIXEL_FOR_MINUTE;


            }


    }


    public void addTimesToList(String startTime, String endTime) {

//        Long eventStartMilli = convertDateToMillisecond(startTime);
//        Long eventEndMilli = convertDateToMillisecond(endTime);


        mTimes = new ArrayList<>();

        int startHour = convertStringDateToHour(startTime);
        int startMinute = convertStringDateToMinute(startTime);
        int endHour = convertStringDateToHour(endTime);
        int endMinute = convertStringDateToMinute(endTime);

        for (int i = 0; i < 25; i++) {

            if (startHour > 23) {
                startHour = startHour - 24;
            }

            switch (i) {
                case 0:
                    if (startMinute >= 30) {
                        mTimes.add(hourToTime(startHour, startMinute));
//                        mTimes.add(startHour + ":" + startMinute);
                        mStartTimeSpace = 60 - startMinute;

                    } else if (startMinute >= 0) {
                        mTimes.add(hourToTime(startHour, startMinute));
//                        mTimes.add(startHour + ":" + startMinute);
                        mStartTimeSpace = 30 - startMinute;
                        mTimes.add(startHour + ":30");
                    }
                    break;

                case 24:
                    if (endMinute >= 30) {
                        mTimes.add(endHour + ":00");
                        mTimes.add(hourToTime(endHour, endMinute));
//                        mTimes.add(endHour + ":" + endMinute);
                        mEndTimeSpace = endMinute * ActionBarAndEventsFragment.PIXEL_FOR_MINUTE;

                    } else if (endMinute >= 0) {
                        mTimes.add(hourToTime(endHour, endMinute));
//                        mTimes.add(endHour + ":" + endMinute);
                        mEndTimeSpace = (30 + endMinute) * ActionBarAndEventsFragment.PIXEL_FOR_MINUTE - (int) mPaint.getTextSize() - 10;
                    }
                    break;
                default:
                    mTimes.add(startHour + ":00");
                    mTimes.add(startHour + ":30");
                    break;
            }

            startHour++;

        }


    }


    private String hourToTime(int hour, int minute) {
        if (minute < 10)
            return String.valueOf(hour) + ":0" + String.valueOf(minute);
        else
            return String.valueOf(hour) + ":" + String.valueOf(minute);
    }



    public static int convertStringDateToHour(String startTime) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(SQL_NO_T_FORMAT);
        try {
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            Date date = format.parse(startTime);
            calendar.setTime(date);   // assigns calendar to given date
            return calendar.get(Calendar.HOUR_OF_DAY);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static int convertStringDateToMinute(String startTime) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(SQL_NO_T_FORMAT);
        try {
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            Date date = format.parse(startTime);
            calendar.setTime(date);   // assigns calendar to given date
            return calendar.get(Calendar.MINUTE);

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
