package com.operators.infra;


/**
 * Created by Admin on 26-Jun-16.
 */
public interface LoginNetworkBridgeInterface {
    void login(String siteUrl, String userName, String password, LoginCoreCallback loginCoreCallback);
}
