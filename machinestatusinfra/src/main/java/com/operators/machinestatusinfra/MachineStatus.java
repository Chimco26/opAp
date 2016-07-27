package com.operators.machinestatusinfra;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MachineStatus
{
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

        public List<Object> getDepartmentMachinePC() {
                return mDepartmentMachinePC;
        }

        public void setDepartmentMachinePC(List<Object> departmentMachinePC) {
                mDepartmentMachinePC = departmentMachinePC;
        }

        public List<Object> getDepartmentOeePee() {
                return mDepartmentOeePee;
        }

        public void setDepartmentOeePee(List<Object> departmentOeePee) {
                mDepartmentOeePee = departmentOeePee;
        }

        public Object getMissingMachineIds() {
                return mMissingMachineIds;
        }

        public void setMissingMachineIds(Object missingMachineIds) {
                mMissingMachineIds = missingMachineIds;
        }

        public List<AllMachinesData> getAllMachinesData() {
                return mAllMachinesData;
        }

        public void setAllMachinesData(List<AllMachinesData> allMachinesData) {
                mAllMachinesData = allMachinesData;
        }
}
