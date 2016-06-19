package com.operatorsapp.managers;

import android.util.Base64;

import com.operatorsapp.models.Site;
import com.operatorsapp.server.ErrorObject;
import com.operatorsapp.server.RequestsManager;
import com.operatorsapp.server.responses.SessionResponse;
import com.zemingo.logrecorder.ZLogger;

import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Response;


public class AccountManager {
    private static final String LOG_TAG = AccountManager.class.getSimpleName();
    private static String ENDPOINT = "https://apilordan.my.leadermes.com";
    private static AccountManager msInstance;
    private Site mSite;
    private AtomicBoolean mSessionActive = new AtomicBoolean(false);
    private OnLoginListener mLoginListener;

    public static AccountManager getInstance() {
        if (msInstance == null) {
            msInstance = new AccountManager();
        }
        return msInstance;
    }

    public AccountManager() {
        mSite = PersistenceManager.getInstance().getSite();
    }

    public void saveSite(String id, String siteName, String siteUrl, String sessionId, String userName, String password) {
        saveSite(new Site(id, siteName, siteUrl, sessionId, userName, password));
    }

    public void saveSite(Site newSite) {
        if (mSite == null) {
            mSite = new Site(newSite.getSiteId(), newSite.getSiteName(), newSite.getSiteUrl(), newSite.getSessionId(), newSite.getUserName(), newSite.getPassword());
        } else {
            mSite.setSessionId(newSite.getSiteId());
            mSite.setSessionId(newSite.getSiteName());
            mSite.setSessionId(newSite.getSiteUrl());
            mSite.setSiteName(newSite.getSessionId());
            mSite.setUserName(newSite.getUserName());
            mSite.setPassword(newSite.getPassword());
        }
        PersistenceManager.getInstance().saveSite(mSite);
    }

    public void performLogin(final String id, final String siteName, final String siteUrl, final String userName, String password) {
        final String EncryptedPassword = Base64.encodeToString(password.getBytes(), Base64.NO_WRAP);
        RequestsManager.getInstance().getUserSessionId(siteUrl, userName, EncryptedPassword, new RequestsManager.OnRequestManagerResponseListener<Response<SessionResponse>>() {
            @Override
            public void onRequestSucceed(Response<SessionResponse> result) {
                ZLogger.d(LOG_TAG, "onRequestSucceed(), " + result);
                if (mLoginListener != null) {
                    mLoginListener.onLoginSucceeded(siteName);
                }
                if (result.body().getGetUserSessionIDResult().getSessionIds().get(0).getSessionId() != null) {
                    AccountManager.getInstance().saveSite(id, siteName, siteUrl, result.body().getGetUserSessionIDResult().getSessionIds().get(0).getSessionId(), userName, EncryptedPassword);
                } else {
                    ZLogger.d(LOG_TAG, "onRequest(), getSessionId failed");
                    mLoginListener.onLoginFailed(ErrorObject.ErrorCode.Unknown);
                }
                mSessionActive.set(true);
            }

            @Override
            public void onRequestFailed(final ErrorObject reason) {
                ZLogger.d(LOG_TAG, "onRequestFailed(), " + reason.toString());
                if (mLoginListener != null) {
                    mLoginListener.onLoginFailed(reason.getError());
                }
            }
        });
    }

    public void registerLoginListener(OnLoginListener listener) {
        mLoginListener = listener;
    }

    public void unRegisterLoginListener() {
        mLoginListener = null;
    }

    public interface OnLoginListener {
        void onLoginSucceeded(String data);

        void onLoginFailed(ErrorObject.ErrorCode errorCode);
    }
}
