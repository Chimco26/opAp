package com.operators.loginnetworkbridge.interfaces;

import com.operators.infra.LoginNetworkBridgeCallback;

import java.util.concurrent.TimeUnit;

public interface LoginNetworkManagerInterface {

    EmeraldLoginServiceRequests getLoginRetroFitServiceRequests(String siteUrl, LoginNetworkBridgeCallback loginNetworkBridgeCallback);

    EmeraldLoginServiceRequests getLoginRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit, LoginNetworkBridgeCallback loginNetworkBridgeCallback);
}
