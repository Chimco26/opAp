package com.operators.shiftlogcore.interfaces;


import com.operators.shiftloginfra.ErrorObjectInterface;
import com.operators.shiftloginfra.ShiftLog;

import java.util.ArrayList;

public interface ShiftLogUICallback<T> {

    void onGetShiftLogSucceeded(ArrayList<ShiftLog> shiftLogs);

    void onGetShiftLogFailed(ErrorObjectInterface reason);
}
