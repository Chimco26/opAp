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


    public String getMachineStatusEname() {
        return machineStatusEname;
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

}
