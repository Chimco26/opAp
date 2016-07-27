package com.operators.shiftlogcore.interfaces;

import com.operators.shiftloginfra.Event;

import java.util.ArrayList;

public interface ShiftLogPersistenceManagerInterface {

    ArrayList getShiftLogs();

    int getTotalRetries();

    int getRequestTimeout();

    void saveShiftLogs(ArrayList<Event> events);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);

}
