package com.operators.machinestatusinfra.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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
    private ArrayList<TaskStatusCount> taskCountObject;
    private boolean isAllowReportingOnSetupEvents = false;
    private boolean isAllowReportingSetupAfterSetupEnd = false;

    public MachineStatus(List<Object> departmentMachinePC, List<Object> departmentOeePee, Object missingMachineIds, List<AllMachinesData> allMachinesData) {
        mDepartmentMachinePC = departmentMachinePC;
        mDepartmentOeePee = departmentOeePee;
        mMissingMachineIds = missingMachineIds;
        mAllMachinesData = allMachinesData;
    }

    public List<AllMachinesData> getAllMachinesData() {
        return mAllMachinesData;
    }

    public boolean isAllowReportingOnSetupEvents() {
        return isAllowReportingOnSetupEvents;
    }

    public boolean isAllowReportingSetupAfterSetupEnd() {
        return isAllowReportingSetupAfterSetupEnd;
    }

    public void setAllowReportingOnSetupEvents(boolean allowReportingOnSetupEvents) {
        isAllowReportingOnSetupEvents  = allowReportingOnSetupEvents;
    }

    public void setAllowReportingSetupAfterSetupEnd(boolean ismAllowReportingSetupAfterSetupEnd) {
        isAllowReportingSetupAfterSetupEnd = ismAllowReportingSetupAfterSetupEnd;
    }

    public enum MachineServerStatus {
        NO_JOB(0), WORKING_OK(1), PARAMETER_DEVIATION(2), STOPPED(3), COMMUNICATION_FAILURE(4), SETUP_WORKING(5), SETUP_STOPPED(6), SETUP_COMMUNICATION_FAILURE(7), STOP_IDLE(8), DOWN_TIME(9);

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

    public int getTaskCountTotal() {
        int total = 0;
        if (taskCountObject != null) {
            for (TaskStatusCount taskStatusCount : taskCountObject) {
                if (taskStatusCount.getTaskStatusId() == 2 || taskStatusCount.getTaskStatusId() == 3) {
                    total += taskStatusCount.getNumOfTasks();
                }
            }
        }
        return total;
    }

    public ArrayList<TaskStatusCount> getTaskCountObject() {
        return taskCountObject;
    }

    public void setTaskCountObject(ArrayList<TaskStatusCount> taskCountObject) {
        this.taskCountObject = taskCountObject;
    }
}
