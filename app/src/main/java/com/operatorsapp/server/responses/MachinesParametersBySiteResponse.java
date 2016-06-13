package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Omri Bager on 2/16/2016.
 * Falcore LTD
 */

public class MachinesParametersBySiteResponse
{
    @SerializedName("MissingMachineIds")
    ArrayList<Integer> mMissingMachineIds;

    @SerializedName("Machines")
    ArrayList<MachineResponseModel> mCriticalMachines;

    public ArrayList<Integer> getMissingMachineIds()
    {
        return mMissingMachineIds;
    }

    public ArrayList<MachineResponseModel> getCriticalMachines()
    {
        return mCriticalMachines;
    }
}
