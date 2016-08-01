package com.operators.machinestatusinfra.models;

import com.google.gson.annotations.SerializedName;
import com.operators.machinestatusinfra.models.BaseWidget;

public class RangeWidget extends BaseWidget {

    @SerializedName("HighLimit")
    private int mHighLimit;

    @SerializedName("LowLimit")
    private int mLowLimit;

    @SerializedName("StandardValue")
    private int mStandardValue;

    @SerializedName("isOutOfRange")
    private boolean mIsOutOfRange;

    public int getHighLimit() {
        return mHighLimit;
    }

    public int getLowLimit() {
        return mLowLimit;
    }

    public int getStandardValue() {
        return mStandardValue;
    }

    public boolean isOutOfRange() {
        return mIsOutOfRange;
    }
}
