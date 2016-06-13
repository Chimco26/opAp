package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Omri Bager on 2/15/2016.
 * Falcore LTD
 */
public class MachineResponseModel
{
    @SerializedName("MachineID")
    private Integer mMchineId;

    @SerializedName("MachineLname")
    private String mMachineLnamee;

    @SerializedName("MachineName")
    private String mMachineName;

    @SerializedName("DepartmentEname")
    private String mDepartmentEname;

    @SerializedName("DepartmentID")
    private Integer mDepartmentID;

    @SerializedName("DepartmentLname")
    private String mDepartmentLname;

    @SerializedName("MachineStatusEname")
    private String mMachineStatusEname;

    @SerializedName("MachineStatusID")
    private Integer mMachineStatusID;

    @SerializedName("MachineStatusName")
    private String mMachineStatusName;

    @SerializedName("Params")
    private ArrayList<MachineResponseModelParams> mMachineParams;

    public Integer getMchineId()
    {
        return mMchineId;
    }

    public String getMachineLnamee()
    {
        return mMachineLnamee;
    }

    public String getMachineName()
    {
        return mMachineName;
    }

    public String getDepartmentEname()
    {
        return mDepartmentEname;
    }

    public Integer getDepartmentID()
    {
        return mDepartmentID;
    }

    public String getDepartmentLname()
    {
        return mDepartmentLname;
    }

    public String getMachineStatusEname()
    {
        return mMachineStatusEname;
    }

    public Integer getMachineStatusID()
    {
        return mMachineStatusID;
    }

    public String getMachineStatusName()
    {
        return mMachineStatusName;
    }

    public ArrayList<MachineResponseModelParams> getMachineParams()
    {
        return mMachineParams;
    }
}
