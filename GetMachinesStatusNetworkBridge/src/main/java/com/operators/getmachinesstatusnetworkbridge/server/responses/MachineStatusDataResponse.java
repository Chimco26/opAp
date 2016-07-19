package com.operators.getmachinesstatusnetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;
import com.operators.machinestatusinfra.MachineStatus;

public class MachineStatusDataResponse extends ErrorBaseResponse {
    @SerializedName("machineStatus")
    private MachineStatus mMachineStatus;

    public MachineStatus getMachineStatus() {
        return mMachineStatus;
    }
}
