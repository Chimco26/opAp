package com.operators.shiftloginfra;


import com.example.common.callback.ErrorObjectInterface;

import java.util.ArrayList;

public interface ShiftLogCoreCallback<T> {

    void onShiftLogSucceeded(ArrayList<T> events);

    void onShiftLogFailed(ErrorObjectInterface reason);
}
