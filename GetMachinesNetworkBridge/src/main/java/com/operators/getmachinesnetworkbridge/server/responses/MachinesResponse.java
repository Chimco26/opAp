package com.operators.getmachinesnetworkbridge.server.responses;


import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;
import com.operators.infra.Machine;

import java.util.ArrayList;

public class MachinesResponse extends StandardResponse {
    @SerializedName("machines")
    private ArrayList<Machine> mMachines;
    @SerializedName("headers")
    private String headers;

    public ArrayList<Machine> getMachines() {
        return mMachines;
    }

    public String getHeaders() {
        return headers;
    }
}