package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Omri Bager on 2/15/2016.
 * Falcore LTD
 */
public class MachineResponseModelParams
{
    @SerializedName("FieldEName")
    private String mFieldEName;

    @SerializedName("FieldLName")
    private String mFieldLName;

    @SerializedName("FieldName")
    private String mFieldName;

    @SerializedName("HighLimit")
    private Float mHighLimit;

    @SerializedName("StandardValue")
    private Float mStandardValue;

    @SerializedName("CurrentValue")
    private Float mCurrentValue;

    @SerializedName("LowLimit")
    private Float mLowLimit;

    public String getFieldEName()
    {
        return mFieldEName;
    }

    public String getFieldLName()
    {
        return mFieldLName;
    }

    public String getFieldName()
    {
        return mFieldName;
    }

    public Float getHighLimit()
    {
        return mHighLimit;
    }

    public Float getStandardValue()
    {
        return mStandardValue;
    }

    public Float getCurrentValue()
    {
        return mCurrentValue;
    }

    public Float getLowLimit()
    {
        return mLowLimit;
    }
}
