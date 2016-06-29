package com.operators.getmachinesnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface GetMachineNetworkManagerInterface {

    EmeraldGetMachinesServiceRequests getMachinesRetroFitServiceRequests(String siteUrl);

    EmeraldGetMachinesServiceRequests getMachinesRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
