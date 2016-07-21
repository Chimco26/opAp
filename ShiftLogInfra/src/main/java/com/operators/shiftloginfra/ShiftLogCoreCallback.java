package com.operators.shiftloginfra;


import java.util.ArrayList;

public interface ShiftLogCoreCallback<T> {

    void onShiftLogSucceeded(ArrayList<ShiftLog> shiftLogs);

    void onShiftLogFailed(ErrorObjectInterface reason);
}
