package com.operatorsapp.server.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class TechCall24HResponse implements Parcelable{

    @SerializedName("OpenCalls")
    private ArrayList<Notification> openCalls;

    @SerializedName("Calls24Hours")
    private ArrayList<Notification> calls24Hours;

    @SerializedName("TotalOpenCalls")
    private int totalOpenCalls;

    @SerializedName("error")
    private Error error;

    @SerializedName("LeaderRecordID")
    private int leaderRecordID;

    @SerializedName("FunctionSucceed")
    private boolean functionSucceed;

    public ArrayList<Notification> getOpenCalls(){
        return openCalls;
    }

    public int getTotalOpenCalls(){
        return totalOpenCalls;
    }

    public Error getError(){
        return error;
    }

    public int getLeaderRecordID(){
        return leaderRecordID;
    }

    public boolean isFunctionSucceed(){
        return functionSucceed;
    }

    public ArrayList<Notification> getCalls24Hours(){
        return calls24Hours;
    }

    public TechCall24HResponse() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.openCalls);
        dest.writeList(this.calls24Hours);
        dest.writeInt(this.totalOpenCalls);
        dest.writeSerializable(this.error);
        dest.writeInt(this.leaderRecordID);
        dest.writeByte(this.functionSucceed ? (byte) 1 : (byte) 0);
    }

    protected TechCall24HResponse(Parcel in) {
        this.openCalls = new ArrayList<Notification>();
        in.readList(this.openCalls, Notification.class.getClassLoader());
        this.calls24Hours = new ArrayList<Notification>();
        in.readList(this.calls24Hours, Notification.class.getClassLoader());
        this.totalOpenCalls = in.readInt();
        this.error = (Error) in.readSerializable();
        this.leaderRecordID = in.readInt();
        this.functionSucceed = in.readByte() != 0;
    }

    public static final Creator<TechCall24HResponse> CREATOR = new Creator<TechCall24HResponse>() {
        @Override
        public TechCall24HResponse createFromParcel(Parcel source) {
            return new TechCall24HResponse(source);
        }

        @Override
        public TechCall24HResponse[] newArray(int size) {
            return new TechCall24HResponse[size];
        }
    };
}
