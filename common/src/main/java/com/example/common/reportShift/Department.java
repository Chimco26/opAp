package com.example.common.reportShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Department {

    @SerializedName("DepartmentGraphParameters")
    @Expose
    private List<String> departmentGraphParameters = null;
    @SerializedName("CurrentShift")
    @Expose
    private List<CurrentShift> currentShift = null;
    @SerializedName("ReferanceShift")
    @Expose
    private List<Object> referanceShift = null;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("DisplayOrder")
    @Expose
    private Integer displayOrder;

    public List<String> getDepartmentGraphParameters() {
        return departmentGraphParameters;
    }

    public void setDepartmentGraphParameters(List<String> departmentGraphParameters) {
        this.departmentGraphParameters = departmentGraphParameters;
    }

    public List<CurrentShift> getCurrentShift() {
        return currentShift;
    }

    public void setCurrentShift(List<CurrentShift> currentShift) {
        this.currentShift = currentShift;
    }

    public List<Object> getReferanceShift() {
        return referanceShift;
    }

    public void setReferanceShift(List<Object> referanceShift) {
        this.referanceShift = referanceShift;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

}
