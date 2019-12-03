package com.example.common.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipeUpdateRequest {
    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("JobID")
    @Expose
    private Integer jobID;
    @SerializedName("recipeValue")
    @Expose
    private List<RecipeValue> recipeValue = null;
    @SerializedName("recipeRefStandardID")
    @Expose
    private Integer recipeRefStandardID;
    @SerializedName("recipeRefType")
    @Expose
    private Integer recipeRefType;

    public RecipeUpdateRequest(String sessionID, Integer jobID, List<RecipeValue> recipeValue, Integer recipeRefStandardID, Integer recipeRefType) {
        this.sessionID = sessionID;
        this.jobID = jobID;
        this.recipeValue = recipeValue;
        this.recipeRefStandardID = recipeRefStandardID;
        this.recipeRefType = recipeRefType;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public List<RecipeValue> getRecipeValue() {
        if (recipeValue == null){
            recipeValue = new ArrayList<>();
        }
        return recipeValue;
    }

    public void setRecipeValue(List<RecipeValue> recipeValue) {
        this.recipeValue = recipeValue;
    }

    public Integer getRecipeRefStandardID() {
        return recipeRefStandardID;
    }

    public void setRecipeRefStandardID(Integer recipeRefStandardID) {
        this.recipeRefStandardID = recipeRefStandardID;
    }

    public Integer getRecipeRefType() {
        return recipeRefType;
    }

    public void setRecipeRefType(Integer recipeRefType) {
        this.recipeRefType = recipeRefType;
    }

}
