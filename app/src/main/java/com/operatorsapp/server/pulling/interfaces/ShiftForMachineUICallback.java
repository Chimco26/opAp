package com.operatorsapp.server.pulling.interfaces;


import com.example.common.callback.ErrorObjectInterface;
import com.operators.shiftloginfra.model.ShiftForMachineResponse;

public interface ShiftForMachineUICallback {

    void onGetShiftForMachineSucceeded(ShiftForMachineResponse shiftForMachineResponse);

    void onGetShiftForMachineFailed(ErrorObjectInterface reason);
}
