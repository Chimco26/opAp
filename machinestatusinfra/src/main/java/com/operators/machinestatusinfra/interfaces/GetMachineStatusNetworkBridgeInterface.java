package com.operators.machinestatusinfra.interfaces;


public interface GetMachineStatusNetworkBridgeInterface {
    void getMachineStatus(String siteUrl, String sessionId, int machineId, GetMachineStatusCallback callback, int totalRetries, int specificRequestTimeout, Integer joshID);

}
