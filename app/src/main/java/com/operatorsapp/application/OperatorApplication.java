package com.operatorsapp.application;

import android.app.Application;
import android.content.Context;

import com.operatorsapp.R;
import com.operatorsapp.common.Params;
import com.operatorsapp.managers.CriticalMachinesManager;
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
        CriticalMachinesManager.initInstance();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidSans.ttf").setFontAttrId(R.attr.fontPath).build());

        if (Params.IS_IN_DEVELOP_MODE) {
            ZLogger.DEBUG = true;
        }
    }

    public static Context getAppContext() {
        return msApplicationContext;
    }

}
