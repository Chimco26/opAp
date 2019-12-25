package com.example.common.StopLogs;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StopLogsResponse extends StandardResponse{

    @SerializedName("EventsAndGroups")
    @Expose
    private Object eventsAndGroups;
    @SerializedName("events")
    @Expose
    private List<Event> events = null;

    public Object getEventsAndGroups() {
        return eventsAndGroups;
    }

    public void setEventsAndGroups(Object eventsAndGroups) {
        this.eventsAndGroups = eventsAndGroups;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}