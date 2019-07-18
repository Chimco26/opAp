package com.operators.activejobslistformachinenetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;
import com.operators.activejobslistformachineinfra.ActiveJob;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 14/08/2016.
 */
public class GetActiveJobsListForMachineRequest {
    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("MachineID")
    private int mMachineId;

    public GetActiveJobsListForMachineRequest(String sessionId, int machineId) {
        mSessionId = sessionId;
        mMachineId = machineId;
    }
}
