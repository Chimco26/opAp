package com.example.common.request;

import com.google.gson.annotations.SerializedName;

public class MachineIdRequest {
    @SerializedName("MachineID")
    private String machineId;

    public MachineIdRequest(String machineId) {
        this.machineId = machineId;
    }
}
