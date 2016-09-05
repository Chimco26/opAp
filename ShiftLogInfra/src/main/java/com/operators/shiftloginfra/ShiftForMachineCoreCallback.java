package com.operators.shiftloginfra;


import com.operators.errorobject.ErrorObjectInterface;

public interface ShiftForMachineCoreCallback<T> {

    void onShiftForMachineSucceeded(ShiftForMachineResponse shiftForMachineResponse);

    void onShiftForMachineFailed(ErrorObjectInterface reason);
}
