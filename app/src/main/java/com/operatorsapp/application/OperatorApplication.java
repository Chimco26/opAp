package com.operatorsapp.application;

import android.app.Application;
import android.content.Context;

import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.zemingo.logrecorder.ZLogger;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class OperatorApplication extends Application {

    private static Context msApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        msApplicationContext = getApplicationContext();
        PersistenceManager.initInstance(msApplicationContext);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidSans.ttf").setFontAttrId(R.attr.fontPath).build());

        if (BuildConfig.DEBUG) {
            ZLogger.DEBUG = true;
        }
    }

    public static Context getAppContext() {
        return msApplicationContext;
    }

}
