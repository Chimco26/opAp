package com.operators.getmachinesstatusnetworkbridge.server.responses;

import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;
import com.operators.machinestatusinfra.models.AllMachinesData;
import com.operators.machinestatusinfra.models.MachineStatus;

import java.util.ArrayList;
import java.util.List;

public class MachineStatusDataResponse extends StandardResponse {

    @SerializedName("DepartmentMachinePC")
    private List<Object> mDepartmentMachinePC;
    @SerializedName("DepartmentOeePee")
    private List<Object> mDepartmentOeePee;
    @SerializedName("allMachinesData")
    private List<AllMachinesData> mAllMachinesData = new ArrayList<>();
    @SerializedName("MissingMachineIds")
    private Object mMissingMachineIds;

    public MachineStatus getMachineStatus() {
        return new MachineStatus(mDepartmentMachinePC, mDepartmentOeePee, mMissingMachineIds, mAllMachinesData);
    }

    public List<Object> getmDepartmentMachinePC() {
        return mDepartmentMachinePC;
    }

    public void setmDepartmentMachinePC(List<Object> mDepartmentMachinePC) {
        this.mDepartmentMachinePC = mDepartmentMachinePC;
    }

    public List<Object> getmDepartmentOeePee() {
        return mDepartmentOeePee;
    }

    public void setmDepartmentOeePee(List<Object> mDepartmentOeePee) {
        this.mDepartmentOeePee = mDepartmentOeePee;
    }

    public List<AllMachinesData> getmAllMachinesData() {
        return mAllMachinesData;
    }

    public void setmAllMachinesData(List<AllMachinesData> mAllMachinesData) {
        this.mAllMachinesData = mAllMachinesData;
    }

    public Object getmMissingMachineIds() {
        return mMissingMachineIds;
    }

    public void setmMissingMachineIds(Object mMissingMachineIds) {
        this.mMissingMachineIds = mMissingMachineIds;
    }

}
