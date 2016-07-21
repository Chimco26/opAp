package com.operatorsapp.managers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operators.logincore.interfaces.LoginPersistenceManagerInterface;
import com.operators.shiftlogcore.interfaces.ShiftLogPersistenceManagerInterface;
import com.operators.shiftloginfra.ShiftLog;
import com.operatorsapp.utils.SecurePreferences;
import com.zemingo.logrecorder.ZLogger;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PersistenceManager implements LoginPersistenceManagerInterface, ShiftLogPersistenceManagerInterface {

    private static final String LOG_TAG = PersistenceManager.class.getSimpleName();

    private static final String PREF_SITE_URL = "pref.PREF_SITE_URL";
    private static final String PREF_USERNAME = "pref.PREF_USERNAME";
    private static final String PREF_PASSWORD = "pref.PREF_PASSWORD";
    private static final String PREF_SESSION_ID = "pref.PREF_SESSION_ID";
    private static final String PREF_MACHINE_ID = "pref.PREF_MACHINE_ID";
    private static final String PREF_SELECTED_MACHINE = "pref.PREF_SELECTED_MACHINE";
    private static final String PREF_TOTAL_RETRIES = "pref.PREF_TOTAL_RETRIES";
    private static final String PREF_REQUEST_TIMEOUT = "pref.PREF_REQUEST_TIMEOUT";
    private static final String PREF_ARRAY_SHIFT_LOGS = "pref.PREF_ARRAY_SHIFT_LOGS";

    private static PersistenceManager msInstance;
    private Gson mGson;

    public static PersistenceManager initInstance(Context context) {
        if (msInstance == null) {
            msInstance = new PersistenceManager(context);
        }

        return msInstance;
    }

    public static PersistenceManager getInstance() {
        if (msInstance == null) {
            ZLogger.e(LOG_TAG, "getInstance(), fail, PersistenceManager is not init");
        }
        return msInstance;
    }

    private PersistenceManager(Context context) {
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

    public int getMachineId() {
        return SecurePreferences.getInstance().getInt(PREF_MACHINE_ID);
    }

    public void setMachineId(int machineId) {
        SecurePreferences.getInstance().setInt(PREF_MACHINE_ID, machineId);
    }

    public boolean isSelectedMachine() {
        return SecurePreferences.getInstance().getBoolean(PREF_SELECTED_MACHINE, false);
    }

    public void setSelectedMachine(boolean selected) {
        SecurePreferences.getInstance().setBoolean(PREF_SELECTED_MACHINE, selected);
    }

    @Override
    public int getTotalRetries() {
        return SecurePreferences.getInstance().getInt(PREF_TOTAL_RETRIES);
    }

    @Override
    public void setTotalRetries(int totalRetries) {
        SecurePreferences.getInstance().setInt(PREF_TOTAL_RETRIES, totalRetries);
    }

    @Override
    public int getRequestTimeout() {
        return SecurePreferences.getInstance().getInt(PREF_REQUEST_TIMEOUT);
    }

    @Override
    public void setRequestTimeOut(int requestTimeOut) {
        SecurePreferences.getInstance().setInt(PREF_REQUEST_TIMEOUT, requestTimeOut);
    }

    @Override
    public void saveShiftLogs(ArrayList<ShiftLog> shiftLogs) {
        SecurePreferences.getInstance().setString(PREF_ARRAY_SHIFT_LOGS, mGson.toJson(shiftLogs));
    }

    @Override
    public ArrayList<ShiftLog> getShiftLogs() {
        String shiftLogsJsonString = SecurePreferences.getInstance().getString(PREF_ARRAY_SHIFT_LOGS);
        Type listType = new TypeToken<ArrayList<ShiftLog>>() {
        }.getType();

        return mGson.fromJson(shiftLogsJsonString, listType);
    }

}
