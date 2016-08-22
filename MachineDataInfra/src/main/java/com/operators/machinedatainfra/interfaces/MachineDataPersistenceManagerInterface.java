package com.operators.machinedatainfra.interfaces;

public interface MachineDataPersistenceManagerInterface {

    String getSiteUrl();

    String getSessionId();

    String getMachineDataStartingFrom();

    int getTotalRetries();

    int getRequestTimeout();

    int getMachineId();

    int getPollingFrequency();

    void setPolingFrequency(int polingFrequency);

    void setSiteUrl(String siteUrl);

    void setSessionId(String sessionId);

    void setMachineDataStartingFrom(String startingFrom);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);

    void setMachineId(int machineId);

}
