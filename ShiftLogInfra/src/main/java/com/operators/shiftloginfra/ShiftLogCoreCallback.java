package com.operators.shiftloginfra;


import com.operators.errorobject.ErrorObjectInterface;

import java.util.ArrayList;

public interface ShiftLogCoreCallback<T> {

    void onShiftLogSucceeded(ArrayList<Event> events);

    void onShiftLogFailed(ErrorObjectInterface reason);
}
