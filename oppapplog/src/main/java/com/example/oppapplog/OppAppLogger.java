package com.example.oppapplog;


import android.util.Log;

public class OppAppLogger {

    private static final String TAG = OppAppLogger.class.getSimpleName();

    public static void v(String tag, String msg) {

        Log.v(tag, msg);

    }

    public static void d(String tag, String msg) {

        Log.d(tag, msg);

    }

    public static void i(String tag, String msg) {

        Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {

        Log.w(tag, msg);

    }

    public static void w(String tag, Exception e) {

        Log.w(tag, e);

    }

    public static void w(String tag, String msg, Exception e) {

        Log.w(tag, msg, e);


    }

    public static void e(String tag, String msg) {

        Log.e(tag, msg);

    }

    public static void e(String tag, String msg, Exception e) {

        Log.e(tag, msg, e);
        
    }

}
