package com.operators.getmachinesnetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;

public class Machine {
    @SerializedName("machineId")
    private String mMachineId;

    @SerializedName("machineName")
    private String mMachineName;

    public String getMachineId() {
        return mMachineId;
    }

    public String getMachineName() {
        return mMachineName;
    }
}
