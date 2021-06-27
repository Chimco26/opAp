package com.example.common.permissions;

import com.google.gson.annotations.SerializedName;

public class UserGroupPermission {


    @SerializedName("ChangeProductionStatus")
    private boolean isChangeProductionStatus;

    @SerializedName("OperatorLogin")
    private boolean isOperatorLogin;

    @SerializedName("QualityTest")
    private boolean isQualityTest;

    @SerializedName("DisplayType")
    private Integer displayType;

    @SerializedName("MainParameterName")
    private String mainParameterName;

    public UserGroupPermission() {
        this.isChangeProductionStatus = false;
        this.isOperatorLogin = false;
        this.isQualityTest = false;
    }

    public boolean isChangeProductionStatus() {
        return isChangeProductionStatus;
    }

    public boolean isOperatorLogin() {
        return isOperatorLogin;
    }

    public boolean isQualityTest() {
        return isQualityTest;
    }

    public Integer getDisplayType() { return displayType; }

    public String getMainParameterName() { return mainParameterName; }
}
