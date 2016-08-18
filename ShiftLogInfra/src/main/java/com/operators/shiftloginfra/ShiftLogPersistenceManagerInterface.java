package com.operators.shiftloginfra;

import com.operators.shiftloginfra.Event;

import java.util.ArrayList;

public interface ShiftLogPersistenceManagerInterface {
    String getSiteUrl();

    String getSessionId();

    ArrayList getShiftLogs();

    int getTotalRetries();

    int getRequestTimeout();
    int getMachineId();

    int getPollingFrequency();

    void setPolingFrequency(int polingFrequency);

    void setSiteUrl(String siteUrl);

    void setSessionId(String sessionId);

    void saveShiftLogs(ArrayList<Event> events);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);
    void setMachineId(int machineId);

}
