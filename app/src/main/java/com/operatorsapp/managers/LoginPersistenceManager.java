package com.operatorsapp.managers;

import android.content.Context;

import com.operators.logincore.interfaces.PersistenceManagerInterface;
import com.operatorsapp.utils.SecurePreferences;
import com.zemingo.logrecorder.ZLogger;

public class LoginPersistenceManager implements PersistenceManagerInterface {

    private static final String LOG_TAG = LoginPersistenceManager.class.getSimpleName();

    private static final String PREF_SITE_URL = "pref.PREF_SITE_URL";
    private static final String PREF_USERNAME = "pref.PREF_USERNAME";
    private static final String PREF_PASSWORD = "pref.PREF_PASSWORD";
    private static final String PREF_SESSION_ID = "pref.PREF_SESSION_ID";
    private static final String PREF_MACHINE_ID = "pref.PREF_MACHINE_ID";

    private static LoginPersistenceManager msInstance;

    public static LoginPersistenceManager initInstance(Context context) {
        if (msInstance == null) {
            msInstance = new LoginPersistenceManager(context);
        }

        return msInstance;
    }

    public static LoginPersistenceManager getInstance() {
        if (msInstance == null) {
            ZLogger.e(LOG_TAG, "getInstance(), fail, LoginPersistenceManager is not init");
        }
        return msInstance;
    }

    private LoginPersistenceManager(Context context) {
        SecurePreferences.initInstance(context);
    }

    @Override
    public String getSiteUrl() {
        return SecurePreferences.getInstance().getString(PREF_SITE_URL);
    }

    @Override
    public void setSiteUrl(String siteUrl) {
        SecurePreferences.getInstance().setString(PREF_SITE_URL, siteUrl);
    }

    @Override
    public String getUserName() {
        return SecurePreferences.getInstance().getString(PREF_USERNAME);
    }

    @Override
    public void setUsername(String username) {
        SecurePreferences.getInstance().setString(PREF_USERNAME, username);
    }

    @Override
    public String getPassword() {
        return SecurePreferences.getInstance().getString(PREF_PASSWORD);
    }

    @Override
    public void setPassword(String password) {
        SecurePreferences.getInstance().setString(PREF_PASSWORD, password);
    }

    @Override
    public String getSessionId() {
        return SecurePreferences.getInstance().getString(PREF_SESSION_ID);
    }

    @Override
    public void setSessionId(String sessionId) {
        SecurePreferences.getInstance().setString(PREF_SESSION_ID, sessionId);
    }

    public int getMachineId() {
        return SecurePreferences.getInstance().getInt(PREF_MACHINE_ID);
    }

    public void setMachineId(int machineId) {
        SecurePreferences.getInstance().setInt(PREF_MACHINE_ID, machineId);
    }

}
