package com.operators.infra;


/**
 * Created by Admin on 26-Jun-16.
 */
public interface GetMachinesNetworkBridgeInterface {
    void getMachinesForFactory(String siteUrl, String sessionId, GetMachinesCallback callback);
}
