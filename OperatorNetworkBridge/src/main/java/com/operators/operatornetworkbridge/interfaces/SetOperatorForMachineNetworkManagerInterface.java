package com.operators.operatornetworkbridge.interfaces;


import java.util.concurrent.TimeUnit;

public interface SetOperatorForMachineNetworkManagerInterface {
    EmeraldSetOperatorForMachine setOperatorForMachineRetroFitServiceRequests(String siteUrl);

    EmeraldSetOperatorForMachine setOperatorForMachineRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
