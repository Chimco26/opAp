package com.operators.activejobslistformachineinfra;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface ActiveJobsListForMachineNetworkBridgeInterface {
    void getActiveJobsForMachine(String siteUrl, String sessionId, int machineId, ActiveJobsListForMachineCallback callback, int totalRetries, int specificRequestTimeout);
}
