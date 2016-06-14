package com.operatorsapp.managers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operatorsapp.models.Site;
import com.operatorsapp.utils.SecurePreferences;
import com.zemingo.logrecorder.ZLogger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class PersistenceManager {
    private static final String LOG_TAG = PersistenceManager.class.getSimpleName();

    private static final String PREF_SITES = "pref.PREF_SITES";
    private static final String PREF_SELECTED_SITE_ID = "pref.PREF_SELECTED_SITE_ID";
    private static final String PREF_CRITICAL_MACHINES = "pref.PREF_CRITICAL_MACHINES";

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

    public void saveSites(ArrayList<Site> sites) {
        SecurePreferences.getInstance().setString(PREF_SITES, mGson.toJson(sites));
    }

    public ArrayList<Site> getSites() {
        String siteJsonString = SecurePreferences.getInstance().getString(PREF_SITES);
        Type listType = new TypeToken<ArrayList<Site>>() {
        }.getType();

        return mGson.fromJson(siteJsonString, listType);
    }

    public void saveCriticalMachines(HashMap<String, ArrayList<Integer>> criticalMachines) {
        String json = mGson.toJson(criticalMachines);
        SecurePreferences.getInstance().setString(PREF_CRITICAL_MACHINES, json);
    }

    public HashMap<String, ArrayList<Integer>> loadCriticalMachines() {
        String criticalMachinesJsonString = SecurePreferences.getInstance().getString(PREF_CRITICAL_MACHINES);
        Type listType = new TypeToken<HashMap<String, ArrayList<Integer>>>() {
        }.getType();

        return mGson.fromJson(criticalMachinesJsonString, listType);
    }

    public void saveSelectedSite(String siteUrl) {
        SecurePreferences.getInstance().setString(PREF_SELECTED_SITE_ID, siteUrl);
    }

    public String getSelectedSiteId() {
        return SecurePreferences.getInstance().getString(PREF_SELECTED_SITE_ID);
    }
}
