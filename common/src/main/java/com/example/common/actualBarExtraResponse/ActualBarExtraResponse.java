package com.example.common.actualBarExtraResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.common.Event;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ActualBarExtraResponse implements Parcelable {


    @SerializedName("FunctionSucceed")
    @Expose
    private Boolean functionSucceed;
    @SerializedName("Rejects")
    @Expose
    private ArrayList<Reject> rejects = null;
    @SerializedName("Inventory")
    @Expose
    private ArrayList<Inventory> inventory = null;
    @SerializedName("Notification")
    @Expose
    private ArrayList<Notification> notification = null;
    @SerializedName("WorkingEvents")
    @Expose
    private ArrayList<WorkingEvent> workingEvents = null;
    private ArrayList<Event> alarmsEvents = null;

    public ArrayList<WorkingEvent> getWorkingEvents() {
        if (workingEvents == null){
            return new ArrayList<>();
        }
        return workingEvents;
    }

    public void setWorkingEvents(ArrayList<WorkingEvent> workingEvents) {
        this.workingEvents = workingEvents;
    }

    public ArrayList<Event> getAlarmsEvents() {
        if (alarmsEvents == null){
            alarmsEvents = new ArrayList<>();
        }
        return alarmsEvents;
    }

    public void setAlarmsEvents(ArrayList<Event> alarmsEvents) {
        this.alarmsEvents = alarmsEvents;
    }

    public Boolean getFunctionSucceed() {
        return functionSucceed;
    }

    public void setFunctionSucceed(Boolean functionSucceed) {
        this.functionSucceed = functionSucceed;
    }

    public ArrayList<Reject> getRejects() {
        if (rejects == null){return new ArrayList<>(); }
        return rejects;
    }

    public void setRejects(ArrayList<Reject> rejects) {
        this.rejects = rejects;
    }

    public ArrayList<Inventory> getInventory() {
        if (inventory == null){return new ArrayList<>(); }
        return inventory;
    }

    public void setInventory(ArrayList<Inventory> inventory) {
        this.inventory = inventory;
    }

    public ArrayList<Notification> getNotification() {
        if (notification == null){return new ArrayList<>(); }
        return notification;
    }

    public void setNotification(ArrayList<Notification> notification) {
        this.notification = notification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.functionSucceed);
        dest.writeTypedList(this.rejects);
        dest.writeTypedList(this.inventory);
        dest.writeTypedList(this.notification);
        dest.writeTypedList(this.alarmsEvents);
    }

    public ActualBarExtraResponse() {
    }

    protected ActualBarExtraResponse(Parcel in) {
        this.functionSucceed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.rejects = in.createTypedArrayList(Reject.CREATOR);
        this.inventory = in.createTypedArrayList(Inventory.CREATOR);
        this.notification = in.createTypedArrayList(Notification.CREATOR);
        this.alarmsEvents = in.createTypedArrayList(Event.CREATOR);
    }

    public static final Parcelable.Creator<ActualBarExtraResponse> CREATOR = new Parcelable.Creator<ActualBarExtraResponse>() {
        @Override
        public ActualBarExtraResponse createFromParcel(Parcel source) {
            return new ActualBarExtraResponse(source);
        }

        @Override
        public ActualBarExtraResponse[] newArray(int size) {
            return new ActualBarExtraResponse[size];
        }
    };
}
