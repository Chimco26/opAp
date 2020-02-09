package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestDetail {
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("MachineID")
    @Expose
    private Integer machineID;
    @SerializedName("MachineName")
    @Expose
    private String machineName;
    @SerializedName("JobID")
    @Expose
    private Integer jobID;
    @SerializedName("JoshID")
    @Expose
    private Integer joshID;
    @SerializedName("ERPJobID")
    @Expose
    private Object eRPJobID;
    @SerializedName("ProductID")
    @Expose
    private Integer productID;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("OrderTime")
    @Expose
    private String orderTime;
    @SerializedName("TestTime")
    @Expose
    private Object testTime;
    @SerializedName("JobNotes")
    @Expose
    private Object jobNotes;
    @SerializedName("TestStatus")
    @Expose
    private Integer testStatus;
    @SerializedName("Batch")
    @Expose
    private Object batch;
    @SerializedName("SampleTime")
    @Expose
    private Object sampleTime;
    @SerializedName("Samples")
    @Expose
    private Integer samples;
    @SerializedName("IssuerUserID")
    @Expose
    private Integer issuerUserID;
    @SerializedName("IssuerName")
    @Expose
    private String issuerName;
    @SerializedName("WorkerID")
    @Expose
    private Object workerID;
    @SerializedName("WorkerName")
    @Expose
    private Object workerName;
    @SerializedName("ExampleNum")
    @Expose
    private Object exampleNum;
    @SerializedName("SubType")
    @Expose
    private Integer subType;
    @SerializedName("TableName")
    @Expose
    private String tableName;
    @SerializedName("HasSamples")
    @Expose
    private Boolean hasSamples;
    @SerializedName("MachineType")
    @Expose
    private Integer machineType;
    @SerializedName("MoldID")
    @Expose
    private Integer moldID;
    @SerializedName("Passed")
    @Expose
    private Boolean passed;
    @SerializedName("AllowEditSamplesCount")
    @Expose
    private Boolean allowEditSamples;

    public Boolean getAllowEditSamples() {
        if (allowEditSamples == null){
            return false;
        }
        return allowEditSamples;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Integer getMachineID() {
        return machineID;
    }

    public void setMachineID(Integer machineID) {
        this.machineID = machineID;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public Integer getJoshID() {
        return joshID;
    }

    public void setJoshID(Integer joshID) {
        this.joshID = joshID;
    }

    public Object getERPJobID() {
        return eRPJobID;
    }

    public void setERPJobID(Object eRPJobID) {
        this.eRPJobID = eRPJobID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Object getTestTime() {
        return testTime;
    }

    public void setTestTime(Object testTime) {
        this.testTime = testTime;
    }

    public Object getJobNotes() {
        return jobNotes;
    }

    public void setJobNotes(Object jobNotes) {
        this.jobNotes = jobNotes;
    }

    public Integer getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(Integer testStatus) {
        this.testStatus = testStatus;
    }

    public Object getBatch() {
        return batch;
    }

    public void setBatch(Object batch) {
        this.batch = batch;
    }

    public Object getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Object sampleTime) {
        this.sampleTime = sampleTime;
    }

    public Integer getSamples() {
        return samples;
    }

    public void setSamples(Integer samples) {
        this.samples = samples;
    }

    public Integer getIssuerUserID() {
        return issuerUserID;
    }

    public void setIssuerUserID(Integer issuerUserID) {
        this.issuerUserID = issuerUserID;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public Object getWorkerID() {
        return workerID;
    }

    public void setWorkerID(Object workerID) {
        this.workerID = workerID;
    }

    public Object getWorkerName() {
        return workerName;
    }

    public void setWorkerName(Object workerName) {
        this.workerName = workerName;
    }

    public Object getExampleNum() {
        return exampleNum;
    }

    public void setExampleNum(Object exampleNum) {
        this.exampleNum = exampleNum;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Boolean getHasSamples() {
        return hasSamples;
    }

    public void setHasSamples(Boolean hasSamples) {
        this.hasSamples = hasSamples;
    }

    public Integer getMachineType() {
        return machineType;
    }

    public void setMachineType(Integer machineType) {
        this.machineType = machineType;
    }

    public Integer getMoldID() {
        return moldID;
    }

    public void setMoldID(Integer moldID) {
        this.moldID = moldID;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }
}
