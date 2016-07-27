package com.operators.getmachinesstatusnetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;
import com.operators.machinestatusinfra.AllMachinesData;
import com.operators.machinestatusinfra.MachineStatus;

import java.util.ArrayList;
import java.util.List;

public class MachineStatusDataResponse extends ErrorBaseResponse {

    @SerializedName("allMachinesData")
    private List<AllMachinesData> mAllMachinesData = new ArrayList<AllMachinesData>();

    public MachineStatus getMachineStatus() {
        return new MachineStatus( mAllMachinesData);
    }
}
