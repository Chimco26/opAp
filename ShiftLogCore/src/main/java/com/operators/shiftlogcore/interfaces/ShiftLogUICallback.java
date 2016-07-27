package com.operators.shiftlogcore.interfaces;


import com.operators.shiftloginfra.ErrorObjectInterface;
import com.operators.shiftloginfra.Event;

import java.util.ArrayList;

public interface ShiftLogUICallback<T> {

    void onGetShiftLogSucceeded(ArrayList<Event> events);

    void onGetShiftLogFailed(ErrorObjectInterface reason);
}
