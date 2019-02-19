package com.operators.shiftlognetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface ShiftLogNetworkManagerInterface {
    EmeraldShiftLogServiceRequests getShiftLogRetroFitServiceRequests(String siteUrl);

    EmeraldShiftLogServiceRequests getShiftLogRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);

    EmeraldShiftForMachineServiceRequests getShiftForMachineServiceRequests(String siteUrl);

    EmeraldShiftForMachineServiceRequests getShiftForMachineServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);

    EmeraldActualBarExtraDetailsServiceRequest getActualBarExtraDetails(String siteUrl, int timeout, TimeUnit timeUnit);
}
