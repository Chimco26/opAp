package com.operators.reportrejectnetworkbridge.server.request;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ReportFixUnitsProducedRequest {
    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("JoshID")
    private long mJobId;

    public ReportFixUnitsProducedRequest(String sessionId, double amount, @Nullable Integer jobId) {
        mSessionId = sessionId;
        this.amount = amount;
        mJobId = jobId;
    }
}
