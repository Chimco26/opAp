package com.operatorsapp.managers;

import android.content.Context;

import com.operators.machinestatusinfra.MachineStatusPersistenceManagerInterface;
import com.operatorsapp.utils.SecurePreferences;
import com.zemingo.logrecorder.ZLogger;

public class MachineStatusPersistenceManager implements MachineStatusPersistenceManagerInterface {
    private static final String LOG_TAG = MachineStatusPersistenceManager.class.getSimpleName();

    private static final String PREF_SITE_URL = "pref.PREF_SITE_URL";
    private static final String PREF_USERNAME = "pref.PREF_USERNAME";
    private static final String PREF_PASSWORD = "pref.PREF_PASSWORD";
    private static final String PREF_SESSION_ID = "pref.PREF_SESSION_ID";
    private static final String PREF_MACHINE_ID = "pref.PREF_MACHINE_ID";
    private static final String PREF_SELECTED_MACHINE = "pref.PREF_SELECTED_MACHINE";
    private static final String PREF_TOTAL_RETRIES = "pref.PREF_TOTAL_RETRIES";
    private static final String PREF_REQUEST_TIMEOUT = "pref.PREF_REQUEST_TIMEOUT";

    private static MachineStatusPersistenceManager msInstance;

    public static MachineStatusPersistenceManager initInstance(Context context)
    {
        if (msInstance == null)
        {
            msInstance = new MachineStatusPersistenceManager(context);
        }
        return msInstance;
    }

    public static MachineStatusPersistenceManager getInstance()
    {
        if (msInstance == null)
        {
            ZLogger.e(LOG_TAG, "getInstance(), fail, PersistenceManager is not init");
        }
        return msInstance;
    }

    private MachineStatusPersistenceManager(Context context)
    {
        SecurePreferences.initInstance(context);
    }

    @Override
    public String getSiteUrl()
    {
        return SecurePreferences.getInstance().getString(PREF_SITE_URL);
    }

    @Override
    public void setSiteUrl(String siteUrl)
    {
        SecurePreferences.getInstance().setString(PREF_SITE_URL, siteUrl);
    }

    @Override
    public String getSessionId()
    {
        return SecurePreferences.getInstance().getString(PREF_SESSION_ID);
    }

    @Override
    public void setSessionId(String sessionId)
    {
        SecurePreferences.getInstance().setString(PREF_SESSION_ID, sessionId);
    }


    @Override
    public int getMachineId()
    {
        return SecurePreferences.getInstance().getInt(PREF_MACHINE_ID);
    }

    @Override
    public void setMachineId(int machineId)
    {
        SecurePreferences.getInstance().setInt(PREF_MACHINE_ID, machineId);
    }

    public boolean isSelectedMachine()
    {
        return SecurePreferences.getInstance().getBoolean(PREF_SELECTED_MACHINE, false);
    }

    public void setSelectedMachine(boolean selected)
    {
        SecurePreferences.getInstance().setBoolean(PREF_SELECTED_MACHINE, selected);
    }

    @Override
    public int getTotalRetries()
    {
        return SecurePreferences.getInstance().getInt(PREF_TOTAL_RETRIES);
    }

    @Override
    public void setTotalRetries(int totalRetries)
    {
        SecurePreferences.getInstance().setInt(PREF_TOTAL_RETRIES, totalRetries);
    }

    @Override
    public int getRequestTimeout()
    {
        return SecurePreferences.getInstance().getInt(PREF_REQUEST_TIMEOUT);
    }

    @Override
    public void setRequestTimeOut(int requestTimeOut)
    {
        SecurePreferences.getInstance().setInt(PREF_REQUEST_TIMEOUT, requestTimeOut);
    }
}
