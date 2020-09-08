package com.example.common.department;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MachineLineResponse extends StandardResponse implements Parcelable {

    @SerializedName("LineID")
    @Expose
    private Integer lineID;
    @SerializedName("LineName")
    @Expose
    private String lineName;
    @SerializedName("MachinesData")
    @Expose
    private List<MachinesLineDetail> machinesData = null;

    public Integer getLineID() {
        return lineID;
    }

    public void setLineID(Integer lineID) {
        this.lineID = lineID;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public List<MachinesLineDetail> getMachinesData() {
        return machinesData;
    }

    public void setMachinesData(List<MachinesLineDetail> machinesData) {
        this.machinesData = machinesData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.lineID);
        dest.writeString(this.lineName);
        dest.writeList(this.machinesData);
    }

    public MachineLineResponse() {
    }

    protected MachineLineResponse(Parcel in) {
        super(in);
        this.lineID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lineName = in.readString();
        this.machinesData = new ArrayList<MachinesLineDetail>();
        in.readList(this.machinesData, MachinesLineDetail.class.getClassLoader());
    }

    public static final Creator<MachineLineResponse> CREATOR = new Creator<MachineLineResponse>() {
        @Override
        public MachineLineResponse createFromParcel(Parcel source) {
            return new MachineLineResponse(source);
        }

        @Override
        public MachineLineResponse[] newArray(int size) {
            return new MachineLineResponse[size];
        }
    };
}
