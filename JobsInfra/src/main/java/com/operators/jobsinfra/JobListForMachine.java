package com.operators.jobsinfra;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class JobListForMachine {
    @SerializedName("titleFields")
    private List<String> titleFields = new ArrayList<>();
    @SerializedName("jobs")
    private List<Job> jobs = new ArrayList<>();

    public JobListForMachine(List<String> titleFields, List<Job> jobs) {
        this.titleFields = titleFields;
        this.jobs = jobs;
    }

    public List<String> getTitleFields() {
        return titleFields;
    }

    public void setTitleFields(List<String> titleFields) {
        this.titleFields = titleFields;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
