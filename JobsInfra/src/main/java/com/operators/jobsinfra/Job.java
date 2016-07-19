package com.operators.jobsinfra;

import com.google.gson.annotations.SerializedName;

public class Job {

   @SerializedName("jobId")
    public int jobId;
    @SerializedName("productName")
    public String productName;
    @SerializedName("erp")
    public int erp;
    @SerializedName("plannedStart")
    public String plannedStart;
    @SerializedName("numberOfUnits")
    public int numberOfUnits;

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
