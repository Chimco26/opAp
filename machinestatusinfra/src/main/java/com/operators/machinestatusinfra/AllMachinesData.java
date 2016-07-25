package com.operators.machinestatusinfra;


import com.google.gson.annotations.SerializedName;

public class AllMachinesData {
    @SerializedName("CurrentJobID")
    private int currentJobID;
    @SerializedName("CurrentProductEname")
    private String currentProductEname;
    @SerializedName("CurrentProductID")
    private int currentProductID;
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
    @SerializedName("MachineStatusEname")
    private String machineStatusEname;
    @SerializedName("MachineStatusID")
    private int machineStatusID;
    @SerializedName("MachineStatusName")
    private String machineStatusName;
    @SerializedName("NoProgressCount")
    private int noProgressCount;
    @SerializedName("Row_Counter")
    private int rowCounter;
    @SerializedName("ShiftID")
    private int shiftID;
    @SerializedName("shiftEndingIn")
    private int shiftEndingIn;

    public int getCurrentJobID() {
        return currentJobID;
    }


    public void setCurrentJobID(int currentJobID) {
        this.currentJobID = currentJobID;
    }

    public String getCurrentProductEname() {
        return currentProductEname;
    }

    public void setCurrentProductEname(String currentProductEname) {
        this.currentProductEname = currentProductEname;
    }

    public int getCurrentProductID() {
        return currentProductID;
    }


    public void setCurrentProductID(int currentProductID) {
        this.currentProductID = currentProductID;
    }

    public String getCurrentProductName() {
        return currentProductName;
    }

    public void setCurrentProductName(String currentProductName) {
        this.currentProductName = currentProductName;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public String getDepartmentEname() {
        return departmentEname;
    }

    public void setDepartmentEname(String departmentEname) {
        this.departmentEname = departmentEname;
    }

    public int getDepartmentID() {
        return departmentID;
    }


    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentLname() {
        return departmentLname;
    }

    public void setDepartmentLname(String departmentLname) {
        this.departmentLname = departmentLname;
    }


    public Object getFieldEName() {
        return fieldEName;
    }

    public void setFieldEName(Object fieldEName) {
        this.fieldEName = fieldEName;
    }

    public Object getFieldLName() {
        return fieldLName;
    }

    public void setFieldLName(Object fieldLName) {
        this.fieldLName = fieldLName;
    }

    public Object getFieldName() {
        return fieldName;
    }

    public void setFieldName(Object fieldName) {
        this.fieldName = fieldName;
    }
    public int getHighLimit() {
        return highLimit;
    }

    public void setHighLimit(int highLimit) {
        this.highLimit = highLimit;
    }

    public boolean isIsMoreMachineToDisplay() {
        return isMoreMachineToDisplay;
    }

    public void setIsMoreMachineToDisplay(boolean isMoreMachineToDisplay) {
        this.isMoreMachineToDisplay = isMoreMachineToDisplay;
    }

    public int getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(int lowLimit) {
        this.lowLimit = lowLimit;
    }

    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    public String getMachineLname() {
        return machineLname;
    }

    public void setMachineLname(String machineLname) {
        this.machineLname = machineLname;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineStatusEname() {
        return machineStatusEname;
    }

    public void setMachineStatusEname(String machineStatusEname) {
        this.machineStatusEname = machineStatusEname;
    }

    public int getMachineStatusID() {
        return machineStatusID;
    }

    public void setMachineStatusID(int machineStatusID) {
        this.machineStatusID = machineStatusID;
    }

    public String getMachineStatusName() {
        return machineStatusName;
    }

    public void setMachineStatusName(String machineStatusName) {
        this.machineStatusName = machineStatusName;
    }

    public int getNoProgressCount() {
        return noProgressCount;
    }

    public void setNoProgressCount(int noProgressCount) {
        this.noProgressCount = noProgressCount;
    }

    public int getRowCounter() {
        return rowCounter;
    }

    public void setRowCounter(int rowCounter) {
        this.rowCounter = rowCounter;
    }

    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }


    public int getShiftEndingIn() {
        return shiftEndingIn;
    }

    public void setShiftEndingIn(int shiftEndingIn) {
        this.shiftEndingIn = shiftEndingIn;
    }
}
