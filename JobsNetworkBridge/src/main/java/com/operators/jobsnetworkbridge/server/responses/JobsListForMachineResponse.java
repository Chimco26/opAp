package com.operators.jobsnetworkbridge.server.responses;


import com.google.gson.annotations.SerializedName;
import com.operators.jobsinfra.Job;
import com.operators.jobsinfra.JobListForMachine;

import java.util.ArrayList;
import java.util.List;

public class JobsListForMachineResponse extends ErrorBaseResponse {
    @SerializedName("titleFields")
    private List<String> titleFields = new ArrayList<String>();
    @SerializedName("jobs")
    private List<Job> jobs = new ArrayList<Job>();

    public JobListForMachine getJobListForMachine(){
        return new JobListForMachine(titleFields,jobs);
    }
}

