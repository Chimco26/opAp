package com.operators.shiftloginfra;


import com.example.common.StandardResponse;

import java.util.ArrayList;

public interface ShiftLogCoreCallback<T> {

    void onShiftLogSucceeded(ArrayList<T> events);

    void onShiftLogFailed(StandardResponse reason);
}
