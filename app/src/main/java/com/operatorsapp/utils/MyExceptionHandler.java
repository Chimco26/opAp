package com.operatorsapp.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.operatorsapp.activities.MainActivity;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.ActionBarAndEventsFragment;
import com.operatorsapp.managers.PersistenceManager;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String LOG_TAG = MyExceptionHandler.class.getSimpleName();
    private final Activity activity;

    public MyExceptionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.d(LOG_TAG, "MyExceptionHandler " + e.toString());
        Intent intent = new Intent(OperatorApplication.getAppContext(), MainActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(OperatorApplication.getAppContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) OperatorApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        activity.finish();
        System.exit(2);
    }
}
