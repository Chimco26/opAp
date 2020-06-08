package com.example.common.department;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachinesLineDetail {

    @SerializedName("CurrentStatusTimeMin")
    @Expose
    private Integer currentStatusTimeMin;
    @SerializedName("JobColor")
    @Expose
    private String jobColor;
    @SerializedName("JobColoredShadow")
    @Expose
    private Boolean jobColoredShadow;
    @SerializedName("MachineID")
    @Expose
    private Integer machineID;
    @SerializedName("MachineName")
    @Expose
    private String machineName;
    @SerializedName("MachineStatusID")
    @Expose
    private Integer machineStatusID;
    @SerializedName("MachineStatusName")
    @Expose
    private String machineStatusName;
    @SerializedName("Row_Counter")
    @Expose
    private Integer rowCounter;
    @SerializedName("MachineStatusColor")
    @Expose
    private String statusColor;
    @SerializedName("RootStop")
    @Expose
    private boolean rootStop;

    public Integer getCurrentStatusTimeMin() {
        return currentStatusTimeMin;
    }

    public void setCurrentStatusTimeMin(Integer currentStatusTimeMin) {
        this.currentStatusTimeMin = currentStatusTimeMin;
    }

    public String getJobColor() {
        return jobColor;
    }

    public void setJobColor(String jobColor) {
        this.jobColor = jobColor;
    }

    public Boolean getJobColoredShadow() {
        return jobColoredShadow;
    }

    public void setJobColoredShadow(Boolean jobColoredShadow) {
        this.jobColoredShadow = jobColoredShadow;
    }

    public Integer getMachineID() {
        return machineID;
    }

    public void setMachineID(Integer machineID) {
        this.machineID = machineID;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Integer getMachineStatusID() {
        return machineStatusID;
    }

    public void setMachineStatusID(Integer machineStatusID) {
        this.machineStatusID = machineStatusID;
    }

    public String getMachineStatusName() {
        return machineStatusName;
    }

    public void setMachineStatusName(String machineStatusName) {
        this.machineStatusName = machineStatusName;
    }

    public Integer getRowCounter() {
        return rowCounter;
    }

    public void setRowCounter(Integer rowCounter) {
        this.rowCounter = rowCounter;
    }

    public String getStatusColor() {
        if (statusColor != null && !statusColor.isEmpty())
            return statusColor;
        else return "#ffffff";
    }

    public boolean isRootStop() {
        return rootStop;
    }
}
