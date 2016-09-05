package com.operators.shiftlogcore.interfaces;


import com.operators.errorobject.ErrorObjectInterface;
import com.operators.shiftloginfra.Event;
import com.operators.shiftloginfra.ShiftForMachineResponse;

import java.util.ArrayList;

public interface ShiftForMachineUICallback {

    void onGetShiftForMachineSucceeded(ShiftForMachineResponse shiftForMachineResponse);

    void onGetShiftForMachineFailed(ErrorObjectInterface reason);
}
