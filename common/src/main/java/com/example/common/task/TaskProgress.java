package com.example.common.task;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.common.utils.TimeUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TaskProgress implements Parcelable {

    public static final String TAG = TaskProgress.class.getSimpleName();

    public TaskProgress() {
    }

    public enum TaskStatus {
        OPEN(1),
        TODO(2),
        IN_PROGRESS(3),
        DONE(4),
        CANCELLED(5);

        private final int mValue;

        TaskStatus(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public enum TaskPriority {
        LOW(1),
        MEDIUM(2),
        HIGH(3),
        VERY_HIGH(4);

        private final int mValue;

        TaskPriority(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    @SerializedName("TaskID")
    @Expose
    private int taskID;
    @SerializedName("HistoryID")
    @Expose
    private int historyID;
    @SerializedName("TaskStatus")
    @Expose
    private int taskStatus;
    @SerializedName("TaskCreateUser")
    @Expose
    private int taskCreateUser;
    @SerializedName("CreateUserName")
    @Expose
    private String createUserName;
    @SerializedName("CreateUserHName")
    @Expose
    private String createUserHName;
    @SerializedName("AssigneeDisplayName")
    @Expose
    private String assigneeDisplayName;
    @SerializedName("AssigneeDisplayHName")
    @Expose
    private String assigneeDisplayHName;
    @SerializedName("Assignee")
    @Expose
    private int assignee;
    @SerializedName("TaskCreateDate")
    @Expose
    private String taskCreateDate;
    @SerializedName("TaskStartTimeTarget")
    @Expose
    private String taskStartTimeTarget;
    @SerializedName("TaskEndTimeTarget")
    @Expose
    private String taskEndTimeTarget;
    @SerializedName("SubjectTrans")
    @Expose
    private String subjectTrans;
    @SerializedName("SubjectID")
    @Expose
    private int subjectId;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("TaskPriorityTrans")
    @Expose
    private String taskPriorityTrans;
    @SerializedName("TaskPriorityID")
    @Expose
    private int taskPriorityID;
    @SerializedName("HistoryDisplayHName")
    @Expose
    private String historyDisplayHName;
    @SerializedName("HistoryDisplayName")
    @Expose
    private String historyDisplayName;
    @SerializedName("HistoryUser")
    @Expose
    private String historyUser;
    @SerializedName("HistoryCreateDate")
    @Expose
    private String historyCreateDate;
    @SerializedName("TaskLevel")
    @Expose
    private int taskLevel;
    @SerializedName("StringID")
    @Expose
    private int StringID;
    @SerializedName("LName")
    @Expose
    private String lName;
    @SerializedName("EName")
    @Expose
    private String eName;
    @SerializedName("EstimatedExecutionTime")
    @Expose
    private double estimatedExecutionTime;
    @SerializedName("NumOfOpenSubTasks")
    @Expose
    private int mNumOfOpenSubTasks;

    public TaskProgress(int taskID) {
        this.taskID = taskID;
    }

    public int getNumOfOpenSubTasks() {
        return mNumOfOpenSubTasks;
    }

    public String getAssigneeDisplayName() {
        if (assigneeDisplayName == null){
            return "";
        }
        return assigneeDisplayName;
    }

    public void setAssigneeDisplayName(String assigneeDisplayName) {
        this.assigneeDisplayName = assigneeDisplayName;
    }

    public String getAssigneeDisplayHName() {
        return assigneeDisplayHName;
    }

    public void setAssigneeDisplayHName(String assigneeDisplayHName) {
        this.assigneeDisplayHName = assigneeDisplayHName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserHName() {
        return createUserHName;
    }

    public void setCreateUserHName(String createUserHName) {
        this.createUserHName = createUserHName;
    }

    public double getEstimatedExecutionTime() {
        return estimatedExecutionTime;
    }

    public void setEstimatedExecutionTime(double estimatedExecutionTime) {
        this.estimatedExecutionTime = estimatedExecutionTime;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getHistoryID() {
        return historyID;
    }

    public void setHistoryID(int historyID) {
        this.historyID = historyID;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getTaskCreateUser() {
        return taskCreateUser;
    }

    public void setTaskCreateUser(int taskCreateUser) {
        this.taskCreateUser = taskCreateUser;
    }

    public int getAssignee() {
        return assignee;
    }

    public void setAssignee(int assignee) {
        this.assignee = assignee;
    }

    public String getTaskCreateDate() {
        return taskCreateDate;
    }

    public void setTaskCreateDate(String taskCreateDate) {
        this.taskCreateDate = taskCreateDate;
    }

    public String getTaskStartTimeTarget() {
        if (taskStartTimeTarget == null) {
            return "";
        }
        return taskStartTimeTarget;
    }

    public void setTaskStartTimeTarget(String taskStartTimeTarget) {
        this.taskStartTimeTarget = taskStartTimeTarget;
    }

    public String getTaskEndTimeTarget() {
        if (taskEndTimeTarget == null) {
            return "";
        }
        return taskEndTimeTarget;
    }

    public void setTaskEndTimeTarget(String taskEndTimeTarget) {
        this.taskEndTimeTarget = taskEndTimeTarget;
    }

    public String getSubjectTrans() {
        return subjectTrans;
    }

    public void setSubjectTrans(String subjectTrans) {
        this.subjectTrans = subjectTrans;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTaskPriorityTrans() {
        return taskPriorityTrans;
    }

    public void setTaskPriorityTrans(String taskPriorityTrans) {
        this.taskPriorityTrans = taskPriorityTrans;
    }

    public int getTaskPriorityID() {
        return taskPriorityID;
    }

    public void setTaskPriorityID(int taskPriorityID) {
        this.taskPriorityID = taskPriorityID;
    }

    public String getHistoryDisplayHName() {
        return historyDisplayHName;
    }

    public void setHistoryDisplayHName(String historyDisplayHName) {
        this.historyDisplayHName = historyDisplayHName;
    }

    public String getHistoryDisplayName() {
        if (historyDisplayName == null) {
            return "";
        }
        return historyDisplayName;
    }

    public void setHistoryDisplayName(String historyDisplayName) {
        this.historyDisplayName = historyDisplayName;
    }

    public String getHistoryUser() {
        return historyUser;
    }

    public void setHistoryUser(String historyUser) {
        this.historyUser = historyUser;
    }

    public String getHistoryCreateDate() {
        return historyCreateDate;
    }

    public void setHistoryCreateDate(String historyCreateDate) {
        this.historyCreateDate = historyCreateDate;
    }

    public int getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(int taskLevel) {
        this.taskLevel = taskLevel;
    }

    public int getStringID() {
        return StringID;
    }

    public void setStringID(int StringID) {
        this.StringID = StringID;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getEName() {
        return eName;
    }

    public void setEName(String eName) {
        this.eName = eName;
    }

    public String getAssigneDisplayName2Chars() {
        if (assigneeDisplayName.contains(" ")) {
            String[] strings = assigneeDisplayName.split(" ");
            String result = "";
            if (strings[0] != null && strings[0].length() > 0) {
                result += String.valueOf(strings[0].charAt(0)).toUpperCase();
            }
            if (strings[1] != null && strings[1].length() > 0) {
                result += String.valueOf(strings[1].charAt(0)).toUpperCase();
            }
            return result;
        } else {
            return String.valueOf(assigneeDisplayName.charAt(0));
        }
    }

    public boolean isCriticalState() {

        int status = taskStatus;
        long timeStamp = new Date().getTime();
        long taskStartTime = TimeUtils.convertDateToMillisecond(taskStartTimeTarget, TimeUtils.SQL_T_FORMAT_NO_SECOND);
        long taskEndTime = TimeUtils.convertDateToMillisecond(taskEndTimeTarget, TimeUtils.SQL_T_FORMAT_NO_SECOND);
        switch (status) {
            case 2:
                if (taskStartTime != 0 && taskStartTime <= timeStamp) {
                    return true;
                }
                break;
            case 3:
                if (taskEndTime != 0 && taskEndTime <= timeStamp) {
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.taskID);
        dest.writeInt(this.historyID);
        dest.writeInt(this.taskStatus);
        dest.writeInt(this.taskCreateUser);
        dest.writeInt(this.assignee);
        dest.writeString(this.taskCreateDate);
        dest.writeString(this.taskStartTimeTarget);
        dest.writeString(this.taskEndTimeTarget);
        dest.writeString(this.subjectTrans);
        dest.writeString(this.text);
        dest.writeString(this.taskPriorityTrans);
        dest.writeInt(this.taskPriorityID);
        dest.writeString(this.historyDisplayHName);
        dest.writeString(this.historyDisplayName);
        dest.writeString(this.historyUser);
        dest.writeString(this.historyCreateDate);
        dest.writeInt(this.taskLevel);
        dest.writeInt(this.StringID);
        dest.writeString(this.lName);
        dest.writeString(this.eName);
    }

    protected TaskProgress(Parcel in) {
        this.taskID = in.readInt();
        this.historyID = in.readInt();
        this.taskStatus = in.readInt();
        this.taskCreateUser = in.readInt();
        this.assignee = in.readInt();
        this.taskCreateDate = in.readString();
        this.taskStartTimeTarget = in.readString();
        this.taskEndTimeTarget = in.readString();
        this.subjectTrans = in.readString();
        this.text = in.readString();
        this.taskPriorityTrans = in.readString();
        this.taskPriorityID = in.readInt();
        this.historyDisplayHName = in.readString();
        this.historyDisplayName = in.readString();
        this.historyUser = in.readString();
        this.historyCreateDate = in.readString();
        this.taskLevel = in.readInt();
        this.StringID = in.readInt();
        this.lName = in.readString();
        this.eName = in.readString();
    }

    public static final Parcelable.Creator<TaskProgress> CREATOR = new Parcelable.Creator<TaskProgress>() {
        @Override
        public TaskProgress createFromParcel(Parcel source) {
            return new TaskProgress(source);
        }

        @Override
        public TaskProgress[] newArray(int size) {
            return new TaskProgress[size];
        }
    };
}
