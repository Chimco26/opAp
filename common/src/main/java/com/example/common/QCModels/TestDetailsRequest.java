package com.example.common.QCModels;

import com.google.gson.annotations.SerializedName;

public class TestDetailsRequest {
    @SerializedName("TestID")
    private int testId;

    public TestDetailsRequest(int testId) {
        this.testId = testId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }
}
