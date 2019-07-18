package com.operators.jobsnetworkbridge.server.responses;


import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;
import com.operators.jobsinfra.Header;
import com.operators.jobsinfra.JobListForMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobsListForMachineResponse extends StandardResponse {
    @SerializedName("Headers")
    private List<Header> mHeaders = new ArrayList<>();
    @SerializedName("Data")
    private List<HashMap<String,Object>>mData = new ArrayList<>();

    public JobListForMachine getJobListForMachine(){
        return new JobListForMachine(mHeaders,mData);
    }
}

