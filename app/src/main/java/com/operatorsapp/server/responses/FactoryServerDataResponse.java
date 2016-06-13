package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by slava-android on 1/11/2016.
 */
public class FactoryServerDataResponse extends ErrorBaseRespone
{
    @SerializedName("OEE")
    private Float mOEE;
    @SerializedName("PEE")
    private Float mPEE;

    @SerializedName("machineDepartmentStatus")
    private ArrayList<MachineDepartmentStatus> mMachineDepartmentStatus;

    public Float getOEE()
    {
        return mOEE;
    }

    public Float getPEE()
    {
        return mPEE;
    }

    public ArrayList<MachineDepartmentStatus> getMachineDepartmentStatus()
    {
        return mMachineDepartmentStatus;
    }
}
