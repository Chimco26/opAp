package com.operators.machinestatusinfra;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MachineStatus {
    @SerializedName("allMachinesData")
    private List<AllMachinesData> mAllMachinesData;

    public MachineStatus(List<AllMachinesData> allMachinesData) {
        mAllMachinesData = allMachinesData;
    }

    public List<AllMachinesData> getAllMachinesData() {
        return mAllMachinesData;
    }
}
