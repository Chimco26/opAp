package com.operators.machinestatusinfra;


public interface GetMachineStatusNetworkBridgeInterface
{
    void getMachineStatus(String siteUrl, String sessionId, int machineId, GetMachineStatusCallback callback, int totalRetries, int specificRequestTimeout);
}
