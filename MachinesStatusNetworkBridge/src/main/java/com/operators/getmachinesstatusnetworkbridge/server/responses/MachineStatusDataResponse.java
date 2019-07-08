package com.operators.getmachinesstatusnetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;
import com.operators.machinestatusinfra.models.AllMachinesData;
import com.operators.machinestatusinfra.models.MachineStatus;

import java.util.ArrayList;
import java.util.List;

public class MachineStatusDataResponse extends ErrorBaseResponse {

    @SerializedName("DepartmentMachinePC")
    private List<Object> mDepartmentMachinePC;
    @SerializedName("DepartmentOeePee")
    private List<Object> mDepartmentOeePee;
    @SerializedName("allMachinesData")
    private List<AllMachinesData> mAllMachinesData = new ArrayList<>();
    @SerializedName("MissingMachineIds")
    private Object mMissingMachineIds;
    @SerializedName("AutoActivateNextJob")
    private Boolean mAutoActivateNextJob;
    @SerializedName("AutoActivateNextJobTimerSec")
    private Integer mAutoActivateNextJobTimerSec;
    @SerializedName("AutoActivateNextJobTimer")
    private Boolean mAutoActivateNextJobTimer;
    @SerializedName("NextJobID")
    private Long mNextJobID;
    @SerializedName("NextERPJobID")
    private String mNextERPJobID;

    public MachineStatus getMachineStatus() {
        return new MachineStatus(mDepartmentMachinePC, mDepartmentOeePee, mMissingMachineIds, mAllMachinesData,
                mAutoActivateNextJob, mAutoActivateNextJobTimerSec, mAutoActivateNextJobTimer, mNextJobID, mNextERPJobID);
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

    public List<AllMachinesData> getmAllMachinesData() {
        return mAllMachinesData;
    }

    public void setmAllMachinesData(List<AllMachinesData> mAllMachinesData) {
        this.mAllMachinesData = mAllMachinesData;
    }

    public Object getmMissingMachineIds() {
        return mMissingMachineIds;
    }

    public void setmMissingMachineIds(Object mMissingMachineIds) {
        this.mMissingMachineIds = mMissingMachineIds;
    }

    public Boolean getmAutoActivateNextJob() {
        return mAutoActivateNextJob;
    }

    public void setmAutoActivateNextJob(Boolean mAutoActivateNextJob) {
        this.mAutoActivateNextJob = mAutoActivateNextJob;
    }

    public Integer getmAutoActivateNextJobTimerSec() {
        return mAutoActivateNextJobTimerSec;
    }

    public void setmAutoActivateNextJobTimerSec(Integer mAutoActivateNextJobTimerSec) {
        this.mAutoActivateNextJobTimerSec = mAutoActivateNextJobTimerSec;
    }

    public Boolean getmAutoActivateNextJobTimer() {
        return mAutoActivateNextJobTimer;
    }

    public void setmAutoActivateNextJobTimer(Boolean mAutoActivateNextJobTimer) {
        this.mAutoActivateNextJobTimer = mAutoActivateNextJobTimer;
    }

    public Long getmNextJobID() {
        return mNextJobID;
    }

    public void setmNextJobID(Long mNextJobID) {
        this.mNextJobID = mNextJobID;
    }

    public String getmNextERPJobID() {
        return mNextERPJobID;
    }

    public void setmNextERPJobID(String mNextERPJobID) {
        this.mNextERPJobID = mNextERPJobID;
    }
}
