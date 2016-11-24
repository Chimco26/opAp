package com.operators.reportfieldsformachineinfra;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 02/08/2016.
 */
public class SubReasons {

    @SerializedName("ID")
    private int id;
    @SerializedName("EName")
    private String EName;
    @SerializedName("LName")
    private String LName;
    @SerializedName("SubReason")
    private SubReasons[] subReasons;

    public int getId() {
        return id;
    }

    public String getEName() {
        return EName;
    }

    public String getLName()
    {
        return LName;
    }

    public SubReasons[] getSubReasons()
    {
        return subReasons;
    }
}
