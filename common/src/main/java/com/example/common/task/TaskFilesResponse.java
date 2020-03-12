package com.example.common.task;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TaskFilesResponse extends StandardResponse {

    @SerializedName("ResponseDictionaryDT")
    @Expose
    private TaskFilesDt responseDictionaryDT;

    public TaskFilesDt getResponseDictionaryDT() {
        return responseDictionaryDT;
    }

    public class TaskFilesDt{
        @SerializedName("TaskFiles")
        @Expose
        private ArrayList<TaskFiles> taskFiles;

        public ArrayList<TaskFiles> getTaskFiles() {
            return taskFiles;
        }
    }

    public class TaskFiles{
        @SerializedName("FilePath")
        @Expose
        private String filePath;

        public String getFilePath() {
            return filePath;
        }
    }
}
