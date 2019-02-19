package com.operators.alldashboarddatacore.interfaces;


import com.operators.errorobject.ErrorObjectInterface;
import com.operators.shiftloginfra.model.ShiftForMachineResponse;

public interface ShiftForMachineUICallback {

    void onGetShiftForMachineSucceeded(ShiftForMachineResponse shiftForMachineResponse);

    void onGetShiftForMachineFailed(ErrorObjectInterface reason);
}
