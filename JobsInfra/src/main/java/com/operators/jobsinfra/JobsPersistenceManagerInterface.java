package com.operators.jobsinfra;

public interface JobsPersistenceManagerInterface
{
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
