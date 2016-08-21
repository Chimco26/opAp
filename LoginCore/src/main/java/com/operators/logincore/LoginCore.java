package com.operators.logincore;

import android.util.Base64;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.infra.GetMachinesCallback;
import com.operators.infra.GetMachinesNetworkBridgeInterface;
import com.operators.infra.LoginCoreCallback;
import com.operators.infra.LoginNetworkBridgeInterface;
import com.operators.infra.Machine;
import com.operators.logincore.interfaces.LoginUICallback;
import com.operators.logincore.interfaces.LoginPersistenceManagerInterface;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;

public class LoginCore {

    private static final String LOG_TAG = LoginCore.class.getSimpleName();
    private LoginPersistenceManagerInterface mLoginPersistenceManagerInterface;
    private static LoginCore msInstance;
    private LoginNetworkBridgeInterface mLoginNetworkBridgeInterface;
    private GetMachinesNetworkBridgeInterface mGetMachinesNetworkBridgeInterface;

    public static LoginCore getInstance() {
        if (msInstance == null) {
            msInstance = new LoginCore();
        }
        return msInstance;
    }

    public void inject(LoginPersistenceManagerInterface loginPersistenceManagerInterface, LoginNetworkBridgeInterface loginNetworkBridgeInterface, GetMachinesNetworkBridgeInterface getMachinesNetworkBridgeInterface) {
        mLoginPersistenceManagerInterface = loginPersistenceManagerInterface;
        mLoginNetworkBridgeInterface = loginNetworkBridgeInterface;
        mGetMachinesNetworkBridgeInterface = getMachinesNetworkBridgeInterface;
    }

    public void login(final String siteUrl, final String username, final String password, final LoginUICallback<Machine> loginUICallback) {
        final String EncryptedPassword = Base64.encodeToString(password.getBytes(), Base64.NO_WRAP);
            mLoginNetworkBridgeInterface.login(siteUrl, username, mLoginPersistenceManagerInterface.getCurrentLang(), EncryptedPassword, new LoginCoreCallback() {
            @Override
            public void onLoginSucceeded(String sessionId) {
                ZLogger.d(LOG_TAG, "login, onGetMachinesSucceeded(), " + sessionId);
                saveSessionData(sessionId, siteUrl, username, password);

                mGetMachinesNetworkBridgeInterface.getMachines(siteUrl, sessionId, new GetMachinesCallback<Machine>() {
                    @Override
                    public void onGetMachinesSucceeded(ArrayList<Machine> machines) {
                        ZLogger.d(LOG_TAG, "getMachines, onGetMachinesSucceeded(), " + machines.size() + " machines");
                        loginUICallback.onLoginSucceeded(machines);
                    }

                    @Override
                    public void onGetMachinesFailed(ErrorObjectInterface reason) {
                        ZLogger.d(LOG_TAG, "getMachines, onGetMachinesFailed" + reason.getDetailedDescription());
                        loginUICallback.onLoginFailed(reason);
                    }
                }, mLoginPersistenceManagerInterface.getTotalRetries(), mLoginPersistenceManagerInterface.getRequestTimeout());
            }

            @Override
            public void onLoginFailed(ErrorObjectInterface reason) {
                ZLogger.d(LOG_TAG, "login, onLoginFailed");
                loginUICallback.onLoginFailed(reason);
            }
        }, mLoginPersistenceManagerInterface.getTotalRetries(), mLoginPersistenceManagerInterface.getRequestTimeout());
    }

    public void silentLoginFromDashBoard(final String siteUrl, final String username, final String password, final LoginUICallback<Machine> loginUICallback) {
        final String EncryptedPassword = Base64.encodeToString(password.getBytes(), Base64.NO_WRAP);
        mLoginNetworkBridgeInterface.login(siteUrl, username, mLoginPersistenceManagerInterface.getCurrentLang(), EncryptedPassword, new LoginCoreCallback() {
            @Override
            public void onLoginSucceeded(String sessionId) {
                ZLogger.d(LOG_TAG, "silentLoginFromDashBoard, onLoginSucceeded(), " + sessionId);
                saveSessionData(sessionId, siteUrl, username, password);
            }

            @Override
            public void onLoginFailed(ErrorObjectInterface reason) {
                ZLogger.d(LOG_TAG, "silentLoginFromDashBoard, onLoginFailed");
                loginUICallback.onLoginFailed(reason);
            }
        }, mLoginPersistenceManagerInterface.getTotalRetries(), mLoginPersistenceManagerInterface.getRequestTimeout());
    }


    public void saveSessionData(String sessionId, String siteUrl, String username, String password) {
        mLoginPersistenceManagerInterface.setSessionId(sessionId);
        mLoginPersistenceManagerInterface.setSiteUrl(siteUrl);
        mLoginPersistenceManagerInterface.setUsername(username);
        mLoginPersistenceManagerInterface.setPassword(password);

    }
}