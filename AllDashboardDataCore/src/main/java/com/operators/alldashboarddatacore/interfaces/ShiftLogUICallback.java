package com.operators.alldashboarddatacore.interfaces;


import com.example.common.Event;
import com.operators.errorobject.ErrorObjectInterface;

import java.util.ArrayList;

public interface ShiftLogUICallback {

    void onGetShiftLogSucceeded(ArrayList<Event> events);

    void onGetShiftLogFailed(ErrorObjectInterface reason);
}
