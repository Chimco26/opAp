package com.example.common.operator;

import com.example.common.machineData.Worker;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaveShiftWorkersRequest {
    @SerializedName("MachineID")
    private long machineId;
    @SerializedName("SessionID")
    private String sessionId;
    @SerializedName("MainWorker")
    private Worker mainWorker;
    @SerializedName("Workers")
    private List<Worker> workers;

    public SaveShiftWorkersRequest(long machineId, String sessionId, List<Worker> workers, Worker mainWorker) {
        this.machineId = machineId;
        this.sessionId = sessionId;
        this.mainWorker = mainWorker;
        this.workers = workers;
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Worker getMainWorker() {
        return mainWorker;
    }

    public void setMainWorker(Worker mainWorker) {
        this.mainWorker = mainWorker;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }
}
