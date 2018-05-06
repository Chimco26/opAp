package com.operatorsapp.interfaces;

import com.ravtech.david.sqlcore.Event;

public interface OnStopClickListener {
    void onStopClicked(int eventId,String startTime, String endTime, long duration);

    void onSelectMode(int type);

    void onStopEventSelected(Event event, boolean b);
}
