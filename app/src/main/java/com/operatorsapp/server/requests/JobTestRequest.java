package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

public class JobTestRequest {

    @SerializedName("JobID")
    private int mJobID;

    @SerializedName("QualityGroupID")
    private int mQualityGroupID;

    @SerializedName("ProductGroupID")
    private int mProductGroupID;

    public JobTestRequest(int jobID, int qualityGroupID, int productGroupID) {
        this.mJobID = mJobID;
        this.mQualityGroupID = mQualityGroupID;
        this.mProductGroupID = mProductGroupID;
    }
}
