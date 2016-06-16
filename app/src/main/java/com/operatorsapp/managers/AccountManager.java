package com.operatorsapp.managers;

import android.text.TextUtils;
import android.util.Base64;

import com.operatorsapp.models.Site;
import com.operatorsapp.polling.LoginDemoJob;
import com.operatorsapp.server.ErrorObject;
import com.operatorsapp.server.RequestsManager;
import com.operatorsapp.utils.IdUtils;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class AccountManager implements LoginDemoJob.OnLoginDemoJobListener {
    private static final String LOG_TAG = AccountManager.class.getSimpleName();
    private static AccountManager msInstance;
    private final ArrayList<Site> mSites = new ArrayList<>();
    private AtomicBoolean mSessionActive = new AtomicBoolean(false);
    private OnLoginListener mLoginListener;
    private String mSiteId;

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

                mSiteId = newSiteId;
                LoginDemoJob.getInstance(mSiteId).registerLoginDemoJobListener(AccountManager.this);
                LoginDemoJob.getInstance(mSiteId).startJob(10, 10, TimeUnit.SECONDS);
            }

            @Override
            public void onRequestFailed(final ErrorObject reason) {
                ZLogger.d(LOG_TAG, "onRequestFailed(), ");
                if (mLoginListener != null) {
                    mLoginListener.onLoginFailed(reason.getError());
                }
                LoginDemoJob.getInstance(mSiteId).startJob(10, 10, TimeUnit.SECONDS);
            }
        });
    }

    public void registerLoginListener(OnLoginListener listener) {
        mLoginListener = listener;
    }

    public void unRegisterLoginListener() {
        mLoginListener = null;
        if (mSiteId != null)
            LoginDemoJob.getInstance(mSiteId).unRegisterLoginDemoJobListener();
    }

    @Override
    public void onLoginSucceeded(String data) {
        if (mLoginListener != null) {
            mLoginListener.onLoginSucceeded(data);
        }
    }

    @Override
    public void onLoginFailed(ErrorObject.ErrorCode errorCode) {
        if (mLoginListener != null) {
            mLoginListener.onLoginFailed(errorCode);
        }
    }

    public interface OnLoginListener {
        void onLoginSucceeded(String data);

        void onLoginFailed(ErrorObject.ErrorCode errorCode);
    }
}
