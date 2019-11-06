package com.example.common.QCModels;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TestDetailsResponse extends StandardResponse {
    public static final String FIELD_TYPE_BOOLEAN = "Boolean";
    public static final int FIELD_TYPE_BOOLEAN_INT = 0;
    public static final String FIELD_TYPE_DATE = "date";
    public static final int FIELD_TYPE_DATE_INT = 1;
    public static final String FIELD_TYPE_NUM = "num";
    public static final int FIELD_TYPE_NUM_INT = 2;
    public static final String FIELD_TYPE_TEXT = "text";
    public static final int FIELD_TYPE_TEXT_INT = 3;
    public static final String FIELD_TYPE_TIME = "time";
    public static final int FIELD_TYPE_TIME_INT = 4;
    public static final int FIELD_TYPE_INTERVAL_INT = 5;
    public static final String FIELD_TYPE_LAST = "last";
    public static final int FIELD_TYPE_LAST_INT = 6;

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
    @SerializedName("TestFieldsGroups")
    @Expose
    private List<TestFieldsGroup> testFieldsGroups = null;
    @SerializedName("TestFieldsData")
    @Expose
    private List<TestFieldsDatum> testFieldsData = null;
    @SerializedName("Files")
    @Expose
    private List<String> files = null;
    private List<TestSampleFieldsDatum> originalSampleFields;
    private ArrayList<ArrayList<TestFieldsDatum>> testFieldsSamplesComplete;

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

    public List<TestFieldsGroup> getTestFieldsGroups() {
        if (testFieldsGroups == null){
            return new ArrayList<>();
        }
        return testFieldsGroups;
    }

    public void setTestFieldsGroups(List<TestFieldsGroup> testFieldsGroups) {
        this.testFieldsGroups = testFieldsGroups;
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

    public void setOriginalSampleFields(List<TestSampleFieldsDatum> originalSampleFields) {
        this.originalSampleFields = originalSampleFields;
    }

    public List<TestSampleFieldsDatum> getOriginalSampleFields() {
        if (originalSampleFields != null) {
            return originalSampleFields;
        }else {
            return getTestSampleFieldsData();
        }
    }

    public void setCompleteTestList(ArrayList<ArrayList<TestFieldsDatum>> completeList) {
        testFieldsSamplesComplete = completeList;
    }

    public ArrayList<ArrayList<TestFieldsDatum>> getTestFieldsComplete() {
        return testFieldsSamplesComplete;
    }
}
