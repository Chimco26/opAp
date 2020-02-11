package com.example.common.task;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskObjectsForCreateOrEditResponse extends StandardResponse {

    @SerializedName("ResponseDictionaryDT")
    @Expose
    private TaskObjectForCreateOrEditContent responseDictionaryDT;


    public TaskObjectForCreateOrEditContent getResponseDictionaryDT() {
        return responseDictionaryDT;
    }

    public void setResponseDictionaryDT(TaskObjectForCreateOrEditContent responseDictionaryDT) {
        this.responseDictionaryDT = responseDictionaryDT;
    }
}
