package com.operators.infra;


public interface GetMachinesNetworkBridgeInterface {
    void getMachines(String siteUrl, String sessionId, GetMachinesCallback<Machine> callback);
}
