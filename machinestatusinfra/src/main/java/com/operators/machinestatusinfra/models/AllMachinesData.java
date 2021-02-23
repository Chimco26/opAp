package com.operators.machinestatusinfra.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllMachinesData {
    @SerializedName("CurrentJobID")
    private int currentJobID;
    @SerializedName("JobName")
    private String currentJobName;
    @SerializedName("CurrentProductEname")
    private String currentProductEname;
    @SerializedName("CurrentProductID")
    private int currentProductID;
    @SerializedName("CurrentCatalogID")
    private String currentCatalogID;
    @SerializedName("CurrentProductName")
    private String currentProductName;
    @SerializedName("CurrentValue")
    private int currentValue;
    @SerializedName("DepartmentEname")
    private String departmentEname;
    @SerializedName("DepartmentID")
    private int departmentID;
    @SerializedName("DepartmentLname")
    private String departmentLname;
    @SerializedName("FieldEName")
    private Object fieldEName;
    @SerializedName("FieldLName")
    private Object fieldLName;
    @SerializedName("FieldName")
    private Object fieldName;
    @SerializedName("HighLimit")
    private int highLimit;
    @SerializedName("IsMoreMachineToDisplay")
    private boolean isMoreMachineToDisplay;
    @SerializedName("LowLimit")
    private int lowLimit;
    @SerializedName("MachineID")
    private int machineID;
    @SerializedName("MachineLname")
    private String machineLname;
    @SerializedName("MachineName")
    private String machineName;
    @SerializedName("MachineEName")
    private String machineEName;
    @SerializedName("MachineStatusEname")
    private String machineStatusEname;
    @SerializedName("MachineStatusID")
    private int machineStatusID;
    @SerializedName("MachineStatusLName")
    private String machineStatusLName;
    @SerializedName("MachineStatusName")
    private String machineStatusName;
    @SerializedName("NoProgressCount")
    private int noProgressCount;
    @SerializedName("PConfigName")
    private String pConfigName;
    @SerializedName("Row_Counter")
    private int rowCounter;
    @SerializedName("ShiftID")
    private int shiftID;
    @SerializedName("ShiftName")
    private String shiftIDString;
    @SerializedName("shiftEndingIn")
    private int shiftEndingIn;
    @SerializedName("SetupEnd")
    private boolean setupEnd;
    @SerializedName("DisplayRejectFactor")
    private boolean displayRejectFactor;
    @SerializedName("AddRejectsOnSetupEnd")
    private boolean addRejectsOnSetupEnd;
    @SerializedName("minEventDuration")
    private int minEventDuration;
    @SerializedName("WorkerName")
    private String operatorName;
    @SerializedName("WorkerID")
    private String OperatorId;
    @SerializedName("UserID")
    private int userId;
    @SerializedName("ProductionModeID")
    private int mProductionModeID;
    @SerializedName("CustomerIsActivateJobs")
    private boolean mCustomerIsActivateJobs;
    @SerializedName("ProductionModeWarning")
    private boolean mProductionModeWarning;
    @SerializedName("CurrentStatusTimeMin")
    private int mCurrentStatusTimeMin;
    @SerializedName("RejectMesuaring")
    private String mRejectMesuaring;
    @SerializedName("LastJobID")
    private String lastJobId;
    @SerializedName("LastERPJobID")
    private String LastErpJobId;
    @SerializedName("LastProductName")
    private String lastProductName;
    @SerializedName("AllowProductionModeOnOpApp")
    private boolean AllowProductionModeOnOpApp = true;
    @SerializedName("AsUnReportedEvents")
    private boolean asUnReportedEvents;
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
    @SerializedName("UnitsInCycleType")
    private int mUnitsInCycleType;
    @SerializedName("LineID")
    private int mLineID;
    @SerializedName("ReportRejectDefaultUnits")
    private int mReportRejectDefaultUnits;
    @SerializedName("WorkerSignInOnShiftChange")
    private boolean workerSigninOnShiftChange;
    @SerializedName("ManageServiceCallInOpApp")
    private boolean isManageServiceCallForTechnician;
    @SerializedName("AllowReportingOnSetupEvents")
    private boolean isAllowReportingOnSetupEvents;
    @SerializedName("AllowReportingSetupAfterSetupEnd")
    private boolean mAllowReportingSetupAfterSetupEnd;
    @SerializedName("RequireWorkerSignIn")
    private boolean mRequireWorkerSignIn;

    public boolean isRequireWorkerSignIn() {
        return mRequireWorkerSignIn;
    }

    public boolean isAllowReportingOnSetupEvents() {
        return isAllowReportingOnSetupEvents;
    }

    public boolean ismAllowReportingSetupAfterSetupEnd() {
        return mAllowReportingSetupAfterSetupEnd;
    }

    public boolean isManageServiceCallForTechnician() {
        return isManageServiceCallForTechnician;
    }

    public boolean isWorkerSigninOnShiftChange() {
        return workerSigninOnShiftChange;
    }

    public int getUserId() {
        return userId;
    }

    public int getReportRejectDefaultUnits() {
        return mReportRejectDefaultUnits;
    }

    public void setReportRejectDefaultUnits(int mReportRejectDefaultUnits) {
        this.mReportRejectDefaultUnits = mReportRejectDefaultUnits;
    }

    public int getLineID() {
        return mLineID;
    }

    public void setLineID(int mLineID) {
        this.mLineID = mLineID;
    }

    public int getUnitsInCycleType() {
        return mUnitsInCycleType;
    }

    public void setUnitsInCycleType(int mUnitsInCycleType) {
        this.mUnitsInCycleType = mUnitsInCycleType;
    }

    public Boolean getmAutoActivateNextJob() {
        if (mAutoActivateNextJob != null) {
            return mAutoActivateNextJob;
        } else {
            return false;
        }
    }

    public String getCurrentCatalogID() {
        return currentCatalogID;
    }

    public Integer getmAutoActivateNextJobTimerSec() {
        if (mAutoActivateNextJobTimerSec != null) {
            return mAutoActivateNextJobTimerSec;
        } else {
            return 0;
        }
    }

    public Boolean getmAutoActivateNextJobTimer() {
        if (mAutoActivateNextJobTimer != null) {
            return mAutoActivateNextJobTimer;
        } else {
            return false;
        }
    }

    public Long getmNextJobID() {
        if (mNextJobID != null) {
            return mNextJobID;
        } else {
            return 0l;
        }
    }

    public String getmNextERPJobID() {
        return mNextERPJobID;
    }

    public boolean isAllowProductionModeOnOpApp() {
        return AllowProductionModeOnOpApp;
    }

    public String getLastJobId() {
        return lastJobId;
    }

    // TODO: 29/07/2018 worker sign in name + number


    public boolean isAsUnReportedEvents() {
        return asUnReportedEvents;
    }

    public void setAsUnReportedEvents(boolean asUnReportedEvents) {
        this.asUnReportedEvents = asUnReportedEvents;
    }

    public String getLastErpJobId() {
        return LastErpJobId;
    }

    public String getLastProductName() {
        return lastProductName;
    }

    public String getRejectMesuaring() {
        return mRejectMesuaring;
    }

    public int getCurrentStatusTimeMin() {
        return mCurrentStatusTimeMin;
    }

    public boolean ismCustomerIsActivateJobs() {
        return mCustomerIsActivateJobs;
    }

    public boolean isProductionModeWarning() {
        return mProductionModeWarning;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getOperatorId() {
        return OperatorId;
    }

    public boolean isAddRejectsOnSetupEnd() {
        return addRejectsOnSetupEnd;
    }

    public boolean isDisplayRejectFactor() {
        return displayRejectFactor;
    }

    public void setCurrentJobName(String currentJobName) {
        this.currentJobName = currentJobName;
    }

    public int getCurrentJobID() {
        return currentJobID;
    }

    public String getCurrentJobName() {
        return currentJobName;
    }

    public String getCurrentProductEname() {
        return currentProductEname;
    }

    public int getCurrentProductID() {
        return currentProductID;
    }

    public String getCurrentProductName() {
        return currentProductName;
    }

    public int getCurrentValue() {
        return currentValue;
    }


    public String getDepartmentEname() {
        return departmentEname;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public String getDepartmentLname() {
        return departmentLname;
    }


    public Object getFieldEName() {
        return fieldEName;
    }

    public Object getFieldLName() {
        return fieldLName;
    }

    public Object getFieldName() {
        return fieldName;
    }

    public int getHighLimit() {
        return highLimit;
    }

    public boolean isIsMoreMachineToDisplay() {
        return isMoreMachineToDisplay;
    }

    public int getLowLimit() {
        return lowLimit;
    }


    public int getMachineID() {
        return machineID;
    }


    public String getMachineLname() {
        return machineLname;
    }


    public String getMachineName() {
        return machineName;
    }

    public String getMachineStatusLName() {
        return machineStatusLName;
    }

    public String getMachineStatusEname() {
        return machineStatusEname;
    }

    public String getMachineStatusName() {
        return machineStatusName;
    }

    public int getMachineStatusID() {
        return machineStatusID;
    }

    public int getShiftID() {
        return shiftID;
    }

    public int getShiftEndingIn() {
        return shiftEndingIn;
    }

    public boolean canReportApproveFirstItem() {
        return !setupEnd; // when not setup end, we can send this report.
    }

    public boolean isSetupEnd() {
        return setupEnd;
    }

    public String getMachineEName() {
        return machineEName;
    }

    public String getShiftIDString() {
        return shiftIDString;
    }

    public String getConfigName() {
        return pConfigName;
    }

    public void setConfigName(String pConfigName) {
        this.pConfigName = pConfigName;
    }

    public int getMinEventDuration() {
//        return 0;
        return minEventDuration;
    }

    public void setMinEventDuration(int minEventDuration) {
        this.minEventDuration = minEventDuration;
    }

    public int getmProductionModeID() {
        return mProductionModeID;
    }

    public void setmProductionModeID(int mProductionModeID) {
        this.mProductionModeID = mProductionModeID;
    }
}
