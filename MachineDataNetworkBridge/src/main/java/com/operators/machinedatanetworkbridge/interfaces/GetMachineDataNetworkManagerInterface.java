package com.operators.machinedatanetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

/**
 * Created by User on 13/07/2016.
 */
public interface GetMachineDataNetworkManagerInterface {
    EmeraldGetMachinesDataServiceRequest getMachineDataRetroFitServiceRequests(String siteUrl);

    EmeraldGetMachinesDataServiceRequest getMachineDataRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
