package com.operatorsapp.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.operators.getmachinesnetworkbridge.GetMachinesNetworkBridge;
import com.operators.logincore.ErrorObject;
import com.operators.logincore.LoginCore;
import com.operators.logincore.LoginUICallback;
import com.operators.loginnetworkbridge.LoginNetworkBridge;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.managers.LoginPersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.zemingo.logrecorder.ZLogger;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class OperatorApplication extends Application {
    private static final String LOG_TAG = OperatorApplication.class.getSimpleName();
    private static Context msApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        msApplicationContext = getApplicationContext();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidSans.ttf").setFontAttrId(R.attr.fontPath).build());

        LoginPersistenceManager.initInstance(msApplicationContext);
        NetworkManager.initInstance();

        GetMachinesNetworkBridge getMachinesNetworkBridge = new GetMachinesNetworkBridge();
        getMachinesNetworkBridge.inject(NetworkManager.getInstance());

        LoginNetworkBridge loginNetworkBridge = new LoginNetworkBridge();
        loginNetworkBridge.inject(NetworkManager.getInstance());

        LoginCore.getInstance().inject(LoginPersistenceManager.getInstance(), loginNetworkBridge, getMachinesNetworkBridge);


        if (BuildConfig.DEBUG) {
            ZLogger.DEBUG = true;
        }
    }

    public static Context getAppContext() {
        return msApplicationContext;
    }

    public static void silentLogin(final LoginUICallback loginUICallback) {
        if (isAllDataAreValid()) {
            LoginCore.getInstance().login(LoginPersistenceManager.getInstance().getSiteUrl(), LoginPersistenceManager.getInstance().getUserName(), LoginPersistenceManager.getInstance().getPassword(), loginUICallback);
        } else {
            Log.d(LOG_TAG, "silentLogin, Data to login is null");
            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Credentials_mismatch, "Data to login is null");
            loginUICallback.onLoginFailed(errorObject);
        }
    }

    private static boolean isAllDataAreValid() {
        String siteUrl = LoginPersistenceManager.getInstance().getSiteUrl();
        String userName = LoginPersistenceManager.getInstance().getUserName();
        String password = LoginPersistenceManager.getInstance().getPassword();
        return siteUrl != null && userName != null && password != null;
    }

}
