package com.operators.machinestatusinfra.interfaces;

public interface MachineStatusPersistenceManagerInterface {

    String getSiteUrl();


    String getSessionId();

    int getTotalRetries();

    int getRequestTimeout();

    int getMachineId();

    int getPollingFrequency();

    void setPolingFrequency(int polingFrequency);

    void setSiteUrl(String siteUrl);

    void setSessionId(String sessionId);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);

    void setMachineId(int machineId);

}
