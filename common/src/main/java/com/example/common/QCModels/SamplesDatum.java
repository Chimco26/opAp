package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SamplesDatum {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("UpsertType")
    @Expose
    private Integer upsertType;

    public SamplesDatum() {
    }

    public SamplesDatum(int upsertType, int id) {
        this.upsertType = upsertType;
        this.iD = id;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getUpsertType() {
        return upsertType;
    }

    public void setUpsertType(Integer upsertType) {
        this.upsertType = upsertType;
    }

}
