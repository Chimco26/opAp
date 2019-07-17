package com.operatorsapp.server.pulling.interfaces;


import com.example.common.StandardResponse;
import com.operators.shiftloginfra.model.ShiftForMachineResponse;

public interface ShiftForMachineUICallback {

    void onGetShiftForMachineSucceeded(ShiftForMachineResponse shiftForMachineResponse);

    void onGetShiftForMachineFailed(StandardResponse reason);
}
