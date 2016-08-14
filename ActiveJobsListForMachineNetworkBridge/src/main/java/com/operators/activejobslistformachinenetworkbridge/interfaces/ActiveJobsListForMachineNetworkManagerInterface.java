package com.operators.activejobslistformachinenetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface ActiveJobsListForMachineNetworkManagerInterface {
    EmeraldGetActiveJobsListForMachineServiceRequests getActiveJobListForMachineStatusRetroFitServiceRequests(String siteUrl);

    EmeraldGetActiveJobsListForMachineServiceRequests getActiveJobListForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
