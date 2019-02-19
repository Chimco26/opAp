package com.operators.machinedatainfra.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Widget {
    @SerializedName("CurrentValue")
    private String mCurrentValue;

    @SerializedName("CurrentColor")
    private String mCurrentColor;

    @SerializedName("ProjectionColor")
    private String mProjectionColor;

    @SerializedName("TargetColor")
    private String mTargetColor;

    @SerializedName("FieldEName")
    private String mFieldEName;

    @SerializedName("FieldLName")
    private String mFieldLName;

    @SerializedName("FieldName")
    private String mFieldName;

    @SerializedName("fieldType")
    private Integer mFieldType;

    @SerializedName("HighLimit")
    private Float mHighLimit;

    @SerializedName("LowLimit")
    private Float mLowLimit;

    @SerializedName("StandardValue")
    private Float mStandardValue;

    @SerializedName("isOutOfRange")
    private boolean mIsOutOfRange;

    @SerializedName("MachineParamHistoricData")
    private ArrayList<HistoricData> mMachineParamHistoricData;

    private int mEditStep;

    public int getEditStep() {
        return mEditStep;
    }

    public void setEditStep(int mEditStep) {
        this.mEditStep = mEditStep;
    }

    public class HistoricData {
        @SerializedName("CurrentValue")
        private Float mCurrentValue;
        @SerializedName("Time")
        private String mTime;

        public Float getValue() {
            return mCurrentValue;
        }

        public String getTime() {
            return mTime;
        }
    }

    @SerializedName("ID")
    private long mID;

    @SerializedName("Projection")
    private Float mProjection;

    @SerializedName("Target")
    private Float mTarget;

    @SerializedName("TargetScreen")
    private String mTargetScreen;


    public String getCurrentColor() {
        return getColor(mCurrentColor);
    }

    public void setCurrentColor(String mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    public String getProjectionColor() {
        return getColor(mProjectionColor);
    }

    public void setProjectionColor(String mProjectionColor) {
        this.mProjectionColor = mProjectionColor;
    }

    public String getTargetColor() {
        return getColor(mTargetColor);
    }

    private String getColor(String color){
        if (color == null){
            return "#bababa";
        }else {
            return color;
        }
    }

    public void setTargetColor(String mTargetColor) {
        this.mTargetColor = mTargetColor;
    }

    public String getTargetScreen() {
        if (mTargetScreen == null){
            mTargetScreen = "";
        }
        return mTargetScreen;
    }

    public void setTargetScreen(String mTargetScreen) {
        this.mTargetScreen = mTargetScreen;
    }

    public void setCurrentValue(String mCurrentValue) {
        this.mCurrentValue = mCurrentValue;
    }

    public void setProjection(Float mProjection) {
        this.mProjection = mProjection;
    }

    public void setTarget(Float mTarget) {
        this.mTarget = mTarget;
    }

    public String getCurrentValue() {
        if(mCurrentValue == null){
            mCurrentValue = "--";
        }
        return mCurrentValue;
    }

    public String getFieldEName() {
        return mFieldEName;
    }

    public String getFieldLName() {
        return mFieldLName;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public Integer getFieldType() {
        return mFieldType;
    }

    public Float getHighLimit() {
        if (mHighLimit == null) {
            mHighLimit = 0f;
        }
        return mHighLimit;
    }

    public Float getLowLimit() {
        if (mLowLimit == null) {
            mLowLimit = 0f;
        }
        return mLowLimit;
    }

    public Float getStandardValue() {
        if (mStandardValue == null) {
            mStandardValue = 0f;
        }
        return mStandardValue;
    }

    public boolean isOutOfRange() {
        return mIsOutOfRange;
    }

    public ArrayList<HistoricData> getMachineParamHistoricData() {
        return mMachineParamHistoricData;
    }

    public long getID() {
        return mID;
    }

    public Float getProjection() {
        if (mProjection == null) {
            mProjection = 0f;
        }
        return mProjection;
    }

    public Float getTarget() {
        if (mTarget == null) {
            mTarget = 0f;
        }
        return mTarget;
    }

    public void createDemo(){
        mCurrentValue = "99";
        mFieldEName = "counter";
        mFieldName = "counter";
        mID = 99;
        mFieldType = 4;
    }
}
