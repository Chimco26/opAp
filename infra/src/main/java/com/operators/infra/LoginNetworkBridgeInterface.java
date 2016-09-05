package com.operators.infra;


public interface LoginNetworkBridgeInterface {
    void login(String siteUrl, String userName, String password,String language, LoginCoreCallback loginCoreCallback, int totalRetries, int specificRequestTimeout);
}
