package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;
import com.operatorsapp.model.TopFiveItem;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by alex on 20/12/2018.
 */

public class StopAndCriticalEventsResponse  extends PojoResponse{

    @SerializedName("CriticalEvents")
    ArrayList<CriticalEvent> mCriticalEvents;

    @SerializedName("StopEventsList")
    ArrayList<StopEvent> mStopEvents;

    public StopAndCriticalEventsResponse(ArrayList<CriticalEvent> mCriticalEvents, ArrayList<StopEvent> mStopEvents) {
        this.mCriticalEvents = mCriticalEvents;
        this.mStopEvents = mStopEvents;
    }

    public ArrayList<CriticalEvent> getmCriticalEvents() {
        return mCriticalEvents;
    }

    public void setmCriticalEvents(ArrayList<CriticalEvent> mCriticalEvents) {
        this.mCriticalEvents = mCriticalEvents;
    }

    public ArrayList<StopEvent> getmStopEvents() {
        return mStopEvents;
    }

    public void setmStopEvents(ArrayList<StopEvent> mStopEvents) {
        this.mStopEvents = mStopEvents;
    }

    public ArrayList<TopFiveItem> getStopsAsTopFive(){
        ArrayList<TopFiveItem> list = new ArrayList<>();
        if (mStopEvents != null) {
            for (StopEvent reason : mStopEvents) {
                list.add(new TopFiveItem(TimeUtils.getMillisFromHMS(reason.mDuration) + "", reason.getmName(), reason.mColor));
            }
        }
        return list;
    }
}
