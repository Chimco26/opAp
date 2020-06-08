package com.operatorsapp.server.responses;

import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StopReasonsResponse extends StandardResponse {


    @SerializedName("EventsAndGroups")
    private ArrayList<StopReasonsGroup> mStopReasonsList;

    public ArrayList<StopReasonsGroup> getStopReasonsList() {
        return mStopReasonsList;
    }
}
