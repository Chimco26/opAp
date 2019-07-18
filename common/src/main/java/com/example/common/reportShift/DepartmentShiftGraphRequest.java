package com.example.common.reportShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentShiftGraphRequest {

    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("ReqDepartment")
    @Expose
    private List<ReqDepartment> reqDepartment = null;
    @SerializedName("DatePart")
    @Expose
    private Integer datePart;

    public DepartmentShiftGraphRequest(String sessionID, List<ReqDepartment> reqDepartment, Integer datePart) {
        this.sessionID = sessionID;
        this.reqDepartment = reqDepartment;
        this.datePart = datePart;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public List<ReqDepartment> getReqDepartment() {
        return reqDepartment;
    }

    public void setReqDepartment(List<ReqDepartment> reqDepartment) {
        this.reqDepartment = reqDepartment;
    }

    public Integer getDatePart() {
        return datePart;
    }

    public void setDatePart(Integer datePart) {
        this.datePart = datePart;
    }

}