package com.operatorsapp.server.pulling.timecounter;


import android.os.CountDownTimer;

import com.example.oppapplog.OppAppLogger;
import com.operatorsapp.server.pulling.interfaces.OnTimeToEndChangedListener;


public class TimeToEndCounter
{
    private static final String LOG_TAG = TimeToEndCounter.class.getSimpleName();
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private static final String ZERO_TIME = "00:00:00";
    public static final int FINISHED = 0;
    private OnTimeToEndChangedListener mOnTimeToEndChangedListener;
    private CountDownTimer mCountDownTimer;

    public TimeToEndCounter(OnTimeToEndChangedListener onTimeToEndChangedListener) {
        mOnTimeToEndChangedListener = onTimeToEndChangedListener;
    }

    public void calculateTimeToEnd(final int timeInMilliseconds) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mCountDownTimer = new CountDownTimer(timeInMilliseconds, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                mOnTimeToEndChangedListener.onTimeToEndChanged(millisUntilFinished);
            }

            public void onFinish() {
                mOnTimeToEndChangedListener.onTimeToEndChanged(FINISHED);
                OppAppLogger.i(LOG_TAG, "onFinish()");
            }

        }.start();
    }

    public void calculateShiftToEnd(final long timeInMilliseconds) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mCountDownTimer = new CountDownTimer(timeInMilliseconds, COUNT_DOWN_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mOnTimeToEndChangedListener.onTimeToEndChanged(FINISHED);
                OppAppLogger.i(LOG_TAG, "onFinish()");
            }

        }.start();
    }

    public void stopTimer() {
        mCountDownTimer.cancel();
    }
}
