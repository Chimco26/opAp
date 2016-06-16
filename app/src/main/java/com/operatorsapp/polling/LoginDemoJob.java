package com.operatorsapp.polling;

import android.util.Base64;

import com.operatorsapp.managers.AccountManager;
import com.operatorsapp.models.Site;
import com.operatorsapp.server.ErrorObject;
import com.operatorsapp.server.RequestsManager;
import com.zemingo.logrecorder.ZLogger;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoginDemoJob extends EmeraldJobBase {
    private static final String LOG_TAG = LoginDemoJob.class.getSimpleName();
    private static LoginDemoJob msInstance;
    private Site mSite;
    private AtomicBoolean mSessionActive = new AtomicBoolean(false);
    private OnLoginDemoJobListener mLoginDemoJobListener;

    public static LoginDemoJob getInstance(String siteId) {
        if (msInstance == null) {
            msInstance = new LoginDemoJob(siteId);
        }
        return msInstance;
    }

    public LoginDemoJob(String siteId) {
        mSite = AccountManager.getInstance().getSiteById(siteId);
        if (mSite == null) {
            ZLogger.v(LOG_TAG, "registerForDataChangesEvent(), trying to create site job but site is null");
        }
    }

    @Override
    protected void executeJob(final OnJobFinishedListener onJobFinishedListener) {
        String pass = "api123";
        final String EncryptedPassword = Base64.encodeToString(pass.getBytes(), Base64.NO_WRAP);
        RequestsManager.getInstance().getUserSessionId(mSite.getSiteUrl(), mSite.getUserName(), EncryptedPassword, new RequestsManager.OnRequestManagerResponseListener<String>() {
            @Override
            public void onRequestSucceed(String result) {
                ZLogger.v(LOG_TAG, "onRequestSucceed(), ");
                if (mLoginDemoJobListener != null) {
                    mLoginDemoJobListener.onLoginSucceeded(mSite.getSiteName());
                }
                AccountManager.getInstance().saveSite(mSite.getSessionId(), mSite.getSiteName(), mSite.getSiteUrl(), result, mSite.getUserName(), EncryptedPassword, "");
                mSessionActive.set(true);
                onJobFinishedListener.onJobFinished();
            }

            @Override
            public void onRequestFailed(final ErrorObject reason) {
                ZLogger.d(LOG_TAG, "onRequestFailed(), ");
                if (mLoginDemoJobListener != null) {
                    mLoginDemoJobListener.onLoginFailed(reason.getError());
                }
                onJobFinishedListener.onJobFinished();
            }
        });
    }


    public void unRegisterLoginDemoJobListener() {
        mLoginDemoJobListener = null;
    }

    public void registerLoginDemoJobListener(OnLoginDemoJobListener listener) {
        mLoginDemoJobListener = listener;
    }

    public interface OnLoginDemoJobListener {
        void onLoginSucceeded(String data);

        void onLoginFailed(ErrorObject.ErrorCode errorCode);
    }
}
