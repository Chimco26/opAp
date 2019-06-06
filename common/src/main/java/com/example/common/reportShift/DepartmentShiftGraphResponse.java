package com.example.common.reportShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentShiftGraphResponse {

    @SerializedName("FunctionSucceed")
    @Expose
    private Boolean functionSucceed;
    @SerializedName("Departments")
    @Expose
    private List<Department> departments = null;
    @SerializedName("GraphColors")
    @Expose
    private List<GraphColor> graphColors = null;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("LeaderRecordID")
    @Expose
    private Integer leaderRecordID;

    public Boolean getFunctionSucceed() {
        return functionSucceed;
    }

    public void setFunctionSucceed(Boolean functionSucceed) {
        this.functionSucceed = functionSucceed;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<GraphColor> getGraphColors() {
        return graphColors;
    }

    public void setGraphColors(List<GraphColor> graphColors) {
        this.graphColors = graphColors;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Integer getLeaderRecordID() {
        return leaderRecordID;
    }

    public void setLeaderRecordID(Integer leaderRecordID) {
        this.leaderRecordID = leaderRecordID;
    }

}
