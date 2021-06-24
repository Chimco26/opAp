package com.operators.machinedatainfra.models;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Widget {

    public static final int FIELD_TYPE_ACTIVATE_JOB = 100;

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

    @SerializedName("AverageValue")
    private String mCycleTimeAvg;

    public String getCycleTimeAvg() {
        if (mCycleTimeAvg == null){
            return "0";
        }
        return mCycleTimeAvg;
    }

    public void setCycleTimeAvg(String mCycleTimeAvg) {
        this.mCycleTimeAvg = mCycleTimeAvg;
    }

    private int mEditStep;

    public int getEditStep() {
        return mEditStep;
    }

    public void setEditStep(int mEditStep) {
        this.mEditStep = mEditStep;
    }

    public class HistoricData implements Comparable<HistoricData> {
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

        @Override
        public int compareTo(@NonNull HistoricData o) {
            return getDateForNotification(getTime()).compareTo(getDateForNotification(o.getTime()));
        }
        public static final String SQL_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String SQL_NO_T_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String SIMPLE_FORMAT_FORMAT = "dd/MM/yyyy HH:mm:ss";
        @SuppressLint("SimpleDateFormat")
        public Date getDateForNotification(String time) {

            SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_FORMAT_FORMAT);
            SimpleDateFormat dateFormatSql = new SimpleDateFormat(SQL_NO_T_FORMAT);
            SimpleDateFormat dateFormatSqlT = new SimpleDateFormat(SQL_T_FORMAT);

            try {
                return dateFormat.parse(time);
            } catch (java.text.ParseException e) {
            }

            try {
                return dateFormatSql.parse(time);
            } catch (java.text.ParseException e) {
            }

            try {
                return dateFormatSqlT.parse(time);

            } catch (java.text.ParseException e) {
            }

            return null;
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
        if(mCurrentValue == null || mCurrentValue.isEmpty()){
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

    public void setmFieldType(Integer mFieldType) {
        this.mFieldType = mFieldType;
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

    public void setStandardValue(Float mStandardValue) {
        this.mStandardValue = mStandardValue;
    }

    public boolean isOutOfRange() {
        return mIsOutOfRange;
    }

    public ArrayList<HistoricData> getMachineParamHistoricData() {
        return mMachineParamHistoricData;
    }

    public void setMachineParamHistoricData(ArrayList<HistoricData> mMachineParamHistoricData) {
        this.mMachineParamHistoricData = mMachineParamHistoricData;
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
