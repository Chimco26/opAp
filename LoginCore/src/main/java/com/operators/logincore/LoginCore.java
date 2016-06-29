package com.operators.logincore;

import android.util.Base64;
import android.util.Log;

import com.operators.infra.ErrorObjectInterface;
import com.operators.infra.GetMachinesCallback;
import com.operators.infra.GetMachinesNetworkBridgeInterface;
import com.operators.infra.LoginCoreCallback;
import com.operators.infra.LoginNetworkBridgeInterface;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;

public class LoginCore {

    private static final String LOG_TAG = LoginCore.class.getSimpleName();

    private PersistenceManagerInterface mPersistenceManagerInterface;
    private static LoginCore msInstance;
    private LoginNetworkBridgeInterface mLoginNetworkBridgeInterface;
    private GetMachinesNetworkBridgeInterface mGetMachinesNetworkBridgeInterface;

    public static LoginCore getInstance() {
        if (msInstance == null) {
            msInstance = new LoginCore();
        }
        return msInstance;
    }

    public LoginCore() {

    }


    public void inject(PersistenceManagerInterface persistenceManagerInterface, LoginNetworkBridgeInterface loginNetworkBridgeInterface, GetMachinesNetworkBridgeInterface getMachinesNetworkBridgeInterface) {
        mPersistenceManagerInterface = persistenceManagerInterface;
        mLoginNetworkBridgeInterface = loginNetworkBridgeInterface;
        mGetMachinesNetworkBridgeInterface = getMachinesNetworkBridgeInterface;
    }

    public void login(final String siteUrl, final String username, final String password, final LoginUICallback loginUICallback) {
        final String EncryptedPassword = Base64.encodeToString(password.getBytes(), Base64.NO_WRAP);
        mLoginNetworkBridgeInterface.login(siteUrl, username, EncryptedPassword, new LoginCoreCallback() {
            @Override
            public void onLoginSucceeded(String sessionId) {
                ZLogger.d(LOG_TAG, "login, onLoginSucceeded(), " + sessionId);
                saveSite(sessionId, siteUrl, username, password);

                mGetMachinesNetworkBridgeInterface.getMachinesForFactory(siteUrl, sessionId, new GetMachinesCallback() {
                    @Override
                    public void onLoginSucceeded(ArrayList machines) {

                    }

                    @Override
                    public void onLoginFailed(ErrorObjectInterface reason) {

                    }
                });
                loginUICallback.onLoginSucceeded();
            }

            @Override
            public void onLoginFailed(ErrorObjectInterface reason) {
                ZLogger.d(LOG_TAG, "login, onLoginFailed");
                loginUICallback.onLoginFailed(reason);
            }
        });
    }

    public void silentLogin(final LoginUICallback loginUICallback) {
        if (isAllDataAreValid()) {
            login(getSaveSiteUrl(), getSaveUsername(), getSavePassword(), loginUICallback);
        } else {
            Log.d(LOG_TAG, "silentLogin, Data to login is null");
            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Credentials_mismatch, "Data to login is null");
            loginUICallback.onLoginFailed(errorObject);
        }
    }


    public String getSaveSiteUrl() {
        return mPersistenceManagerInterface.getSiteUrl();

    }

    public String getSaveUsername() {
        return mPersistenceManagerInterface.getUserName();

    }

    public String getSavePassword() {
        return mPersistenceManagerInterface.getPassword();

    }

    public String getSaveSessionId() {
        return mPersistenceManagerInterface.getSessionId();

    }

    private boolean isAllDataAreValid() {
        String siteUrl = getSaveSiteUrl();
        String userName = getSaveUsername();
        String password = getSavePassword();
        return siteUrl != null && userName != null && password != null;
    }

    public void saveSite(String sessionId, String siteUrl, String username, String password) {
        mPersistenceManagerInterface.setSessionId(sessionId);
        mPersistenceManagerInterface.setSiteUrl(siteUrl);
        mPersistenceManagerInterface.setUsername(username);
        mPersistenceManagerInterface.setPassword(password);
    }
}
