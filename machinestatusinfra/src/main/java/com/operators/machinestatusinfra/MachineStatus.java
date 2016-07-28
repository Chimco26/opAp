package com.operators.machinestatusinfra;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MachineStatus {
    @SerializedName("DepartmentMachinePC")
    private List<Object> mDepartmentMachinePC;
    @SerializedName("DepartmentOeePee")
    private List<Object> mDepartmentOeePee;
    @SerializedName("MissingMachineIds")
    private Object mMissingMachineIds;
    @SerializedName("allMachinesData")
    private List<AllMachinesData> mAllMachinesData;

    public MachineStatus(List<Object> departmentMachinePC, List<Object> departmentOeePee, Object missingMachineIds, List<AllMachinesData> allMachinesData) {
        mDepartmentMachinePC = departmentMachinePC;
        mDepartmentOeePee = departmentOeePee;
        mMissingMachineIds = missingMachineIds;
        mAllMachinesData = allMachinesData;
    }

    public List<AllMachinesData> getAllMachinesData() {
        return mAllMachinesData;
    }
}
