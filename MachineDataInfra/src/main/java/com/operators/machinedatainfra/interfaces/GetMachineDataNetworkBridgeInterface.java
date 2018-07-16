package com.operators.machinedatainfra.interfaces;


import com.operators.machinedatainfra.models.Widget;

public interface GetMachineDataNetworkBridgeInterface {
    void getMachineData(String siteUrl, String sessionId, int machineId, String startingFrom, GetMachineDataCallback<Widget> callback, int totalRetries, int specificRequestTimeout, Integer joshID);
}
