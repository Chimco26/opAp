package com.operators.machinestatuscore.timecounter;


import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import com.operators.machinestatuscore.interfaces.OnTimeToEndChangedListener;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeToEndCounter {
    private static final String LOG_TAG = TimeToEndCounter.class.getSimpleName();
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private static final String ZERO_TIME = "00:00:00";
    private OnTimeToEndChangedListener mOnTimeToEndChangedListener;
    private CountDownTimer mCountDownTimer;

    public TimeToEndCounter(OnTimeToEndChangedListener onTimeToEndChangedListener) {
        mOnTimeToEndChangedListener = onTimeToEndChangedListener;
    }

    public void calculateTimeToEnd(final int timeInMilliseconds,final Context context) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mCountDownTimer = new CountDownTimer(timeInMilliseconds, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {

                Locale locale = context.getResources().getConfiguration().locale;

                String countDownTimer = String.format(locale, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                mOnTimeToEndChangedListener.onTimeToEndChanged(countDownTimer);
            }

            public void onFinish() {
                mOnTimeToEndChangedListener.onTimeToEndChanged(ZERO_TIME);
                Log.i(LOG_TAG, "onFinish()");
            }

        }.start();
    }

    public void stopTimer() {
        mCountDownTimer.cancel();
    }
}
