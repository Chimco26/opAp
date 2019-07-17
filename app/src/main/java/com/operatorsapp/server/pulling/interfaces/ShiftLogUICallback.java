package com.operatorsapp.server.pulling.interfaces;


import com.example.common.Event;
import com.example.common.StandardResponse;

import java.util.ArrayList;

public interface ShiftLogUICallback {

    void onGetShiftLogSucceeded(ArrayList<Event> events);

    void onGetShiftLogFailed(StandardResponse reason);
}
