package com.operators.activejobslistformachineinfra;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 14/08/2016.
 */
public class ActiveJobsListForMachine {
    @SerializedName("Joshs")
    private List<ActiveJob> jobs = new ArrayList<ActiveJob>();

    public ActiveJobsListForMachine(List<ActiveJob> jobs) {
        this.jobs = jobs;
    }

    public List<ActiveJob> getActiveJobs() {
        return jobs;
    }
}
