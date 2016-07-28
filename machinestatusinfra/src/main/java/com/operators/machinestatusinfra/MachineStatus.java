package com.operators.machinestatusinfra;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MachineStatus {
    @SerializedName("DepartmentMachinePC")
    private List<Object> mDepartmentMachinePC;
    @SerializedName("DepartmentOeePee")
    private List<Object> mDepartmentOeePee;
    @SerializedName("MissingMachineIds")
    private Object mMissingMachineIds;
    @SerializedName("allMachinesData")
    private List<AllMachinesData> mAllMachinesData;

    public MachineStatus(List<Object> departmentMachinePC, List<Object> departmentOeePee, Object missingMachineIds, List<AllMachinesData> allMachinesData) {
        mDepartmentMachinePC = departmentMachinePC;
        mDepartmentOeePee = departmentOeePee;
        mMissingMachineIds = missingMachineIds;
        mAllMachinesData = allMachinesData;
    }

    public List<AllMachinesData> getAllMachinesData() {
        return mAllMachinesData;
    }

    public enum MachineServerStatus
    {
        NO_JOB(0), WORKING_OK(1), PARAMETER_DEVIATION(2), STOPPED(3), COMMUNICATION_FAILURE(4), SETUP_WORKING(5), SETUP_STOPPED(6), SETUP_COMMUNICATION_FAILURE(7);

        private int mId;

        MachineServerStatus(int id)
        {
            mId = id;
        }

        public int getId()
        {
            return mId;
        }
    }

}
