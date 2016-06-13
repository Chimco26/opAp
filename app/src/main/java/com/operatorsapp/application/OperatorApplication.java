package com.operatorsapp.application;

import android.app.Application;
import android.content.Context;


public class OperatorApplication extends Application
{

    private static Context msApplicationContext;

    @Override
    public void onCreate()
    {
        super.onCreate();

        msApplicationContext = getApplicationContext();
    }

    public static Context getAppContext()
    {
        return msApplicationContext;
    }

}
