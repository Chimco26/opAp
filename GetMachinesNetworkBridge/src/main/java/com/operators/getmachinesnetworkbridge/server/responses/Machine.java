package com.operators.getmachinesnetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;

public class Machine {
    @SerializedName("DefaultControllerFieldName")
    private String mDefaultControllerFieldName;
    @SerializedName("Department")
    private int mDepartment;
    @SerializedName("DisplayOrder")
    private int mDisplayOrder;
    @SerializedName("Id")
    private int mId;
    @SerializedName("MachineLName")
    private String mMachineLName;
    @SerializedName("MachineName")
    private String mMachineName;
    @SerializedName("MachineStatus")
    private int mMachineStatus;

    public String getDefaultControllerFieldName() {
        return mDefaultControllerFieldName;
    }

    public int getDepartment() {
        return mDepartment;
    }

    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public int getId() {
        return mId;
    }

    public String getMachineLName() {
        return mMachineLName;
    }

    public String getMachineName() {
        return mMachineName;
    }

    public int getMachineStatus() {
        return mMachineStatus;
    }
}
