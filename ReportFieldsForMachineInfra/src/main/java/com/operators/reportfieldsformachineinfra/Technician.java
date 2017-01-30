package com.operators.reportfieldsformachineinfra;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Oren on 1/30/2017.
 */
public class Technician
{
    @SerializedName("EName")
    private String mEName;
    @SerializedName("ID")
    private int mID;
    @SerializedName("LName")
    private String mLName;

    public String getEName()
    {
        return mEName;
    }

    public int getID()
    {
        return mID;
    }

    public String getLName()
    {
        return mLName;
    }

}
