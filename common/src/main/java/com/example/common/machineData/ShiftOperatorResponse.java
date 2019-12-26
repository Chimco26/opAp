package com.example.common.machineData;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShiftOperatorResponse extends StandardResponse {

    @SerializedName("Workers")
    @Expose
    List<Worker> workers;

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }
}
