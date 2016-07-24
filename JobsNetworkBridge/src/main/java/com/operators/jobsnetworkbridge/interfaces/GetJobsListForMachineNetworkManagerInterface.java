package com.operators.jobsnetworkbridge.interfaces;


import java.util.concurrent.TimeUnit;

public interface GetJobsListForMachineNetworkManagerInterface {
    EmeraldGetJobsListServiceRequests getJobListForMachineStatusRetroFitServiceRequests(String siteUrl);

    EmeraldGetJobsListServiceRequests getJobListForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
