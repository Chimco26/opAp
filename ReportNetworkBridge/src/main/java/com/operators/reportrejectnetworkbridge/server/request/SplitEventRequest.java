package com.operators.reportrejectnetworkbridge.server.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by alex on 05/07/2018.
 */

public class SplitEventRequest {

    @SerializedName("SessionID")
    @Expose
    private String sessionID;

    @SerializedName("EventID")
    @Expose
    private String eventId;

    public SplitEventRequest(String sessionId, String eventId) {

        this.sessionID = sessionId;
        this.eventId = eventId;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
