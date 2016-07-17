package com.operators.infra;

import java.util.ArrayList;

public interface PersistenceManagerInterface {

    String getSiteUrl();

    String getUserName();

    String getPassword();

    String getSessionId();

    int getTotalRetries();

    int getRequestTimeout();

    int getMachineId();


    void setSiteUrl(String siteUrl);

    void setUsername(String username);

    void setPassword(String password);

    void setSessionId(String sessionId);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);

    void setMachineId(int machineId);

}
