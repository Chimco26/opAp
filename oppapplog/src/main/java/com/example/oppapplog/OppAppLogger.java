package com.example.oppapplog;


import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.util.Log;

import com.zemingo.logrecorder.LogRecorder;
import com.zemingo.logrecorder.ZLogger;

public class OppAppLogger {

    private static final String PREF_STORAGE_PERMISSION_GRANTED = "PREF_STORAGE_PERMISSION_GRANTED";
    private static final String TAG = OppAppLogger.class.getSimpleName();

    private static boolean mGranted = false;
    private static OppAppLogger instance;

    private OppAppLogger(Context context) {

        try {
            LogRecorder.initInstance(context);

            mGranted = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE).getBoolean(PREF_STORAGE_PERMISSION_GRANTED, false);

            if (BuildConfig.DEBUG) {
                ZLogger.DEBUG = true;
            }
        }catch (ReceiverCallNotAllowedException e){

        }
    }

    private OppAppLogger() {

        mGranted = false;
    }

    public static void setStorageGranted(Context context, boolean granted) {
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE).edit().putBoolean(PREF_STORAGE_PERMISSION_GRANTED, granted).apply();
    }


    public static void initInstance(Context context) {

        if (context != null) {
            instance = new OppAppLogger(context);
        }
    }

    public static OppAppLogger getInstance(Context context) {

        if (instance == null && context != null) {
            instance = new OppAppLogger(context);
        }
        return instance;
    }

    public static OppAppLogger getInstance() {

        if (instance == null) {

            Log.e(TAG, "getInstance: you must init the OppAppLogger with context");

            instance = new OppAppLogger();

        }
        return instance;
    }

    public void v(String tag, String msg) {
        if (mGranted) {
            ZLogger.v(tag, msg);
        }

    }

    public void d(String tag, String msg) {
        if (mGranted) {
            ZLogger.d(tag, msg);
        }

    }

    public void i(String tag, String msg) {
        if (mGranted) {
            ZLogger.i(tag, msg);
        }

    }

    public void w(String tag, String msg) {
        if (mGranted) {
            ZLogger.w(tag, msg);
        }

    }

    public void w(String tag, Exception e) {
        if (mGranted) {
            ZLogger.w(tag, e);
        }

    }

    public void w(String tag, String msg, Exception e) {
        if (mGranted) {
            ZLogger.w(tag, msg, e);
        }

    }

    public void e(String tag, String msg) {
        if (mGranted) {
            ZLogger.e(tag, msg);
        }
    }

    public void e(String tag, String msg, Exception e) {
        if (mGranted) {
            ZLogger.e(tag, msg, e);
        }
    }

}
