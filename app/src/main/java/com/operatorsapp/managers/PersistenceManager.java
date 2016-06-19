package com.operatorsapp.managers;

import android.content.Context;

import com.google.gson.Gson;
import com.operatorsapp.models.Site;
import com.operatorsapp.utils.SecurePreferences;
import com.zemingo.logrecorder.ZLogger;

public class PersistenceManager {
    private static final String LOG_TAG = PersistenceManager.class.getSimpleName();

    private static final String PREF_SITE = "pref.PREF_SITE";

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

    public void saveSite(Site site) {
        SecurePreferences.getInstance().setString(PREF_SITE, mGson.toJson(site));
    }

    public Site getSite() {
        String siteJsonString = SecurePreferences.getInstance().getString(PREF_SITE);
        if (!siteJsonString.equals("")) {
            return mGson.fromJson(siteJsonString, Site.class);
        }
        return null;
    }
}
