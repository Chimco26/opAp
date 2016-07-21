package com.operators.shiftlogcore.interfaces;

import com.operators.shiftloginfra.ShiftLog;

import java.util.ArrayList;

public interface ShiftLogPersistenceManagerInterface {

    ArrayList getShiftLogs();

    int getTotalRetries();

    int getRequestTimeout();

    void saveShiftLogs(ArrayList<ShiftLog> shiftLogs);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);

}
