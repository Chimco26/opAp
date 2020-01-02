package com.example.common.machineData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Worker {
    @SerializedName("HeadWorker")
    @Expose
    private Boolean headWorker;
    @SerializedName("ID")
    @Expose
    private int iD;
    @SerializedName("UpsertType")
    @Expose
    private int upsertType;
    @SerializedName("WorkerID")
    @Expose
    private String workerID;
    @SerializedName("WorkerName")
    @Expose
    private String workerName;

    public Worker(String id) {
        this.workerID = id;
    }

    public Worker(String workerID, String workerName, Integer upsertType) {
        this.upsertType = upsertType;
        this.workerID = workerID;
        this.workerName = workerName;
    }

    public Boolean isHeadWorker() {
        return headWorker;
    }

    public void setHeadWorker(Boolean headWorker) {
        this.headWorker = headWorker;
    }

    public int getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public int getUpsertType() {
        return upsertType;
    }

    public void setUpsertType(Integer upsertType) {
        this.upsertType = upsertType;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

}
