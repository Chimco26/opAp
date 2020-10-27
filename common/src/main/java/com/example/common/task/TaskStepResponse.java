package com.example.common.task;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TaskStepResponse extends StandardResponse {
    @SerializedName("ResponseDictionaryDT")
    private ResponseDictionaryDT mResponseDictionaryDT;

    public static class ResponseDictionaryDT{

        @SerializedName("TaskSteps")
        private ArrayList<TaskStep> mTaskStepList;
    }

    public ArrayList<TaskStep> getTaskStepList() {
        return mResponseDictionaryDT.mTaskStepList;
    }


    public TaskStepResponse(boolean isFunctionSucceed, int mLeaderRecordID, ErrorResponse mError) {
        super(isFunctionSucceed, mLeaderRecordID, mError);
    }

}
