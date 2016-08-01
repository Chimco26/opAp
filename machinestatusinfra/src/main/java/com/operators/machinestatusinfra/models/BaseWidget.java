package com.operators.machinestatusinfra.models;

import com.google.gson.annotations.SerializedName;

public class BaseWidget {
    @SerializedName("CurrentValue")
    private int mCurrentValue;

    @SerializedName("FieldEName")
    private String mFieldEName;

    @SerializedName("FieldLName")
    private String mFieldLName;

    @SerializedName("FieldName")
    private String mFieldName;

    @SerializedName("fieldType")
    private int mFieldType;

    public int getCurrentValue() {
        return mCurrentValue;
    }

    public String getFieldEName() {
        return mFieldEName;
    }

    public String getFieldLName() {
        return mFieldLName;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public int getFieldType() {
        return mFieldType;
    }

}
