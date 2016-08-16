package com.operators.reportfieldsformachineinfra;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 14/08/2016.
 */
public class PackageTypes {
    @SerializedName("ID")
    private int id;
    @SerializedName("EName")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
