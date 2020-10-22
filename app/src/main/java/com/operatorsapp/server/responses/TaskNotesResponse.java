package com.operatorsapp.server.responses;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TaskNotesResponse extends StandardResponse {

    @SerializedName("ResponseDictionaryDT")
    private ResponseDictionaryDT mResponseDictionaryDT;

    public TaskNotesResponse(boolean isFunctionSucceed, int mLeaderRecordID, ErrorResponse mError) {
        super(isFunctionSucceed, mLeaderRecordID, mError);
    }

    public static class ResponseDictionaryDT{

        @SerializedName("TaskNotes")
        private ArrayList<TaskNote> mTaskNoteList;


    }

    public ArrayList<TaskNote> getTaskNoteList() {
        return mResponseDictionaryDT.mTaskNoteList;
    }
}
