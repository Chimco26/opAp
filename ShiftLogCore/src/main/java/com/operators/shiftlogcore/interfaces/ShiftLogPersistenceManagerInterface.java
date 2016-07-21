package com.operators.shiftlogcore.interfaces;

import com.operators.shiftloginfra.ShiftLog;

import java.util.ArrayList;

public interface ShiftLogPersistenceManagerInterface {

    ArrayList getShiftLogs();


    void saveShiftLogs(ArrayList<ShiftLog> shiftLogs);

}
