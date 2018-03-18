package com.operators.shiftloginfra;


import com.ravtech.david.sqlcore.Event;

import java.util.ArrayList;

public interface ShiftLogPersistenceManagerInterface {
    String getSiteUrl();

    String getSessionId();

    String getShiftLogStartingFrom();

    ArrayList getShiftLogs();

    int getTotalRetries();

    int getRequestTimeout();

    int getMachineId();

    int getPollingFrequency();

    void setPolingFrequency(int polingFrequency);

    void setSiteUrl(String siteUrl);

    void setSessionId(String sessionId);

    void setShiftLogStartingFrom(String startingFrom);

    void saveShiftLogs(ArrayList<Event> events);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);

    void setMachineId(int machineId);

}
