package com.example.common.QCModels;

import com.google.gson.annotations.SerializedName;

public class TestOrderRequest {

    @SerializedName("JobID")
    private long jobID;
    @SerializedName("JoshID")
    private long joshID;
    @SerializedName("QualityGroupID")
    private long qualityGroupID;
    @SerializedName("ProductGroupID")
    private long productGroupID;
    @SerializedName("ProductID")
    private long productID;
    @SerializedName("SubType")
    private long subType = -1;

    public TestOrderRequest(long jobID) {
        this.jobID = jobID;
    }

    public TestOrderRequest(long jobID, long joshID, long qualityGroupID, long productGroupID, long productID, long subType) {
        this.jobID = jobID;
        this.joshID = joshID;
        this.qualityGroupID = qualityGroupID;
        this.productGroupID = productGroupID;
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

    public long getQualityGroupID() {
        return qualityGroupID;
    }

    public void setQualityGroupID(long qualityGroupID) {
        this.qualityGroupID = qualityGroupID;
    }

    public long getProductGroupID() {
        return productGroupID;
    }

    public void setProductGroupID(long productGroupID) {
        this.productGroupID = productGroupID;
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
}
