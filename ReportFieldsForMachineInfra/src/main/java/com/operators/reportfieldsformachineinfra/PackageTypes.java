package com.operators.reportfieldsformachineinfra;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 14/08/2016.
 */
public class PackageTypes {
    @SerializedName("ID")
    private int id;
    @SerializedName("EName")
    private String EName;
    @SerializedName("LName")
    private String LName;

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
}
