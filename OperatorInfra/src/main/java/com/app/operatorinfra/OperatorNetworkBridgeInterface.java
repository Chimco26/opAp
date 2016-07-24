package com.app.operatorinfra;

public interface OperatorNetworkBridgeInterface {
    void getOperator(String siteUrl, String sessionId, String operatorId, GetOperatorByIdCallback callback, int totalRetries, int specificRequestTimeout);

    void setOperatorForMachine(String siteUrl, String sessionId, String machineId, String operatorId, SetOperatorForMachineCallback callback, int totalRetries, int specificRequestTimeout);


}
