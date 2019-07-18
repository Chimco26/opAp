package com.operators.reportrejectnetworkbridge.server.response;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IntervalAndTimeOutResponse extends StandardResponse {

    @SerializedName("PollingIntervalData")
    @Expose
    private List<PollingIntervalDatum> pollingIntervalData = null;

    public List<PollingIntervalDatum> getPollingIntervalData() {
        return pollingIntervalData;
    }

    public void setPollingIntervalData(List<PollingIntervalDatum> pollingIntervalData) {
        this.pollingIntervalData = pollingIntervalData;
    }

}

