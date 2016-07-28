package com.operators.getmachinesstatusnetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;
import com.operators.machinestatusinfra.AllMachinesData;
import com.operators.machinestatusinfra.MachineStatus;

import java.util.ArrayList;
import java.util.List;

public class MachineStatusDataResponse extends ErrorBaseResponse {

    @SerializedName("DepartmentMachinePC")
    private List<Object> mDepartmentMachinePC;
    @SerializedName("DepartmentOeePee")
    private List<Object> mDepartmentOeePee;

    @SerializedName("MissingMachineIds")
    private Object mMissingMachineIds;
    @SerializedName("allMachinesData")
    private List<AllMachinesData> mAllMachinesData = new ArrayList<>();

    public MachineStatus getMachineStatus() {
        return new MachineStatus(  mDepartmentMachinePC,mDepartmentOeePee,mMissingMachineIds,mAllMachinesData);
    }
}
