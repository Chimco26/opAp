package com.example.common.task;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskListResponse extends StandardResponse {

    @SerializedName("ResponseDictionaryDT")
    @Expose
    private TaskListContentResponse responseDictionaryDT;


    public TaskListContentResponse getResponseDictionaryDT() {
        return responseDictionaryDT;
    }

    public void setResponseDictionaryDT(TaskListContentResponse responseDictionaryDT) {
        this.responseDictionaryDT = responseDictionaryDT;
    }
}
