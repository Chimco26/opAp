package com.operatorsapp.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.operatorsapp.utils.LogCacheCleaner;

public class BroadcastAlarmManager extends BroadcastReceiver {

    private static final String TAG = BroadcastAlarmManager.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d(TAG, "BroadcastReceiver, in onReceive:");

        new LogCacheCleaner(context).removeExistingFiles();

//        OppAppLogger.initInstance(context); can't start service from broadcast receiver

    }
}
