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
    @SerializedName("PackageTypes")
    private List<PackageTypes> packageTypes = new ArrayList<PackageTypes>();
    @SerializedName("Technicians")
    private List<Technician> technicians = new ArrayList<Technician>();
    @SerializedName("PackageTypes")
    private List<PackageTypes> productionStatus = new ArrayList<PackageTypes>();

    public ReportFieldsForMachine(List<StopReasons> stopReasons, List<RejectReasons> rejectReasons, List<RejectCauses> rejectCauses, List<PackageTypes> packageTypes, List<Technician> technicians, List<PackageTypes> productionStatus) {
        this.stopReasons = stopReasons;
        this.rejectReasons = rejectReasons;
        this.rejectCauses = rejectCauses;
        this.packageTypes = packageTypes;
        this.technicians = technicians;
        this.productionStatus = productionStatus;
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

    public List<PackageTypes> getPackageTypes() {
        return packageTypes;
    }

    public List<Technician> getTechnicians()
    {
        return technicians;
    }

    public List<PackageTypes> getProductionStatus() {return productionStatus; }
}
