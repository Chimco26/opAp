package com.operators.getmachinesstatusnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

/**
 * Created by User on 13/07/2016.
 */
public interface GetMachineStatusNetworkManagerInterface {
    EmeraldGetMachinesStatusServiceRequest getMachineStatusRetroFitServiceRequests(String siteUrl);

    EmeraldGetMachinesStatusServiceRequest getMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);

    EmeraldSetProductionModeForMachineRequest postProductionModeForMachineRetroFitServiceRequests(String siteUrl);

    EmeraldSetProductionModeForMachineRequest postProductionModeForMachineRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
