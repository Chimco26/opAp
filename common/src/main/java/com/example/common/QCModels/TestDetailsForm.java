package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TestDetailsForm {
    public static final int FIELD_TYPE_TEXT_INT = 1;
    public static final int FIELD_TYPE_COMBO_INT = 2;
    public static final int FIELD_TYPE_BOOLEAN_INT = 3;
    public static final int FIELD_TYPE_NUMBER_INT = 6;
    public static final int FIELD_TYPE_DATE_INT = 7;
    public static final int FIELD_TYPE_TIME_INT = 8;
    public static final int FIELD_TYPE_HIDDEN_INT = 12;

    @SerializedName("DisplayHName")
    @Expose
    private String displayHName;
    @SerializedName("DisplayEName")
    @Expose
    private String displayEName;
    @SerializedName("DisplayType")
    @Expose
    private Integer displayType;
    @SerializedName("DisplayTypeName")
    @Expose
    private String displayTypeName;
    @SerializedName("AllowNull")
    @Expose
    private Boolean allowNull;
    @SerializedName("AllowEntry")
    @Expose
    private Boolean allowEntry;
    @SerializedName("LinkTarget")
    @Expose
    private Object linkTarget;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("CurrentValue")
    @Expose
    private String currentValue;
    private List<StatusList> statusList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayHName() {
        return displayHName;
    }

    public void setDisplayHName(String displayHName) {
        this.displayHName = displayHName;
    }

    public String getDisplayEName() {
        return displayEName;
    }

    public void setDisplayEName(String displayEName) {
        this.displayEName = displayEName;
    }

    public Integer getDisplayType() {
        return displayType;
    }

    public void setDisplayType(Integer displayType) {
        this.displayType = displayType;
    }

    public String getDisplayTypeName() {
        return displayTypeName;
    }

    public void setDisplayTypeName(String displayTypeName) {
        this.displayTypeName = displayTypeName;
    }

    public Boolean getAllowNull() {
        return allowNull;
    }

    public void setAllowNull(Boolean allowNull) {
        this.allowNull = allowNull;
    }

    public Boolean getAllowEntry() {
        return allowEntry;
    }

    public void setAllowEntry(Boolean allowEntry) {
        this.allowEntry = allowEntry;
    }

    public Object getLinkTarget() {
        return linkTarget;
    }

    public void setLinkTarget(Object linkTarget) {
        this.linkTarget = linkTarget;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public void setComboList(List<StatusList> statusList) {
        this.statusList = statusList;
    }

    public List<StatusList> getStatusList() {
        return statusList;
    }

}
