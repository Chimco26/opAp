package com.example.common.department;

import com.google.gson.annotations.SerializedName;

public class ProductionStatus {


    @SerializedName("ID")
    private int id;

    @SerializedName("Name")
    private String name;

    public int getId() {
        return id;
    }

    public String getStatusName() {
        return name;
    }
}
