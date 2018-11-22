package com.operators.reportrejectnetworkbridge.server.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IntervalAndTimeOutResponse {
    @SerializedName("FunctionSucceed")
    @Expose
    private Boolean functionSucceed;
    @SerializedName("LeaderRecordID")
    @Expose
    private Integer leaderRecordID;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("PollingIntervalData")
    @Expose
    private List<PollingIntervalDatum> pollingIntervalData = null;

    public Boolean getFunctionSucceed() {
        return functionSucceed;
    }

    public void setFunctionSucceed(Boolean functionSucceed) {
        this.functionSucceed = functionSucceed;
    }

    public Integer getLeaderRecordID() {
        return leaderRecordID;
    }

    public void setLeaderRecordID(Integer leaderRecordID) {
        this.leaderRecordID = leaderRecordID;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public List<PollingIntervalDatum> getPollingIntervalData() {
        return pollingIntervalData;
    }

    public void setPollingIntervalData(List<PollingIntervalDatum> pollingIntervalData) {
        this.pollingIntervalData = pollingIntervalData;
    }

}

