package com.example.common.department;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentMachine {
    @SerializedName("Key")
    @Expose
    private DepartmentsMachinesKey departmentsMachinesKey;
    @SerializedName("Value")
    @Expose
    private List<DepartmentMachineValue> departmentMachineValue = null;

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

