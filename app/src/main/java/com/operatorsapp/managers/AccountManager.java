package com.operatorsapp.managers;

import android.text.TextUtils;
import android.util.Base64;

import com.operatorsapp.models.Site;
import com.operatorsapp.server.ErrorObject;
import com.operatorsapp.server.RequestsManager;
import com.operatorsapp.utils.IdUtils;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class AccountManager {
    private static final String LOG_TAG = AccountManager.class.getSimpleName();
    private static AccountManager msInstance;
    private final ArrayList<Site> mSites = new ArrayList<>();
    private AtomicBoolean mDuringSilentLogin = new AtomicBoolean(false);
    private AtomicBoolean mSessionActive = new AtomicBoolean(false);
    private OnSessionListener mSessionListener;
    private OnLoginListener mLoginListener;

    public static AccountManager getInstance() {
        if (msInstance == null) {
            msInstance = new AccountManager();
        }
        return msInstance;
    }

    public AccountManager() {
        if (PersistenceManager.getInstance().getSites() != null) {
            mSites.addAll(PersistenceManager.getInstance().getSites());
        }
    }

    public Site getSiteById(String id) {
        Site wantedSite = null;
        for (Site site : mSites) {
            if (site.getSiteId().equals(id)) {
                wantedSite = site;
            }
        }
        return wantedSite;
    }

    public void saveSelectedSite(String siteId) {
        PersistenceManager.getInstance().saveSelectedSite(siteId);
    }

    public String getSelectedSiteId() {
        return PersistenceManager.getInstance().getSelectedSiteId();
    }

    public String getAliasForSiteUrlExcludingSiteId(String siteUrl, String siteId) {
        for (Site site : mSites) {
            if (site.getSiteUrl().equals(siteUrl) && !site.getSiteId().equals(siteId)) {
                return site.getSiteName();
            }
        }

        return null;
    }


    public void deleteSite(String siteIdForDelete) {
        int index = -1;
        for (Site site : mSites) {
            if (site.getSiteId().equals(siteIdForDelete)) {
                index = mSites.indexOf(site);
                break;
            }
        }

        if (index != -1) {
            // check if future to be deleted site is also save as selectedSiteUrl, if so clear it
            Site site = mSites.get(index);
            if (site.getSiteId().equals(getSelectedSiteId())) {
                PersistenceManager.getInstance().saveSelectedSite(null);
            }

            if (mSites.remove(index) != null) {
                PersistenceManager.getInstance().saveSites(mSites);
            } else {
                ZLogger.w(LOG_TAG, "deleteSite(), couldn't delete site although site exists");
            }
        }
    }

    public void saveSite(String id, String siteName, String siteUrl, String sessionId, String userName, String password, String previousSiteId) {
        saveSite(new Site(id, siteName, siteUrl, sessionId, userName, password), previousSiteId);
    }

    public void saveSite(Site newSite, String previousSiteId) {
        String siteId = TextUtils.isEmpty(previousSiteId) ? newSite.getSiteId() : previousSiteId;
        boolean siteAlreadyExists = false;
        for (Site site : mSites) {
            if (site.getSiteId().equals(siteId)) {
                if (!TextUtils.isEmpty(previousSiteId)) {
                    if (previousSiteId.equals(getSelectedSiteId())) {// saving site id of focused item to retrieve it later
                        saveSelectedSite(newSite.getSiteId());
                    }
                    site.setSiteUrl(newSite.getSiteUrl());
                }
                site.setSessionId(newSite.getSessionId());
                site.setSiteName(newSite.getSiteName());
                site.setUserName(newSite.getUserName());
                site.setPassword(newSite.getPassword());
                siteAlreadyExists = true;
                break;
            }
        }

        if (!siteAlreadyExists) {
            mSites.add(newSite);
        }
        PersistenceManager.getInstance().saveSites(mSites);
    }

    /*public boolean isAtLeastOneSiteConnectedBefore() {
        return mSites.size() > 0;
    }*/

    /*public ArrayList<Site> getSites() {
        return mSites;
    }*/

    /*public boolean isSessionActive() {
        return mSessionActive.get();
    }*/

    /*public void activateSilentLogin() {
        if (mDuringSilentLogin.getAndSet(true)) {
            return;
        }

        final AtomicInteger numberOfSite = new AtomicInteger(mSites.size());
        final AtomicInteger numOfSiteFailedToConnect = new AtomicInteger(0);
        for (final Site site : mSites) {
            RequestsManager.getInstance().getUserSessionId(site.getSiteUrl(), site.getUserName(), site.getPassword(), new RequestsManager.OnRequestManagerResponseListener<String>() {
                @Override
                public void onRequestSucceed(String result) {
                    site.setSessionId(result);
                    AccountManager.getInstance().saveSite(site, null);
                    ZLogger.d(LOG_TAG, "onRequestSucceed(), log in succeeded");
                    if (mSessionListener != null) {
                        mSessionListener.onSessionActivated();
                    }
                    checkIfAllSitesHaveFinishedThereAPiCalls(numberOfSite);
                }

                @Override
                public void onRequestFailed(ErrorObject reason) {
                    ZLogger.d(LOG_TAG, "onRequestFailed(), ");
                    checkIfAllSitesHaveFinishedThereAPiCalls(numberOfSite);
                    if (numOfSiteFailedToConnect.incrementAndGet() == mSites.size()) {
                        if (mSessionListener != null) {
                            mSessionListener.onAllSessionFailed();
                        }
                    }
                }
            });
        }
    }*/

    public void performLogin(final String siteName, final String siteUrl, final String userName, String password, final String previousSiteId) {
        String generatedId = previousSiteId;
        if (TextUtils.isEmpty(previousSiteId)) {
            generatedId = IdUtils.generateId();
        }

        final String newSiteId = generatedId;
        final String EncryptedPassword = Base64.encodeToString(password.getBytes(), Base64.NO_WRAP);
        RequestsManager.getInstance().getUserSessionId(siteUrl, userName, EncryptedPassword, new RequestsManager.OnRequestManagerResponseListener<String>() {
            @Override
            public void onRequestSucceed(String result) {
                ZLogger.d(LOG_TAG, "onRequestSucceed(), ");
                if (mLoginListener != null) {
                    mLoginListener.onLoginSucceeded(siteName);
                }
                AccountManager.getInstance().saveSite(newSiteId, siteName, siteUrl, result, userName, EncryptedPassword, previousSiteId);
                mSessionActive.set(true);
            }

            @Override
            public void onRequestFailed(final ErrorObject reason) {
                if (mLoginListener != null) {
                    mLoginListener.onLoginFailed(reason.getError());
                }
                ZLogger.d(LOG_TAG, "onRequestFailed(), ");
            }
        });
    }

   /* public void registerSessionActivationListener(OnSessionListener listener) {
        mSessionListener = listener;
    }*/

    public void registerLoginListener(OnLoginListener listener) {
        mLoginListener = listener;
    }

   /* public void unRegisterSessionActivationListener() {
        mSessionListener = null;
    }*/

    public void unRegisterLoginListener() {
        mLoginListener = null;
    }

   /* private void checkIfAllSitesHaveFinishedThereAPiCalls(AtomicInteger numberOfSite) {
        if (numberOfSite.decrementAndGet() == 0) {
            mDuringSilentLogin.set(false);
        }
    }*/

    public interface OnSessionListener {
        void onSessionActivated();

        void onAllSessionFailed();
    }

    public interface OnLoginListener {
        void onLoginSucceeded(String data);

        void onLoginFailed(ErrorObject.ErrorCode errorCode);
    }
}
