package com.operators.activejobslistformachineinfra;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface GetActiveJobsListForMachineNetworkBridgeInterface {
    void getActiveJobsForMachine(String siteUrl, String sessionId, int machineId, GetActiveJobsListForMachineCallback callback, int totalRetries, int specificRequestTimeout);
}
