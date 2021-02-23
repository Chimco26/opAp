package com.example.common.department;

import com.example.common.permissions.UserGroupPermission;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentsMachinesResponse {
    @SerializedName("FunctionSucceed")
    @Expose
    private Boolean functionSucceed;
    @SerializedName("LeaderRecordID")
    @Expose
    private Integer leaderRecordID;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("DepartmentMachine")
    @Expose
    private List<DepartmentMachine> departmentMachine = null;
    @SerializedName("ErrorCode")
    @Expose
    private Object errorCode;
    @SerializedName("ErrorDescription")
    @Expose
    private Object errorDescription;
    @SerializedName("ErrorMessage")
    @Expose
    private Object errorMessage;
    @SerializedName("departments")
    @Expose
    private List<Object> departments = null;
    @SerializedName("UserGroupPermission")
    @Expose
    private UserGroupPermission userGroupPermission;

    public UserGroupPermission getUserGroupPermission() {
        if (userGroupPermission == null){
            userGroupPermission = new UserGroupPermission();
        }
        return userGroupPermission;
    }

    public Boolean getFunctionSucceed() {
        return functionSucceed;
    }

    public void setFunctionSucceed(Boolean functionSucceed) {
        this.functionSucceed = functionSucceed;
    }

    public Integer getLeaderRecordID() {
        return leaderRecordID;
    }

    public void setLeaderRecordID(Integer leaderRecordID) {
        this.leaderRecordID = leaderRecordID;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public List<DepartmentMachine> getDepartmentMachine() {
        return departmentMachine;
    }

    public void setDepartmentMachine(List<DepartmentMachine> departmentMachine) {
        this.departmentMachine = departmentMachine;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public Object getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(Object errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Object> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Object> departments) {
        this.departments = departments;
    }
}
