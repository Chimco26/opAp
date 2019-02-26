package com.operators.activejobslistformachineinfra;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 14/08/2016.
 */
public class ActiveJob implements Parcelable {

    @SerializedName("CavitiesActual")
    private Double cavitiesActual;
    @SerializedName("CavitiesStandard")
    private Integer cavitiesStandard;
    @SerializedName("Department")
    private Integer department;
    @SerializedName("JobID")
    private Integer jobID;
    @SerializedName("MachineID")
    private Integer machineID;
    @SerializedName("ShiftID")
    private Integer shiftID;
    @SerializedName("joshID")
    private Integer joshID;
    @SerializedName("joshName")
    private String joshName;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("ProductCatalogID")
    private String productCatalogId;
    @SerializedName("JobName")
    private String jobName;
    @SerializedName("JobUnitsProducedOK")
    private String jobUnitsProducedOK;
    @SerializedName("JoshUnitsProducedOK")
    private String joshUnitsProducedOK;
    private boolean isUnit = true;
    private float reportValue;
    private boolean isEdited;

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public boolean isUnit() {
        return isUnit;
    }

    public void setUnit(boolean unit) {
        isUnit = unit;
    }

    public float getReportValue() {
        return reportValue;
    }

    public void setReportValue(float reportValue) {
        this.reportValue = reportValue;
    }

    public String getJobUnitsProducedOK() {
        return jobUnitsProducedOK;
    }

    public String getJoshUnitsProducedOK() {
        return joshUnitsProducedOK;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setCavitiesActual(Double cavitiesActual) {
        this.cavitiesActual = cavitiesActual;
    }

    public void setCavitiesStandard(Integer cavitiesStandard) {
        this.cavitiesStandard = cavitiesStandard;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public void setMachineID(Integer machineID) {
        this.machineID = machineID;
    }

    public void setShiftID(Integer shiftID) {
        this.shiftID = shiftID;
    }

    public void setJoshID(Integer joshID) {
        this.joshID = joshID;
    }

    public void setJoshName(String joshName) {
        this.joshName = joshName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(String productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public Integer getCavitiesStandard() {
        return cavitiesStandard;
    }

    public Double getCavitiesActual() {
        return cavitiesActual;
    }

    public Integer getDepartment() {
        return department;
    }

    public Integer getJobID() {
        return jobID;
    }

    public Integer getMachineID() {
        return machineID;
    }

    public Integer getShiftID() {
        return shiftID;
    }

    public Integer getJoshID() {
        return joshID;
    }

    public String getJoshName() {
        return joshName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.cavitiesActual);
        dest.writeValue(this.cavitiesStandard);
        dest.writeValue(this.department);
        dest.writeValue(this.jobID);
        dest.writeValue(this.machineID);
        dest.writeValue(this.shiftID);
        dest.writeValue(this.joshID);
        dest.writeString(this.joshName);
        dest.writeString(this.productName);
        dest.writeString(this.productCatalogId);
        dest.writeString(this.jobName);
        dest.writeString(this.jobUnitsProducedOK);
        dest.writeString(this.joshUnitsProducedOK);
    }

    public ActiveJob() {
    }

    protected ActiveJob(Parcel in) {
        this.cavitiesActual = (Double) in.readValue(Double.class.getClassLoader());
        this.cavitiesStandard = (Integer) in.readValue(Integer.class.getClassLoader());
        this.department = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jobID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.machineID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.shiftID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.joshID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.joshName = in.readString();
        this.productName = in.readString();
        this.productCatalogId = in.readString();
        this.jobName = in.readString();
        this.jobUnitsProducedOK = in.readString();
        this.joshUnitsProducedOK = in.readString();
    }

    public static final Parcelable.Creator<ActiveJob> CREATOR = new Parcelable.Creator<ActiveJob>() {
        @Override
        public ActiveJob createFromParcel(Parcel source) {
            return new ActiveJob(source);
        }

        @Override
        public ActiveJob[] newArray(int size) {
            return new ActiveJob[size];
        }
    };
}
