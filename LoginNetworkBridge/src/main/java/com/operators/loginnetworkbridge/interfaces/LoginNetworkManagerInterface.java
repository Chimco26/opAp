package com.operators.loginnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface LoginNetworkManagerInterface {

    EmeraldLoginServiceRequests getLoginRetroFitServiceRequests(String siteUrl);

    EmeraldLoginServiceRequests getLoginRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
