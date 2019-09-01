package com.example.common.QCModels;

import com.google.gson.annotations.SerializedName;

public class TestDetailsRequest {
    @SerializedName("TestID")
    private int testId;

    public TestDetailsRequest(int testId) {
        this.testId = testId;
    }
}
