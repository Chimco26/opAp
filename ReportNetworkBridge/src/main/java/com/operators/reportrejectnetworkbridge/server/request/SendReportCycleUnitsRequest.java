package com.operators.reportrejectnetworkbridge.server.request;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 14/08/2016.
 */
public class SendReportCycleUnitsRequest {
    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("MachineID")
    private String mMachineId;
    @SerializedName("OperatorID")
    private String mOperatorId;
    @SerializedName("UnitsPerCycle")
    private double mUnitsPerCycle;
//    @SerializedName("JobId")
//    private Integer mJobId;

    public SendReportCycleUnitsRequest(String sessionId, String machineId, String operatorId, double unitsPerCycle, @Nullable Integer jobId) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mOperatorId = operatorId;
        mUnitsPerCycle = unitsPerCycle;
//        mJobId = jobId;
    }
}
