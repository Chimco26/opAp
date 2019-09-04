package com.example.common.QCModels;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestDetailsResponse extends StandardResponse {
    public static final int FIELD_TYPE_BOOLEAN = 1;
    public static final int FIELD_TYPE_DATE = 2;
    public static final int FIELD_TYPE_NUM = 3;
    public static final int FIELD_TYPE_TEXT = 4;
    public static final int FIELD_TYPE_TIME = 5;
    public static final int FIELD_TYPE_INTERVAL = 6;

    @SerializedName("StatusList")
    @Expose
    private List<StatusList> statusList = null;
    @SerializedName("TestDetails")
    @Expose
    private List<TestDetail> testDetails = null;
    @SerializedName("TestDetailsForm")
    @Expose
    private List<TestDetailsForm> testDetailsForm = null;
    @SerializedName("TestSampleFieldsData")
    @Expose
    private List<TestSampleFieldsDatum> testSampleFieldsData = null;
    @SerializedName("TestFieldsData")
    @Expose
    private List<TestFieldsDatum> testFieldsData = null;
    @SerializedName("Files")
    @Expose
    private List<String> files = null;

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public List<StatusList> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<StatusList> statusList) {
        this.statusList = statusList;
    }

    public List<TestDetail> getTestDetails() {
        return testDetails;
    }

    public void setTestDetails(List<TestDetail> testDetails) {
        this.testDetails = testDetails;
    }

    public List<TestDetailsForm> getTestDetailsForm() {
        return testDetailsForm;
    }

    public void setTestDetailsForm(List<TestDetailsForm> testDetailsForm) {
        this.testDetailsForm = testDetailsForm;
    }

    public List<TestSampleFieldsDatum> getTestSampleFieldsData() {
        return testSampleFieldsData;
    }

    public void setTestSampleFieldsData(List<TestSampleFieldsDatum> testSampleFieldsData) {
        this.testSampleFieldsData = testSampleFieldsData;
    }

    public List<TestFieldsDatum> getTestFieldsData() {
        return testFieldsData;
    }

    public void setTestFieldsData(List<TestFieldsDatum> testFieldsData) {
        this.testFieldsData = testFieldsData;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
