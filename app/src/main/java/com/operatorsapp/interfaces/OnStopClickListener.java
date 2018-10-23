package com.operatorsapp.interfaces;

public interface OnStopClickListener {
    void onStopClicked(int eventId,String startTime, String endTime, long duration);

    void onSelectMode(int type, int eventID);

    void onStopEventSelected(Integer event, boolean b);

    void onSplitEventPressed(int eventID);

    void onLastItemUpdated();
}
