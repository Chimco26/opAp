package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

public class TestOrderMaterialRequest {

    @SerializedName("MaterialID")
    int materialId;

    @SerializedName("QualityGroupID")
    int qualityGroupID;

    @SerializedName("SubType")
    int subType;

    public TestOrderMaterialRequest(int materialId, int qualityGroupID, int subType) {
        this.materialId = materialId;
        this.qualityGroupID = qualityGroupID;
        this.subType = subType;
    }
}
