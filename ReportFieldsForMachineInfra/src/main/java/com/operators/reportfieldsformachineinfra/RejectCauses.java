package com.operators.reportfieldsformachineinfra;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 02/08/2016.
 */
public class RejectCauses {

    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}