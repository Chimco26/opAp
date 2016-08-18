package com.operators.machinedatainfra.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Widget {
    @SerializedName("CurrentValue")
    private String mCurrentValue;

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
    private List<HistoricData> mMachineParamHistoricData;

    public class HistoricData {
        @SerializedName("Time")
        private Long mTime;

        @SerializedName("Value")
        private Integer mValue;

        public Long getTime() {
            return mTime;
        }

        public int getValue() {
            return mValue;
        }

    }

    @SerializedName("ID")
    private long mID;

    @SerializedName("Projection")
    private int mProjection;

    @SerializedName("Target")
    private int mTarget;


    public String getCurrentValue() {
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

    public int getFieldType() {
        return mFieldType;
    }

    public float getHighLimit() {
        if (mHighLimit == null) {
            mHighLimit = 0f;
        }
        return mHighLimit;
    }

    public float getLowLimit() {
        if (mLowLimit == null) {
            mLowLimit = 0f;
        }
        return mLowLimit;
    }

    public float getStandardValue() {
        if (mStandardValue == null) {
            mStandardValue = 0f;
        }
        return mStandardValue;
    }

    public boolean isOutOfRange() {
        return mIsOutOfRange;
    }

    public List<HistoricData> getMachineParamHistoricData() {
        return mMachineParamHistoricData;
    }

    public long getID() {
        return mID;
    }

    public int getProjection() {
        return mProjection;
    }

    public int getTarget() {
        return mTarget;
    }
}
