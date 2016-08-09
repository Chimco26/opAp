package com.operators.reportfieldsformachinenetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sergey on 02/08/2016.
 */
public interface GetReportFieldsForMachineNetworkManagerInterface {
     EmeraldGetReportFieldsForMachineRequest getReportFieldsForMachineStatusRetroFitServiceRequests(String siteUrl);

    EmeraldGetReportFieldsForMachineRequest getReportFieldsForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);

}

