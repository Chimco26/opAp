package com.operators.machinedatanetworkbridge.server.responses;

import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;
import com.operators.machinedatainfra.models.Widget;

import java.util.ArrayList;

public class MachineDataDataResponse extends StandardResponse {

    @SerializedName("MachineID")
    private int mMachineID;

    @SerializedName("MachineParams")
    private ArrayList<Widget> mMachineParams = new ArrayList<>();

    public ArrayList<Widget> getMachineParams() {
        return mMachineParams;
    }
}
