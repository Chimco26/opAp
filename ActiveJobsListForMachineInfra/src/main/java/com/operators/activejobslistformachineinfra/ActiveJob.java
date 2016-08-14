package com.operators.activejobslistformachineinfra;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 14/08/2016.
 */
public class ActiveJob {

    @SerializedName("JobID")
    private Integer jobID;
    @SerializedName("JobName")
    private String jobName;

    public Integer getJobID() {
        return jobID;
    }

    public String getJobName() {
        return jobName;
    }
}
