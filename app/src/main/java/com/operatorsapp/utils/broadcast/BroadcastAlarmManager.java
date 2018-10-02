package com.operatorsapp.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.oppapplog.OppAppLogger;
import com.operatorsapp.utils.LogCacheCleaner;
import com.zemingo.logrecorder.LogRecorder;

public class BroadcastAlarmManager extends BroadcastReceiver {

    private static final String TAG = BroadcastAlarmManager.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d(TAG, "BroadcastReceiver, in onReceive:");

        new LogCacheCleaner(context).removeExistingFiles();

        OppAppLogger.initInstance(context);

    }
}
