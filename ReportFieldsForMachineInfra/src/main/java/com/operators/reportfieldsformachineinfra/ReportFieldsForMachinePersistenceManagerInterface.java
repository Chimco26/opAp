package com.operators.reportfieldsformachineinfra;

/**
 * Created by Sergey on 02/08/2016.
 */
public interface ReportFieldsForMachinePersistenceManagerInterface {
    String getSiteUrl();

    String getSessionId();

    int getTotalRetries();

    int getRequestTimeout();

    int getMachineId();

    int getJobId();

    void setJobId(int jobId);

    void setSiteUrl(String siteUrl);

    void setSessionId(String sessionId);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);

    void setMachineId(int machineId);
}
