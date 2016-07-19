package com.operators.jobsnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;


public interface StartJobForMachineNetworkManagerInterface {
    EmeraldStartJobServiceRequests startJobForMachineStatusRetroFitServiceRequests(String siteUrl);

    EmeraldStartJobServiceRequests startJobForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
