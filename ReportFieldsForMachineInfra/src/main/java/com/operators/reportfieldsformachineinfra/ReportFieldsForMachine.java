package com.operators.reportfieldsformachineinfra;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 02/08/2016.
 */
public class ReportFieldsForMachine {
    @SerializedName("StopReason")
    private List<StopReasons> stopReasons = new ArrayList<StopReasons>();
    @SerializedName("RejectReason")
    private List<RejectReasons> rejectReasons = new ArrayList<RejectReasons>();
    @SerializedName("RejectCauses")
    private List<RejectCauses> rejectCauses = new ArrayList<RejectCauses>();

    public ReportFieldsForMachine(List<StopReasons> stopReasons, List<RejectReasons> rejectReasons, List<RejectCauses> rejectCauses) {
        this.stopReasons = stopReasons;
        this.rejectReasons = rejectReasons;
        this.rejectCauses = rejectCauses;
    }

    public List<StopReasons> getStopReasons() {
        return stopReasons;
    }

    public List<RejectReasons> getRejectReasons() {
        return rejectReasons;
    }

    public List<RejectCauses> getRejectCauses() {
        return rejectCauses;
    }
}
