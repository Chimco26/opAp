package com.operators.jobsinfra;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobListForMachine {
    @SerializedName("Headers")
    private List<Header> mHeaders = new ArrayList<>();
    @SerializedName("Data")
    private List<HashMap<String,Object>> mData = new ArrayList<>();

    public JobListForMachine(List<Header> headers,List<HashMap<String,Object>> data) {
        mHeaders = headers;
        mData = data;
    }

    public List<Header> getHeaders() {
        return mHeaders;
    }

    public List<HashMap<String,Object>> getData() {
        return mData;
    }

}
