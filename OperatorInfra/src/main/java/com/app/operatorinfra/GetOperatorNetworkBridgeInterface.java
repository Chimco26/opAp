package com.app.operatorinfra;

public interface GetOperatorNetworkBridgeInterface {
    void getOperator(String siteUrl, String sessionId, String operatorId, GetOperatorByIdCallback callback, int totalRetries, int specificRequestTimeout);

}
