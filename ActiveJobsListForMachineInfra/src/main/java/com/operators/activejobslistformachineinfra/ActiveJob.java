package com.operators.activejobslistformachineinfra;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 14/08/2016.
 */
public class ActiveJob {

    @SerializedName("CavitiesStandard")
    private Integer cavitiesStandard;
    @SerializedName("Department")
    private Integer department;
    @SerializedName("JobID")
    private Integer jobID;
    @SerializedName("MachineID")
    private Integer machineID;
    @SerializedName("ShiftID")
    private Integer shiftID;
    @SerializedName("joshID")
    private Integer joshID;
    @SerializedName("joshName")
    private String joshName;

    public Integer getCavitiesStandard()
    {
        return cavitiesStandard;
    }

    public Integer getDepartment()
    {
        return department;
    }

    public Integer getJobID()
    {
        return jobID;
    }

    public Integer getMachineID()
    {
        return machineID;
    }

    public Integer getShiftID()
    {
        return shiftID;
    }

    public Integer getJoshID() {
        return joshID;
    }

    public String getJoshName() {
        return joshName;
    }
}
