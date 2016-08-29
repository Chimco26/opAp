package com.operatorsapp.application;

import android.app.Application;
import android.content.Context;

import com.operators.getmachinesnetworkbridge.GetMachinesNetworkBridge;
import com.operators.logincore.LoginCore;
import com.operators.loginnetworkbridge.LoginNetworkBridge;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.zemingo.logrecorder.ZLogger;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class OperatorApplication extends Application
{
    private static Context msApplicationContext;

    @Override
    public void onCreate()
    {
        super.onCreate();

        msApplicationContext = getApplicationContext();
//        LeakCanary.install(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidSans.ttf").setFontAttrId(R.attr.fontPath).build());

        PersistenceManager.initInstance(msApplicationContext);
        NetworkManager.initInstance();

        GetMachinesNetworkBridge getMachinesNetworkBridge = new GetMachinesNetworkBridge();
        getMachinesNetworkBridge.inject(NetworkManager.getInstance());

        LoginNetworkBridge loginNetworkBridge = new LoginNetworkBridge();
        loginNetworkBridge.inject(NetworkManager.getInstance());
        LoginCore.getInstance().inject(PersistenceManager.getInstance(), loginNetworkBridge, getMachinesNetworkBridge);

//        ShiftLogNetworkBridge shiftLogNetworkBridge = new ShiftLogNetworkBridge();
//        shiftLogNetworkBridge.inject(NetworkManager.getInstance());
//
//        ShiftLogCore.getInstance().inject(PersistenceManager.getInstance(), shiftLogNetworkBridge);



        if (BuildConfig.DEBUG)
        {
            ZLogger.DEBUG = true;
        }
    }

    public static Context getAppContext()
    {
        return msApplicationContext;
    }
}
