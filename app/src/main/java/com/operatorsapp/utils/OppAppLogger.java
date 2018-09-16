package com.operatorsapp.utils;

import com.operatorsapp.managers.PersistenceManager;
import com.zemingo.logrecorder.ZLogger;

public class OppAppLogger {

    private static boolean mGranted = false;
    private static OppAppLogger instance;

    private OppAppLogger() {

        mGranted = PersistenceManager.getInstance().isStorageGranted();

    }

    public static OppAppLogger getInstance(){

        if (instance == null){
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
