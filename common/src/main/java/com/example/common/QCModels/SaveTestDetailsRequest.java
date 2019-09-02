package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SaveTestDetailsRequest {
    @SerializedName("SampleFields")
    @Expose
    private List<TestSampleFieldsDatum> testSampleFieldsData = null;
    @SerializedName("TestFields")
    @Expose
    private List<TestFieldsDatum> testFieldsData = null;
    @SerializedName("Samples")
    @Expose
    private int samples;
    @SerializedName("TestID")
    @Expose
    private int testId;
    @SerializedName("TestDetails")
    @Expose
    private ArrayList<String> testDetails = new ArrayList<>();

    public SaveTestDetailsRequest(List<TestSampleFieldsDatum> testSampleFieldsData, List<TestFieldsDatum> testFieldsData, int samples, int testId) {
        this.testSampleFieldsData = testSampleFieldsData;
        this.testFieldsData = testFieldsData;
        this.samples = samples;
        this.testId = testId;
    }
}
