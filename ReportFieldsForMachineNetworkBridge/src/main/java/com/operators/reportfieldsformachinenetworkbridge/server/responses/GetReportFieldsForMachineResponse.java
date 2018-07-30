package com.operators.reportfieldsformachinenetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;
import com.operators.reportfieldsformachineinfra.PackageTypes;
import com.operators.reportfieldsformachineinfra.RejectCauses;
import com.operators.reportfieldsformachineinfra.RejectReasons;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.StopReasons;
import com.operators.reportfieldsformachineinfra.Technician;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 02/08/2016.
 */
public class GetReportFieldsForMachineResponse extends ErrorBaseResponse {
    @SerializedName("StopReason")
    private List<StopReasons> stopReasons = new ArrayList<StopReasons>();
    @SerializedName("RejectReason")
    private List<RejectReasons> rejectReasons = new ArrayList<RejectReasons>();
    @SerializedName("RejectCauses")
    private List<RejectCauses> rejectCauses = new ArrayList<RejectCauses>();
    @SerializedName("PackageTypes")
    private List<PackageTypes> packageTypes = new ArrayList<PackageTypes>();
    @SerializedName("Technicians")
    private List<Technician> technicians = new ArrayList<Technician>();
    @SerializedName("ProductionStatus")
    private List<PackageTypes> productionStatus = new ArrayList<PackageTypes>();



    public ReportFieldsForMachine getReportFieldsForMachine() {
        return new ReportFieldsForMachine(stopReasons, rejectReasons, rejectCauses,packageTypes, technicians, productionStatus);
    }


}
