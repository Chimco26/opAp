package com.operatorsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RejectRequest {
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
    private Integer units;
    @SerializedName("RejectCauseID")
    @Expose
    private Integer rejectCauseID;
    @SerializedName("Weight")
    @Expose
    private Integer weight;
    @SerializedName("JoshID")
    @Expose
    private Integer joshID;

    public RejectRequest(Integer machineID, String operatorID, Integer rejectReasonID, Integer units, Integer rejectCauseID, Integer weight, Integer joshID) {
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

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Integer getRejectCauseID() {
        return rejectCauseID;
    }

    public void setRejectCauseID(Integer rejectCauseID) {
        this.rejectCauseID = rejectCauseID;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getJoshID() {
        return joshID;
    }

    public void setJoshID(Integer joshID) {
        this.joshID = joshID;
    }

}