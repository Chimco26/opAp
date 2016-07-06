package com.operators.getmachinesnetworkbridge.server.responses;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MachinesResponse extends ErrorBaseResponse {
    @SerializedName("machines")
    private ArrayList<Machine> mMachines;

    public ArrayList<Machine> getMachines() {
        return mMachines;
    }
}