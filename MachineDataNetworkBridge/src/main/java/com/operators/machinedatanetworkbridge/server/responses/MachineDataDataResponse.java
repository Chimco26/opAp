package com.operators.machinedatanetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;
import com.operators.machinedatainfra.models.Widget;

import java.util.ArrayList;
import java.util.List;

public class MachineDataDataResponse extends ErrorBaseResponse {

    @SerializedName("MachineID")
    private int mMachineID;

    @SerializedName("MachineParams")
    private ArrayList<Widget> mMachineParams = new ArrayList<>();

    public ArrayList<Widget> getMachineParams() {
        return mMachineParams;
    }
}
