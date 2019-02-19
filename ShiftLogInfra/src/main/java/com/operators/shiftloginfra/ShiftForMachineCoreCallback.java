package com.operators.shiftloginfra;


import com.operators.errorobject.ErrorObjectInterface;
import com.operators.shiftloginfra.model.ShiftForMachineResponse;

public interface ShiftForMachineCoreCallback<T> {

    void onShiftForMachineSucceeded(ShiftForMachineResponse shiftForMachineResponse);

    void onShiftForMachineFailed(ErrorObjectInterface reason);
}
