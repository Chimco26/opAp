package com.operatorsapp.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.operatorsapp.activities.MainActivity;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.ActionBarAndEventsFragment;
import com.operatorsapp.managers.PersistenceManager;

import java.lang.ref.WeakReference;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String LOG_TAG = MyExceptionHandler.class.getSimpleName();
    private final WeakReference<Activity> activity;

    public MyExceptionHandler(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        Log.d(LOG_TAG, "MyExceptionHandler- " + t.getStackTrace()[0].getClassName() + ": " + t.getStackTrace()[0].getLineNumber());

        FirebaseCrashlytics.getInstance().recordException(e);
        Intent intent = new Intent(OperatorApplication.getAppContext(), MainActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(OperatorApplication.getAppContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) OperatorApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        if (activity.get() != null && !activity.get().isFinishing() && !activity.get().isDestroyed()) {
            activity.get().finish();
        }
        System.exit(2);
    }
}
