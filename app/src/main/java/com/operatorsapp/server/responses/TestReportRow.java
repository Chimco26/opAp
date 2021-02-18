package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

public class TestReportRow {

    @SerializedName("ID")
    int mID;

    @SerializedName("Passed")
    boolean mPassed;

    @SerializedName("SubType")
    String mSubType;

    @SerializedName("ProductID")
    String mProductID;

    @SerializedName("CatalogID")
    String mCatalogID;

    @SerializedName("TestType")
    String mTestType;

    @SerializedName("MachineID")
    String mMachineName;

    @SerializedName("JobID")
    int mJobID;

    @SerializedName("JoshID")
    int mJoshID;

    @SerializedName("OrderTime")
    String mOrderTime;

    @SerializedName("UnitsProduced")
    double mUnitsProduced;

    @SerializedName("IssuerUserID")
    String mIssuerUserName;

    @SerializedName("QAWorkerID")
    int mQAWorkerID;

    @SerializedName("Samples")
    String mSamples;

    @SerializedName("ProductGroup")
    String mProductGroup;

    @SerializedName("MoldID")
    String mMoldName;

    @SerializedName("MoldGroupID")
    String mMoldGroupID;

    @SerializedName("FailedFields")
    int mFailedFields;

    @SerializedName("FailedSampleFields")
    int mFailedSampleFields;

    @SerializedName("TestTime")
    String mTestTime;

    @SerializedName("ExampleNum")
    String mExampleNum;

    @SerializedName("Department")
    String mDepartmentName;

    public int getID() {
        return mID;
    }

    public boolean ismPassed() {
        return mPassed;
    }

    public String getSubType() {
        return mSubType;
    }

    public String getProductID() {
        return mProductID;
    }

    public String getCatalogID() {
        return mCatalogID;
    }

    public String getTestType() {
        return mTestType;
    }

    public String getMachineName() {
        return mMachineName;
    }

    public int getJobID() {
        return mJobID;
    }

    public int getJoshID() {
        return mJoshID;
    }

    public String getOrderTime() {
        return mOrderTime;
    }

    public double getUnitsProduced() {
        return mUnitsProduced;
    }

    public String getIssuerUserName() {
        return mIssuerUserName;
    }

    public int getQAWorkerID() {
        return mQAWorkerID;
    }

    public String getSamples() {
        return mSamples;
    }

    public String getProductGroup() {
        return mProductGroup;
    }

    public String getMoldName() {
        return mMoldName;
    }

    public String getMoldGroupID() {
        return mMoldGroupID;
    }

    public int getFailedFields() {
        return mFailedFields;
    }

    public int getFailedSampleFields() {
        return mFailedSampleFields;
    }

    public String getTestTime() {
        return mTestTime;
    }

    public String getExampleNum() {
        return mExampleNum;
    }

    public String getDepartmentName() {
        return mDepartmentName;
    }
}
