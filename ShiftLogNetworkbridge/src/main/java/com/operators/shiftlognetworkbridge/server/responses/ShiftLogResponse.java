package com.operators.shiftlognetworkbridge.server.responses;


import com.google.gson.annotations.SerializedName;
import com.operators.shiftloginfra.Event;

import java.util.ArrayList;

public class ShiftLogResponse extends ErrorBaseResponse {
    @SerializedName("events")
    private ArrayList<Event> mEvents;

    public ArrayList<Event> getEvents() {
        return mEvents;
    }
}