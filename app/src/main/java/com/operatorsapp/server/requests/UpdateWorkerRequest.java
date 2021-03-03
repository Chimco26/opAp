package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpdateWorkerRequest {


    @SerializedName("SessionID")
    String sessionID;

    @SerializedName("WorkerID")
    String workerID;

    @SerializedName("Machines")
    int[] machines;

    public UpdateWorkerRequest(String sessionID, String workerID, int[] machines) {
        this.sessionID = sessionID;
        this.workerID = workerID;
        this.machines = machines;
    }

    public UpdateWorkerRequest(String sessionID, String workerID, String[] machines) {
        this.sessionID = sessionID;
        this.workerID = workerID;
        this.machines = new int[machines.length];
        for (int i = 0; i < machines.length; i++) {
            this.machines[i] = Integer.parseInt(machines[i]);
        }
    }


    public UpdateWorkerRequest(String sessionId, String workerID, ArrayList<String> machines) {
        this.sessionID = sessionID;
        this.workerID = workerID;
        this.machines = new int[machines.size()];
        for (int i = 0; i < machines.size(); i++) {
            this.machines[i] = Integer.parseInt(machines.get(i));
        }
    }
}
