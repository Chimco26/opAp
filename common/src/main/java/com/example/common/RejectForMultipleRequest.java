package com.example.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RejectForMultipleRequest {
    @SerializedName("MachineID")
    @Expose
    private Integer machineID;
    @SerializedName("OperatorID")
    @Expose
    private String operatorID;
    @SerializedName("RejectReasonID")
    @Expose
    private Integer rejectReasonID;
    @SerializedName("Units")
    @Expose
    private Float units;
    @SerializedName("RejectCauseID")
    @Expose
    private Integer rejectCauseID;
    @SerializedName("Weight")
    @Expose
    private Float weight;
    @SerializedName("JoshID")
    @Expose
    private Integer joshID;

    public RejectForMultipleRequest(Integer machineID, String operatorID, Integer rejectReasonID, Float units, Integer rejectCauseID, Float weight, Integer joshID) {
        this.machineID = machineID;
        this.operatorID = operatorID;
        this.rejectReasonID = rejectReasonID;
        this.units = units;
        this.rejectCauseID = rejectCauseID;
        this.weight = weight;
        this.joshID = joshID;
    }

    public Integer getMachineID() {
        return machineID;
    }

    public void setMachineID(Integer machineID) {
        this.machineID = machineID;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public Integer getRejectReasonID() {
        return rejectReasonID;
    }

    public void setRejectReasonID(Integer rejectReasonID) {
        this.rejectReasonID = rejectReasonID;
    }

    public Float getUnits() {
        return units;
    }

    public void setUnits(Float units) {
        this.units = units;
    }

    public Integer getRejectCauseID() {
        return rejectCauseID;
    }

    public void setRejectCauseID(Integer rejectCauseID) {
        this.rejectCauseID = rejectCauseID;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getJoshID() {
        return joshID;
    }

    public void setJoshID(Integer joshID) {
        this.joshID = joshID;
    }

}