package com.operators.jobsinfra;

import com.google.gson.annotations.SerializedName;

public class Job {

   @SerializedName("jobId")
    private int jobId;
    @SerializedName("productName")
    private String productName;
    @SerializedName("ERP")
    private int erp;
    @SerializedName("plannedStart")
    private String plannedStart;
    @SerializedName("numberOfUnits")
    private int numberOfUnits;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getErp() {
        return erp;
    }

    public void setErp(int erp) {
        this.erp = erp;
    }

    public String getPlannedStart() {
        return plannedStart;
    }

    public void setPlannedStart(String plannedStart) {
        this.plannedStart = plannedStart;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }
}
