package com.example.common.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeValue {
    @SerializedName("RecipeID")
    @Expose
    private Integer recipeID;
    @SerializedName("FValue")
    @Expose
    private String fValue;
    @SerializedName("LValue")
    @Expose
    private Float lValue;
    @SerializedName("HValue")
    @Expose
    private Float hValue;

    public RecipeValue(Integer recipeID, String fValue, Float lValue, Float hValue) {
        this.recipeID = recipeID;
        this.fValue = fValue;
        this.lValue = lValue;
        this.hValue = hValue;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Integer recipeID) {
        this.recipeID = recipeID;
    }

    public String getFValue() {
        return fValue;
    }

    public void setFValue(String fValue) {
        this.fValue = fValue;
    }

    public Float getLValue() {
        return lValue;
    }

    public void setLValue(Float lValue) {
        this.lValue = lValue;
    }

    public Float getHValue() {
        return hValue;
    }

    public void setHValue(Float hValue) {
        this.hValue = hValue;
    }
}
