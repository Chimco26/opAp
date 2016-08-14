package com.operators.activejobslistformachinenetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;
import com.operators.activejobslistformachineinfra.ActiveJob;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 14/08/2016.
 */
public class GetActiveJobsListForMachineResponse extends ErrorBaseResponse {
    @SerializedName("Jobs")
    private List<ActiveJob> jobs = new ArrayList<ActiveJob>();

    public GetActiveJobsListForMachineResponse(List<ActiveJob> jobs) {
        this.jobs = jobs;
    }

    public List<ActiveJob> getActiveJobs() {
        return jobs;
    }
}
