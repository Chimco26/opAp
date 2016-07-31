package com.operators.machinestatusinfra;

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

    public void setCurrentValue(int currentValue) {
        this.mCurrentValue = currentValue;
    }

    public String getFieldEName() {
        return mFieldEName;
    }

    public void setFieldEName(String fieldEName) {
        this.mFieldEName = fieldEName;
    }

    public String getFieldLName() {
        return mFieldLName;
    }

    public void setFieldLName(String fieldLName) {
        this.mFieldLName = fieldLName;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public void setFieldName(String fieldName) {
        this.mFieldName = fieldName;
    }

    public int getFieldType() {
        return mFieldType;
    }

    public void setFieldType(int fieldType) {
        this.mFieldType = fieldType;
    }
}
