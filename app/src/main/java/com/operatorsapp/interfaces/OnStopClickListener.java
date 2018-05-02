package com.operatorsapp.interfaces;

public interface OnStopClickListener {
    void onStopClicked(int eventId,String startTime, String endTime, long duration);

    void onStopEventSelected(int eventID, String time, String eventEndTime, long duration);

}
