package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestDetailFormForSend{
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("CurrentValue")
    @Expose
    private String currentValue;

    public TestDetailFormForSend(String name, String currentValue) {
        this.name = name;
        this.currentValue = currentValue;
    }
}
