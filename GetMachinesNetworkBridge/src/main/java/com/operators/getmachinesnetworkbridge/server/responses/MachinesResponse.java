package com.operators.getmachinesnetworkbridge.server.responses;


import com.google.gson.annotations.SerializedName;
import com.operators.infra.Machine;

import java.util.ArrayList;

public class MachinesResponse extends ErrorBaseResponse {
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