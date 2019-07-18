package com.operators.activejobslistformachinenetworkbridge.server.responses;

import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;
import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 14/08/2016.
 */
public class ActiveJobsListForMachineResponse extends StandardResponse {
    @SerializedName("Joshs")
    private List<ActiveJob> jobs = new ArrayList<ActiveJob>();

    public ActiveJobsListForMachineResponse(List<ActiveJob> jobs) {
        this.jobs = jobs;
    }


    public ActiveJobsListForMachine getActiveJobsListForMachine(){
       return new ActiveJobsListForMachine(jobs);
    }
}
