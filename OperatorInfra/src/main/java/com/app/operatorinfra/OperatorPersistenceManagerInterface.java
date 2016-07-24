package com.app.operatorinfra;

public interface OperatorPersistenceManagerInterface {
    String getSiteUrl();

    String getSessionId();

    int getTotalRetries();

    int getRequestTimeout();

    int getMachineId();

    String getOperatorId();

    String getOperatorName();

    void setSiteUrl(String siteUrl);

    void setSessionId(String sessionId);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);

    void setMachineId(int machineId);

    void setOperatorId(String operatorId);

    void setOperatorName(String operatorName);
}
