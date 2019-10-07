package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubType extends ResponseDictionnaryItemsBaseModel{

    @SerializedName("hassamples")
    @Expose
    private Boolean hasSamples;

    public Boolean getHasSamples() {
        return hasSamples;
    }

    public void setHassamples(Boolean hassamples) {
        this.hasSamples = hassamples;
    }

}