package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

public class CreateTaskNotesRequest {
    @SerializedName("SessionID")
    private String mSessionId;

    @SerializedName("task")
    private CreateTaskNote createTaskNote;

    public CreateTaskNotesRequest(String sessionId, int taskId, int commentId, int historyId, String commentText) {
        mSessionId = sessionId;
        createTaskNote = new CreateTaskNote(commentId, taskId, historyId, commentText);
    }

    public class CreateTaskNote {

        @SerializedName("ID")
        private int commentId;

        @SerializedName("TaskID")
        private int taskId;

        @SerializedName("HistoryID")
        private int historyId;

        @SerializedName("Text")
        private String commentText;

        @SerializedName("SourceTaskCreationPlatform")
        private int Id;

        public CreateTaskNote(int commentId, int taskId, int historyId, String commentText) {
            this.commentId = commentId;
            this.taskId = taskId;
            this.historyId = historyId;
            this.commentText = commentText;
            this.Id = 2;
        }
    }
}
