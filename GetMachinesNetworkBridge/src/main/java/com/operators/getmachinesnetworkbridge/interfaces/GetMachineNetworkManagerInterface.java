package com.operators.getmachinesnetworkbridge.interfaces;

import com.operators.infra.LoginNetworkBridgeCallback;

import java.util.concurrent.TimeUnit;

public interface GetMachineNetworkManagerInterface {

    EmeraldGetMachinesServiceRequests getMachinesRetroFitServiceRequests(String siteUrl, LoginNetworkBridgeCallback loginNetworkBridgeCallback);

    EmeraldGetMachinesServiceRequests getMachinesRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit, LoginNetworkBridgeCallback loginNetworkBridgeCallback);
}
