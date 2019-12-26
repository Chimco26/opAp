package com.example.common.machineData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Worker {
    @SerializedName("HeadWorker")
    @Expose
    private Boolean headWorker;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("UpsertType")
    @Expose
    private Integer upsertType;
    @SerializedName("WorkerID")
    @Expose
    private String workerID;
    @SerializedName("WorkerName")
    @Expose
    private String workerName;

    public Worker(String id) {
        this.workerID = id;
    }

    public Boolean getHeadWorker() {
        return headWorker;
    }

    public void setHeadWorker(Boolean headWorker) {
        this.headWorker = headWorker;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Integer getUpsertType() {
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
