package com.example.common.department;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MachineLineResponse extends StandardResponse {

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

}
