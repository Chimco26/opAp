package com.operators.reportrejectnetworkbridge.server.request;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 14/08/2016.
 */
public class SendReportInventoryRequest {
    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("MachineID")
    private String mMachineId;
    @SerializedName("OperatorID")
    private String mOperatorId;
    @SerializedName("PackageTypeId")
    private int mPackageTypeId;
    @SerializedName("units")
    private int mUnits;
    @SerializedName("joshID")
    private Integer mJobId;
    @SerializedName("NumOfBatch")
    private Integer numOfBatch = 1;

    public SendReportInventoryRequest(String sessionId, String machineId, String operatorId, int packageTypeId, int units, @Nullable Integer jobId, Integer numOfBatch) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mOperatorId = operatorId;
        mPackageTypeId = packageTypeId;
        mUnits = units;
        mJobId = jobId;
        this.numOfBatch = numOfBatch;
    }
}
