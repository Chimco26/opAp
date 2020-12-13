package com.operatorsapp.server.responses;

import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JobListForTestResponse extends StandardResponse {

    @SerializedName("ResponseDictionaryDT")
    TestJobs jobs;


    public ArrayList<JobForTest> getJobForTestList() {
        return jobs != null ? jobs.mJobForTestList : new ArrayList<JobForTest>();
    }

    public class TestJobs {

        @SerializedName("Jobs")
        ArrayList<JobForTest> mJobForTestList;
    }
}
