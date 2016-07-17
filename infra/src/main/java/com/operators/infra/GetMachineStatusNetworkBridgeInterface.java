package com.operators.infra;

/**
 * Created by User on 14/07/2016.
 */
public interface GetMachineStatusNetworkBridgeInterface
{
    void getMachineStatus(String siteUrl,String sessionId, int machineId, GetMachineStatusCallback callback, int totalRetries, int specificRequestTimeout);
}
