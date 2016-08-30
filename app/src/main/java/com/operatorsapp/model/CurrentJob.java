package com.operatorsapp.model;

public class CurrentJob {
    private final String [] mHeaders;
    private final String mFirstField;
    private final String mSecondField;
    private final String mThirdField;
    private final String mFourthField;
    private final String mFifthField;
    private final int mJobId;

    public CurrentJob(String[] headers, String firstField, String secondField, String thirdField, String fourthField, String fifthField, int jobId) {
        mHeaders = headers;
        mFirstField = firstField;
        mSecondField = secondField;
        mThirdField = thirdField;
        mFourthField = fourthField;
        mFifthField = fifthField;
        mJobId = jobId;
    }

    public int getJobId() {
        return mJobId;
    }

    public String[] getHeaders() {
        return mHeaders;
    }

    public String getFirstField() {
        return mFirstField;
    }

    public String getSecondField() {
        return mSecondField;
    }

    public String getThirdField() {
        return mThirdField;
    }

    public String getFourthField() {
        return mFourthField;
    }

    public String getFifthField() {
        return mFifthField;
    }
}
