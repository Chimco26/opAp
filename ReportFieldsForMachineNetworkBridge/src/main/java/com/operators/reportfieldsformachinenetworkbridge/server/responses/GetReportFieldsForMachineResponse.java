package com.operators.reportfieldsformachinenetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;
import com.operators.reportfieldsformachineinfra.RejectCauses;
import com.operators.reportfieldsformachineinfra.RejectReasons;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.StopReasons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 02/08/2016.
 */
public class GetReportFieldsForMachineResponse extends ErrorBaseResponse {
    @SerializedName("stopReasons")
    private List<StopReasons> stopReasons = new ArrayList<StopReasons>();
    @SerializedName("rejectReasons")
    private List<RejectReasons> rejectReasons = new ArrayList<RejectReasons>();
    @SerializedName("rejectCauses")
    private List<RejectCauses> rejectCauses = new ArrayList<RejectCauses>();

    public ReportFieldsForMachine getReportFieldsForMachine() {
        return new ReportFieldsForMachine(stopReasons, rejectReasons, rejectCauses);
    }
}
