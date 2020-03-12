package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubType extends ResponseDictionnaryItemsBaseModel {

    @SerializedName("hassamples")
    @Expose
    private Boolean hasSamples;
    @SerializedName("defaultsamplescount")
    @Expose
    private Integer defaultSamplesCount;
    @SerializedName("alloweditsamplescount")
    @Expose
    private Boolean allowEdit;

    public void setHasSamples(Boolean hasSamples) {
        this.hasSamples = hasSamples;
    }

    public Integer getDefaultSamplesCount() {
        return defaultSamplesCount;
    }

    public void setDefaultSamplesCount(Integer defaultSamplesCount) {
        this.defaultSamplesCount = defaultSamplesCount;
    }

    public Boolean getAllowEdit() {
        if (allowEdit == null){
            return false;
        }
        return allowEdit;
    }

    public void setAllowEdit(Boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public Boolean getHasSamples() {
        if (hasSamples == null){
            return false;
        }
        return hasSamples;
    }

}