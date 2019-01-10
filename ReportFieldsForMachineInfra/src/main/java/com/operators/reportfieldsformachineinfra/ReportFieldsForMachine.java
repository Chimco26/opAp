package com.operators.reportfieldsformachineinfra;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 02/08/2016.
 */
public class ReportFieldsForMachine implements Parcelable {
    public static final String TAG = ReportFieldsForMachine.class.getSimpleName();
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

    public void setRejectReasons(List<RejectReasons> rejectReasons){
        this.rejectReasons = rejectReasons;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.stopReasons);
        dest.writeList(this.rejectReasons);
        dest.writeList(this.rejectCauses);
        dest.writeList(this.packageTypes);
        dest.writeList(this.technicians);
        dest.writeList(this.productionStatus);
    }

    protected ReportFieldsForMachine(Parcel in) {
        this.stopReasons = new ArrayList<StopReasons>();
        in.readList(this.stopReasons, StopReasons.class.getClassLoader());
        this.rejectReasons = new ArrayList<RejectReasons>();
        in.readList(this.rejectReasons, RejectReasons.class.getClassLoader());
        this.rejectCauses = new ArrayList<RejectCauses>();
        in.readList(this.rejectCauses, RejectCauses.class.getClassLoader());
        this.packageTypes = new ArrayList<PackageTypes>();
        in.readList(this.packageTypes, PackageTypes.class.getClassLoader());
        this.technicians = new ArrayList<Technician>();
        in.readList(this.technicians, Technician.class.getClassLoader());
        this.productionStatus = new ArrayList<PackageTypes>();
        in.readList(this.productionStatus, PackageTypes.class.getClassLoader());
    }

    public static final Parcelable.Creator<ReportFieldsForMachine> CREATOR = new Parcelable.Creator<ReportFieldsForMachine>() {
        @Override
        public ReportFieldsForMachine createFromParcel(Parcel source) {
            return new ReportFieldsForMachine(source);
        }

        @Override
        public ReportFieldsForMachine[] newArray(int size) {
            return new ReportFieldsForMachine[size];
        }
    };
}
