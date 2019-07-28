package com.example.common.department;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartmentMachineValue implements Cloneable{

    @SerializedName("DisplayOrder")
    @Expose
    private Integer displayOrder;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("MachineName")
    @Expose
    private String machineName;
    @SerializedName("MachineStatus")
    @Expose
    private Integer machineStatus;

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Integer getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(Integer machineStatus) {
        this.machineStatus = machineStatus;
    }

}