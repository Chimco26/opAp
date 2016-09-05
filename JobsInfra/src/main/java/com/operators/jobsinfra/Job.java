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

    public String getProductName() {
        return productName;
    }

    public int getErp() {
        return erp;
    }

    public String getPlannedStart() {
        return plannedStart;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

}
