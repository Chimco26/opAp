package com.example.common.reportShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReqDepartment {
    @SerializedName("DepartmentID")
    @Expose
    private Integer departmentID;
    @SerializedName("MachineIDs")
    @Expose
    private List<Integer> machineIDs = null;
    @SerializedName("ShiftByTime")
    @Expose
    private ShiftByTime shiftByTime;

    public ReqDepartment(Integer departmentID, List<Integer> machineIDs, ShiftByTime shiftByTime) {
        this.departmentID = departmentID;
        this.machineIDs = machineIDs;
        this.shiftByTime = shiftByTime;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public List<Integer> getMachineIDs() {
        return machineIDs;
    }

    public void setMachineIDs(List<Integer> machineIDs) {
        this.machineIDs = machineIDs;
    }

    public ShiftByTime getShiftByTime() {
        return shiftByTime;
    }

    public void setShiftByTime(ShiftByTime shiftByTime) {
        this.shiftByTime = shiftByTime;
    }
}
