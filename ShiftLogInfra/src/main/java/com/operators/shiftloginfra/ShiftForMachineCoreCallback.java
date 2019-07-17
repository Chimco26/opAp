package com.operators.shiftloginfra;


import com.example.common.StandardResponse;
import com.operators.shiftloginfra.model.ShiftForMachineResponse;

public interface ShiftForMachineCoreCallback<T> {

    void onShiftForMachineSucceeded(ShiftForMachineResponse shiftForMachineResponse);

    void onShiftForMachineFailed(StandardResponse reason);
}
