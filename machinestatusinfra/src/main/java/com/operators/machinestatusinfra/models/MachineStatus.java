package com.operators.machinestatusinfra.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MachineStatus {
    private final Boolean mAutoActivateNextJob;
    private final Integer mAutoActivateNextJobTimerSec;
    private final Boolean mAutoActivateNextJobTimer;
    private final Long mNextJobID;
    private final String mNextERPJobID;
    @SerializedName("DepartmentMachinePC")
    private List<Object> mDepartmentMachinePC;
    @SerializedName("DepartmentOeePee")
    private List<Object> mDepartmentOeePee;
    @SerializedName("MissingMachineIds")
    private Object mMissingMachineIds;
    @SerializedName("allMachinesData")
    private List<AllMachinesData> mAllMachinesData;

    public MachineStatus(List<Object> departmentMachinePC, List<Object> departmentOeePee, Object missingMachineIds, List<AllMachinesData> allMachinesData,
                         Boolean autoActivateNextJob, Integer autoActivateNextJobTimerSec, Boolean autoActivateNextJobTimer, Long nextJobID, String nextERPJobID) {
        mDepartmentMachinePC = departmentMachinePC;
        mDepartmentOeePee = departmentOeePee;
        mMissingMachineIds = missingMachineIds;
        mAllMachinesData = allMachinesData;
        mAutoActivateNextJob = autoActivateNextJob;
        mAutoActivateNextJobTimerSec = autoActivateNextJobTimerSec;
        mAutoActivateNextJobTimer = autoActivateNextJobTimer;
        mNextJobID = nextJobID;
        mNextERPJobID = nextERPJobID;
    }

    public List<AllMachinesData> getAllMachinesData() {
        return mAllMachinesData;
    }

    public enum MachineServerStatus {
        NO_JOB(0), WORKING_OK(1), PARAMETER_DEVIATION(2), STOPPED(3), COMMUNICATION_FAILURE(4), SETUP_WORKING(5), SETUP_STOPPED(6), SETUP_COMMUNICATION_FAILURE(7), STOP_IDLE(8);

        private int mId;

        MachineServerStatus(int id) {
            mId = id;
        }

        public int getId() {
            return mId;
        }
    }

    public List<Object> getmDepartmentMachinePC() {
        return mDepartmentMachinePC;
    }

    public void setmDepartmentMachinePC(List<Object> mDepartmentMachinePC) {
        this.mDepartmentMachinePC = mDepartmentMachinePC;
    }

    public List<Object> getmDepartmentOeePee() {
        return mDepartmentOeePee;
    }

    public void setmDepartmentOeePee(List<Object> mDepartmentOeePee) {
        this.mDepartmentOeePee = mDepartmentOeePee;
    }

    public Object getmMissingMachineIds() {
        return mMissingMachineIds;
    }

    public void setmMissingMachineIds(Object mMissingMachineIds) {
        this.mMissingMachineIds = mMissingMachineIds;
    }

    public List<AllMachinesData> getmAllMachinesData() {
        return mAllMachinesData;
    }

    public void setmAllMachinesData(List<AllMachinesData> mAllMachinesData) {
        this.mAllMachinesData = mAllMachinesData;
    }

    public Boolean getmAutoActivateNextJob() {
        if (mAutoActivateNextJob != null) {
            return mAutoActivateNextJob;
        }else {
            return false;
        }
    }

    public Integer getmAutoActivateNextJobTimerSec() {
        if (mAutoActivateNextJobTimerSec != null) {
            return mAutoActivateNextJobTimerSec;
        }else {
            return 0;
        }
    }

    public Boolean getmAutoActivateNextJobTimer() {
        if (mAutoActivateNextJobTimer != null) {
            return mAutoActivateNextJobTimer;
        }else {
            return false;
        }
    }

    public Long getmNextJobID() {
        if (mNextJobID != null) {
            return mNextJobID;
        }else {
            return 0l;
        }
    }

    public String getmNextERPJobID() {
        return mNextERPJobID;
    }
}
