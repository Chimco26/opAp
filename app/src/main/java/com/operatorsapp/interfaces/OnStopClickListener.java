package com.operatorsapp.interfaces;


import com.example.common.Event;

public interface OnStopClickListener {
    void onStopClicked(int eventId,String startTime, String endTime, long duration);

    void onSelectMode(Event eventID);

    void onStopEventSelected(Long event, boolean b);

    void onSplitEventPressed(long eventID);

    void onLastItemUpdated();
}
