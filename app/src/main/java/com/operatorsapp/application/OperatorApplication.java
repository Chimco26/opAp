package com.operatorsapp.application;

import android.app.Application;
import android.content.Context;

import com.operators.logincore.LoginCore;
import com.operators.networkbridge.LoginNetworkBridge;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.managers.LoginPersistenceManager;
import com.operatorsapp.server.LoginNetworkManager;
import com.zemingo.logrecorder.ZLogger;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class OperatorApplication extends Application {

    private static Context msApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        msApplicationContext = getApplicationContext();
        LoginPersistenceManager.initInstance(msApplicationContext);
        LoginNetworkManager.initInstance();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidSans.ttf").setFontAttrId(R.attr.fontPath).build());

        LoginNetworkBridge loginNetworkBridge = new LoginNetworkBridge();
        loginNetworkBridge.inject(LoginNetworkManager.getInstance());
        LoginCore.getInstance().inject(LoginPersistenceManager.getInstance(),  loginNetworkBridge);

        if (BuildConfig.DEBUG) {
            ZLogger.DEBUG = true;
        }
    }

    public static Context getAppContext() {
        return msApplicationContext;
    }

}
