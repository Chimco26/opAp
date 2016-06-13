package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by slava-android on 1/11/2016.
 */
public class MachineDepartmentStatus
{
    @SerializedName("MachineCount")
    private int mMachineCount;
    @SerializedName("MachinePC")
    private float mMachinePC;
    @SerializedName("MachineStatus")
    private String mMachineStatus;
    @SerializedName("MachineStatusID")
    private int mMachineStatusId;
    @SerializedName("TotalMachines")
    private int mTotalMachines;


    public int getMachineCount()
    {
        return mMachineCount;
    }

    public float getMachinePC()
    {
        return mMachinePC;
    }

    public String getMachineStatus()
    {
        return mMachineStatus;
    }

    public int getMachineStatusId()
    {
        return mMachineStatusId;
    }

    public int getTotalMachines()
    {
        return mTotalMachines;
    }
}
