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

    public List<String> getTitleFields() {
        return titleFields;
    }

    public void setTitleFields(List<String> titleFields) {
        this.titleFields = titleFields;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public JobListForMachine getJobListForMachine(){
        return new JobListForMachine(titleFields,jobs);
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}

