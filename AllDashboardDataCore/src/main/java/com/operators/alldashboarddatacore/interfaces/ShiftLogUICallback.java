package com.operators.alldashboarddatacore.interfaces;


import com.operators.errorobject.ErrorObjectInterface;
import com.ravtech.david.sqlcore.Event;

import java.util.ArrayList;

public interface ShiftLogUICallback {

    void onGetShiftLogSucceeded(ArrayList<Event> events);

    void onGetShiftLogFailed(ErrorObjectInterface reason);
}
