package com.operators.logincore;

import android.util.Base64;

import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.operators.infra.GetMachinesCallback;
import com.operators.infra.GetMachinesNetworkBridgeInterface;
import com.operators.infra.LoginCoreCallback;
import com.operators.infra.LoginNetworkBridgeInterface;
import com.operators.infra.Machine;
import com.operators.logincore.interfaces.LoginUICallback;
import com.operators.logincore.interfaces.LoginPersistenceManagerInterface;

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
            mLoginNetworkBridgeInterface.login(siteUrl, username, EncryptedPassword, mLoginPersistenceManagerInterface.getCurrentLang(),  new LoginCoreCallback() {
            @Override
            public void onLoginSucceeded(final String sessionId, final String siteName, final int userId) {
                OppAppLogger.getInstance().d(LOG_TAG, "login, onGetMachinesSucceeded(), " + sessionId);
                saveSessionData(sessionId, siteUrl, username, password, userId);

                mGetMachinesNetworkBridgeInterface.getMachines(siteUrl, sessionId, new GetMachinesCallback<Machine>() {
                    @Override
                    public void onGetMachinesSucceeded(ArrayList<Machine> machines) {
                        OppAppLogger.getInstance().d(LOG_TAG, "getMachines, onGetMachinesSucceeded(), " + machines.size() + " machines");
                        loginUICallback.onLoginSucceeded(machines, siteName);
                    }

                    @Override
                    public void onGetMachinesFailed(ErrorObjectInterface reason) {
                        OppAppLogger.getInstance().d(LOG_TAG, "getMachines, onGetMachinesFailed" + reason.getDetailedDescription());
                        loginUICallback.onLoginFailed(reason);
                    }
                }, mLoginPersistenceManagerInterface.getTotalRetries(), mLoginPersistenceManagerInterface.getRequestTimeout());
            }

            @Override
            public void onLoginFailed(ErrorObjectInterface reason) {
                OppAppLogger.getInstance().d(LOG_TAG, "login, onLoginFailed");
                loginUICallback.onLoginFailed(reason);
            }
        }, mLoginPersistenceManagerInterface.getTotalRetries(), mLoginPersistenceManagerInterface.getRequestTimeout());
    }

    public void silentLoginFromDashBoard(final String siteUrl, final String username, final String password, final LoginUICallback<Machine> loginUICallback) {
        final String EncryptedPassword = Base64.encodeToString(password.getBytes(), Base64.NO_WRAP);

        mLoginNetworkBridgeInterface.login(siteUrl, username, EncryptedPassword, mLoginPersistenceManagerInterface.getCurrentLang(), new LoginCoreCallback() {
            @Override
            public void onLoginSucceeded(String sessionId, String siteName, final int userId) {

                OppAppLogger.getInstance().d(LOG_TAG, "silentLoginFromDashBoard, onLoginSucceeded(), " + sessionId);

                saveSessionData(sessionId, siteUrl, username, password, userId);
                loginUICallback.onLoginSucceeded(null, siteName);
            }

            @Override
            public void onLoginFailed(ErrorObjectInterface reason) {
                OppAppLogger.getInstance().d(LOG_TAG, "silentLoginFromDashBoard, onLoginFailed");
                loginUICallback.onLoginFailed(reason);
            }
        }, mLoginPersistenceManagerInterface.getTotalRetries(), mLoginPersistenceManagerInterface.getRequestTimeout());
    }


    public void saveSessionData(String sessionId, String siteUrl, String username, String password, int userId) {
        mLoginPersistenceManagerInterface.setSessionId(sessionId);
        mLoginPersistenceManagerInterface.setSiteUrl(siteUrl);
        mLoginPersistenceManagerInterface.setUsername(username);
        mLoginPersistenceManagerInterface.setPassword(password);
        mLoginPersistenceManagerInterface.setUserId(userId);

    }
}