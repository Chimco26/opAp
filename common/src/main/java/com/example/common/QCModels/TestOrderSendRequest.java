package com.example.common.QCModels;

import com.google.gson.annotations.SerializedName;

public class TestOrderSendRequest {
    @SerializedName("JobID")
    private long jobID;
    @SerializedName("JoshID")
    private long joshID;
    @SerializedName("ProductID")
    private long productID;
    @SerializedName("SubType")
    private long subType;
    @SerializedName("Samples")
    private int samples;

    public TestOrderSendRequest(long jobID, long joshID, long productID, long subType, int samples) {
        this.jobID = jobID;
        this.joshID = joshID;
        this.productID = productID;
        this.subType = subType;
        this.samples = samples;
    }

    public TestOrderSendRequest(long jobID, long joshID, long productID, long subType) {
        this.jobID = jobID;
        this.joshID = joshID;
        this.productID = productID;
        this.subType = subType;

    }
    public long getJobID() {
        return jobID;
    }

    public void setJobID(long jobID) {
        this.jobID = jobID;
    }

    public long getJoshID() {
        return joshID;
    }

    public void setJoshID(long joshID) {
        this.joshID = joshID;
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public long getSubType() {
        return subType;
    }

    public void setSubType(long subType) {
        this.subType = subType;
    }

    public int getSamples() {
        return samples;
    }

    public void setSamples(int samples) {
        this.samples = samples;
    }
}

