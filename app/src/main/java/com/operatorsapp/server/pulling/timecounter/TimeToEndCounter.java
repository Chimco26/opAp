package com.operatorsapp.server.pulling.timecounter;


import android.os.CountDownTimer;

import com.example.oppapplog.OppAppLogger;
import com.operatorsapp.server.pulling.interfaces.OnTimeToEndChangedListener;

import java.lang.ref.WeakReference;


public class TimeToEndCounter
{
    private static final String LOG_TAG = TimeToEndCounter.class.getSimpleName();
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private static final String ZERO_TIME = "00:00:00";
    public static final int FINISHED = 0;
    private CountDownTimer mCountDownTimer;

    public void calculateTimeToEnd(final int timeInMilliseconds, final OnTimeToEndChangedListener listener) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mCountDownTimer = new CountDownTimer(timeInMilliseconds, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                if (listener != null) {
                    listener.onTimeToEndChanged(millisUntilFinished);
                }
            }

            public void onFinish() {
                if (listener != null) {
                    listener.onTimeToEndChanged(FINISHED);
                }
                OppAppLogger.i(LOG_TAG, "onFinish()");
            }

        }.start();
    }

    public void calculateShiftToEnd(final long timeInMilliseconds, final OnTimeToEndChangedListener listener) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }


        mCountDownTimer = new CountDownTimer(timeInMilliseconds, COUNT_DOWN_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.onTimeToEndChanged(FINISHED);
                }
                OppAppLogger.i(LOG_TAG, "onFinish()");
            }

        }.start();
    }

    public void stopTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }
}
