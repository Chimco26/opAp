package com.operators.activejobslistformachineinfra;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 14/08/2016.
 */
public class ActiveJob {

    @SerializedName("CavitiesActual")
    private Integer cavitiesActual;
    @SerializedName("CavitiesStandard")
    private Integer cavitiesStandard;
    @SerializedName("Department")
    private Integer department;
    @SerializedName("JobID")
    private Integer jobID;
    @SerializedName("MachineID")
    private Integer machineID;
    @SerializedName("ShiftID")
    private Integer shiftID;
    @SerializedName("joshID")
    private Integer joshID;
    @SerializedName("joshName")
    private String joshName;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("ProductCatalogID")
    private String productCatalogId;
    @SerializedName("JobName")
    private String jobName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setCavitiesActual(Integer cavitiesActual) {
        this.cavitiesActual = cavitiesActual;
    }

    public void setCavitiesStandard(Integer cavitiesStandard) {
        this.cavitiesStandard = cavitiesStandard;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public void setMachineID(Integer machineID) {
        this.machineID = machineID;
    }

    public void setShiftID(Integer shiftID) {
        this.shiftID = shiftID;
    }

    public void setJoshID(Integer joshID) {
        this.joshID = joshID;
    }

    public void setJoshName(String joshName) {
        this.joshName = joshName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(String productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public Integer getCavitiesStandard()
    {
        return cavitiesStandard;
    }

    public Integer getCavitiesActual()
    {
        return cavitiesActual;
    }

    public Integer getDepartment()
    {
        return department;
    }

    public Integer getJobID()
    {
        return jobID;
    }

    public Integer getMachineID()
    {
        return machineID;
    }

    public Integer getShiftID()
    {
        return shiftID;
    }

    public Integer getJoshID() {
        return joshID;
    }

    public String getJoshName() {
        return joshName;
    }
}
