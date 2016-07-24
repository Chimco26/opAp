package com.operators.operatornetworkbridge.interfaces;


import java.util.concurrent.TimeUnit;

public interface GetOperatorByIdNetworkManagerInterface {
    EmeraldGetOperatorById getOperatorByIdRetroFitServiceRequests(String siteUrl);

    EmeraldGetOperatorById getOperatorByIdRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
