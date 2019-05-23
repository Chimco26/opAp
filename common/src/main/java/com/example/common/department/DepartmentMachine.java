package com.example.common.department;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentMachine implements Cloneable{
    @SerializedName("Key")
    @Expose
    private DepartmentsMachinesKey departmentsMachinesKey;
    @SerializedName("Value")
    @Expose
    private List<DepartmentMachineValue> departmentMachineValue = null;

    public DepartmentMachine(DepartmentsMachinesKey departmentsMachinesKey, List<DepartmentMachineValue> departmentMachineValue) {
        this.departmentsMachinesKey = departmentsMachinesKey;
        this.departmentMachineValue = departmentMachineValue;
    }

    public DepartmentsMachinesKey getDepartmentsMachinesKey() {
        return departmentsMachinesKey;
    }

    public void setDepartmentsMachinesKey(DepartmentsMachinesKey departmentsMachinesKey) {
        this.departmentsMachinesKey = departmentsMachinesKey;
    }

    public List<DepartmentMachineValue> getDepartmentMachineValue() {
        return departmentMachineValue;
    }

    public void setDepartmentMachineValue(List<DepartmentMachineValue> departmentMachineValue) {
        this.departmentMachineValue = departmentMachineValue;
    }

}

