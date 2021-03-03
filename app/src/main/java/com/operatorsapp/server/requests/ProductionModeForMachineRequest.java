package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductionModeForMachineRequest {

    @SerializedName("SessionID")
    String sessionID;

    @SerializedName("ProductionModeID")
    int ProductionModeID;

    @SerializedName("Machines")
    int[] machines;


    public ProductionModeForMachineRequest(String sessionID, int ProductionModeID, int[] machines) {
        this.sessionID = sessionID;
        this.ProductionModeID = ProductionModeID;
        this.machines = machines;
    }

    public ProductionModeForMachineRequest(String sessionID, int ProductionModeID, String[] machines) {
        this.sessionID = sessionID;
        this.ProductionModeID = ProductionModeID;
        this.machines = new int[machines.length];
        for (int i = 0; i < machines.length; i++) {
            this.machines[i] = Integer.parseInt(machines[i]);
        }
    }

    public ProductionModeForMachineRequest(String sessionID, int ProductionModeID, ArrayList<String> machines) {
        this.sessionID = sessionID;
        this.ProductionModeID = ProductionModeID;
        this.machines = new int[machines.size()];
        for (int i = 0; i < machines.size(); i++) {
            this.machines[i] = Integer.parseInt(machines.get(i));
        }
    }
}
