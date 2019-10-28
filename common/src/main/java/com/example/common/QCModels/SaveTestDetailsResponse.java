package com.example.common.QCModels;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaveTestDetailsResponse extends StandardResponse {
    @SerializedName("Passed")
    @Expose
    private Boolean passed;
    @SerializedName("FailedSampleFields")
    @Expose
    private List<Integer> failedTestSampleFieldsData = null;
    @SerializedName("FailedTestFields")
    @Expose
    private List<Integer> failedTestFieldsData = null;

    public SaveTestDetailsResponse(Boolean passed, List<Integer> failedTestSampleFieldsData, List<Integer> failedTestFieldsData) {
        this.passed = passed;
        this.failedTestSampleFieldsData = failedTestSampleFieldsData;
        this.failedTestFieldsData = failedTestFieldsData;
    }

    public Boolean isPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public List<Integer> getFailedTestSampleFieldsData() {
        return failedTestSampleFieldsData;
    }

    public void setFailedTestSampleFieldsData(List<Integer> failedTestSampleFieldsData) {
        this.failedTestSampleFieldsData = failedTestSampleFieldsData;
    }

    public List<Integer> getFailedTestFieldsData() {
        return failedTestFieldsData;
    }

    public void setFailedTestFieldsData(List<Integer> failedTestFieldsData) {
        this.failedTestFieldsData = failedTestFieldsData;
    }
}
