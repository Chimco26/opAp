package com.example.common.task;


import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TaskFileResponse  extends StandardResponse {

    public TaskFileResponse(boolean isFunctionSucceed, int mLeaderRecordID, ErrorResponse mError) {
        super(isFunctionSucceed, mLeaderRecordID, mError);
    }

    @SerializedName("ResponseDictionaryDT")
    private DictionaryDTResponse mDictionaryDTResponse;

    public ArrayList<TaskFiles> getFiles(){
        return mDictionaryDTResponse.getFileList();
    }

    public DictionaryDTResponse getDictionaryDTResponse() {
        return mDictionaryDTResponse;
    }

    public class DictionaryDTResponse {

        @SerializedName("TaskFiles")
        private ArrayList<TaskFiles> mFileList;

        public ArrayList<TaskFiles> getFileList() {
            return mFileList;
        }
    }



    public class TaskFiles {

        @SerializedName("FileID")
        private int mFileID;

        @SerializedName("TaskID")
        private int mTaskID;

        @SerializedName("TaskHistoryID")
        private int mTaskHistoryID;

        @SerializedName("LName")
        private String mLName;

        @SerializedName("EName")
        private String mEName;

        @SerializedName("Descr")
        private String mDescr;

        @SerializedName("FilePath")
        private String mFilePath;

        @SerializedName("FileType")
        private String mFileType;

        public int getmFileID() {
            return mFileID;
        }

        public int getmTaskID() {
            return mTaskID;
        }

        public int getmTaskHistoryID() {
            return mTaskHistoryID;
        }

        public String getmLName() {
            return mLName;
        }

        public String getmEName() {
            return mEName;
        }

        public String getmDescr() {
            return mDescr;
        }

        public String getmFilePath() {
            return mFilePath;
        }

        public String getmFileType() {
            return mFileType;
        }
    }

}
