package com.operators.machinestatusinfra;

public interface MachineStatusPersistenceManagerInterface {

    String getSiteUrl();



    String getSessionId();

    int getTotalRetries();

    int getRequestTimeout();

    int getMachineId();


    void setSiteUrl(String siteUrl);

    void setSessionId(String sessionId);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);

    void setMachineId(int machineId);

}
