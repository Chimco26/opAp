package com.operatorsapp.managers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operators.getmachinesnetworkbridge.server.responses.Machine;
import com.operators.logincore.PersistenceManagerInterface;
import com.operatorsapp.utils.SecurePreferences;
import com.zemingo.logrecorder.ZLogger;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoginPersistenceManager implements PersistenceManagerInterface {

    private static final String LOG_TAG = LoginPersistenceManager.class.getSimpleName();

    private static final String PREF_SITE_URL = "pref.PREF_SITE_URL";
    private static final String PREF_USERNAME = "pref.PREF_USERNAME";
    private static final String PREF_PASSWORD = "pref.PREF_PASSWORD";
    private static final String PREF_SESSION_ID = "pref.PREF_SESSION_ID";
    private static final String PREF_MACHINES = "pref.PREF_MACHINES";

    private static LoginPersistenceManager msInstance;
    private Gson mGson;

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
        mGson = new Gson();
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

    @Override
    public void saveMachines(ArrayList machines) {
        SecurePreferences.getInstance().setString(PREF_MACHINES, mGson.toJson(machines));
    }

    @Override
    public ArrayList<Machine> getMachines() {
        String machinesJsonString = SecurePreferences.getInstance().getString(PREF_MACHINES);
        Type listType = new TypeToken<ArrayList<Machine>>() {
        }.getType();

        return mGson.fromJson(machinesJsonString, listType);
    }

}
