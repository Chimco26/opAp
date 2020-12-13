package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValueList {
    @SerializedName("ID")
    @Expose
    private int iD;

    @SerializedName("Value")
    @Expose
    private String value;

    public int getID() {
        return iD;
    }

    public String getValue() {
        return value;
    }
}
