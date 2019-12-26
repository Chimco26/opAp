package com.example.common.StopLogs;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("AlarmDismissed")
    @Expose
    private Boolean alarmDismissed;
    @SerializedName("AlarmHValue")
    @Expose
    private Integer alarmHValue;
    @SerializedName("AlarmLValue")
    @Expose
    private Integer alarmLValue;
    @SerializedName("AlarmStandardValue")
    @Expose
    private Integer alarmStandardValue;
    @SerializedName("AlarmValue")
    @Expose
    private Integer alarmValue;
    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("Descr")
    @Expose
    private String descr;
    @SerializedName("EventDefinitionID")
    @Expose
    private Integer eventDefinitionID;
    @SerializedName("EventDuration")
    @Expose
    private Integer eventDuration;
    @SerializedName("EventETitle")
    @Expose
    private String eventETitle;
    @SerializedName("EventEndTime")
    @Expose
    private String eventEndTime;
    @SerializedName("EventGroupEname")
    @Expose
    private String eventGroupEname;
    @SerializedName("EventGroupID")
    @Expose
    private Integer eventGroupID;
    @SerializedName("EventGroupLname")
    @Expose
    private String eventGroupLname;
    @SerializedName("EventID")
    @Expose
    private Integer eventID;
    @SerializedName("EventLTitle")
    @Expose
    private String eventLTitle;
    @SerializedName("EventReasonID")
    @Expose
    private Integer eventReasonID;
    @SerializedName("EventSubTitleEname")
    @Expose
    private String eventSubTitleEname;
    @SerializedName("EventSubTitleLname")
    @Expose
    private String eventSubTitleLname;
    @SerializedName("EventTime")
    @Expose
    private String eventTime;
    @SerializedName("EventTitle")
    @Expose
    private String eventTitle;
    @SerializedName("RootEventID")
    @Expose
    private Integer rootEventID;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("MachineID")
    @Expose
    private Integer machineId;
    @SerializedName("MachineName")
    @Expose
    private String machineName;
    private boolean expand;
    private boolean showSub;
    private boolean haveChild;

    public boolean isHaveChild() {
        return haveChild;
    }

    public void setHaveChild(boolean haveChild) {
        this.haveChild = haveChild;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Boolean getAlarmDismissed() {
        return alarmDismissed;
    }

    public void setAlarmDismissed(Boolean alarmDismissed) {
        this.alarmDismissed = alarmDismissed;
    }

    public Integer getAlarmHValue() {
        return alarmHValue;
    }

    public void setAlarmHValue(Integer alarmHValue) {
        this.alarmHValue = alarmHValue;
    }

    public Integer getAlarmLValue() {
        return alarmLValue;
    }

    public void setAlarmLValue(Integer alarmLValue) {
        this.alarmLValue = alarmLValue;
    }

    public Integer getAlarmStandardValue() {
        return alarmStandardValue;
    }

    public void setAlarmStandardValue(Integer alarmStandardValue) {
        this.alarmStandardValue = alarmStandardValue;
    }

    public Integer getAlarmValue() {
        return alarmValue;
    }

    public void setAlarmValue(Integer alarmValue) {
        this.alarmValue = alarmValue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Integer getEventDefinitionID() {
        return eventDefinitionID;
    }

    public void setEventDefinitionID(Integer eventDefinitionID) {
        this.eventDefinitionID = eventDefinitionID;
    }

    public Integer getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public String getEventETitle() {
        return eventETitle;
    }

    public void setEventETitle(String eventETitle) {
        this.eventETitle = eventETitle;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventGroupEname() {
        return eventGroupEname;
    }

    public void setEventGroupEname(String eventGroupEname) {
        this.eventGroupEname = eventGroupEname;
    }

    public Integer getEventGroupID() {
        return eventGroupID;
    }

    public void setEventGroupID(Integer eventGroupID) {
        this.eventGroupID = eventGroupID;
    }

    public String getEventGroupLname() {
        return eventGroupLname;
    }

    public void setEventGroupLname(String eventGroupLname) {
        this.eventGroupLname = eventGroupLname;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public String getEventLTitle() {
        return eventLTitle;
    }

    public void setEventLTitle(String eventLTitle) {
        this.eventLTitle = eventLTitle;
    }

    public Integer getEventReasonID() {
        return eventReasonID;
    }

    public void setEventReasonID(Integer eventReasonID) {
        this.eventReasonID = eventReasonID;
    }

    public String getEventSubTitleEname() {
        return eventSubTitleEname;
    }

    public void setEventSubTitleEname(String eventSubTitleEname) {
        this.eventSubTitleEname = eventSubTitleEname;
    }

    public String getEventSubTitleLname() {
        return eventSubTitleLname;
    }

    public void setEventSubTitleLname(String eventSubTitleLname) {
        this.eventSubTitleLname = eventSubTitleLname;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Integer getRootEventID() {
        return rootEventID;
    }

    public void setRootEventID(Integer rootEventID) {
        this.rootEventID = rootEventID;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public boolean isShowSub() {
        return showSub;
    }

    public void setShowSub(boolean showSub) {
        this.showSub = showSub;
    }
}
