package com.operators.alldashboarddatacore.interfaces;


import com.example.common.Event;
import com.example.common.callback.ErrorObjectInterface;

import java.util.ArrayList;

public interface ShiftLogUICallback {

    void onGetShiftLogSucceeded(ArrayList<Event> events);

    void onGetShiftLogFailed(ErrorObjectInterface reason);
}
