package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestSampleFieldsDatum {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("SubType")
    @Expose
    private Integer subType;
    @SerializedName("LName")
    @Expose
    private String lName;
    @SerializedName("EName")
    @Expose
    private String eName;
    @SerializedName("DisplayOrder")
    @Expose
    private Integer displayOrder;
    @SerializedName("FieldType")
    @Expose
    private String fieldType;
    @SerializedName("InputType")
    @Expose
    private Integer inputType;
    @SerializedName("InputField")
    @Expose
    private String inputField;
    @SerializedName("PropertyID")
    @Expose
    private String propertyID;
    @SerializedName("AllowNull")
    @Expose
    private Boolean allowNull;
    @SerializedName("AutoUpdateProperty")
    @Expose
    private Boolean autoUpdateProperty;
    @SerializedName("LValue")
    @Expose
    private String lValue;
    @SerializedName("HValue")
    @Expose
    private String hValue;
    @SerializedName("BeginNewLine")
    @Expose
    private Boolean beginNewLine;
    @SerializedName("FName")
    @Expose
    private String fName;
    @SerializedName("CurrentValue")
    @Expose
    private Object currentValue;
    @SerializedName("AllowEntry")
    @Expose
    private Boolean allowEntry;
    @SerializedName("RequiredField")
    @Expose
    private Boolean requiredField;
    @SerializedName("UpsertType")
    @Expose
    private Integer upsertType;
    @SerializedName("SamplesData")
    @Expose
    private List<SamplesDatum> samplesData = null;
    private boolean failed;


    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }
    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getInputType() {
        return inputType;
    }

    public void setInputType(Integer inputType) {
        this.inputType = inputType;
    }

    public String getInputField() {
        return inputField;
    }

    public void setInputField(String inputField) {
        this.inputField = inputField;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public Boolean getAllowNull() {
        return allowNull;
    }

    public void setAllowNull(Boolean allowNull) {
        this.allowNull = allowNull;
    }

    public Boolean getAutoUpdateProperty() {
        return autoUpdateProperty;
    }

    public void setAutoUpdateProperty(Boolean autoUpdateProperty) {
        this.autoUpdateProperty = autoUpdateProperty;
    }

    public String getLValue() {
        return lValue;
    }

    public void setLValue(String lValue) {
        this.lValue = lValue;
    }

    public String getHValue() {
        return hValue;
    }

    public void setHValue(String hValue) {
        this.hValue = hValue;
    }

    public Boolean getBeginNewLine() {
        return beginNewLine;
    }

    public void setBeginNewLine(Boolean beginNewLine) {
        this.beginNewLine = beginNewLine;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public Object getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Object currentValue) {
        this.currentValue = currentValue;
    }

    public Boolean getAllowEntry() {
        return allowEntry;
    }

    public void setAllowEntry(Boolean allowEntry) {
        this.allowEntry = allowEntry;
    }

    public Boolean getRequiredField() {
        return requiredField;
    }

    public void setRequiredField(Boolean requiredField) {
        this.requiredField = requiredField;
    }

    public Integer getUpsertType() {
        if (upsertType == null){
            return 0;
        }
        return upsertType;
    }

    public void setUpsertType(Integer upsertType) {
        this.upsertType = upsertType;
    }

    public List<SamplesDatum> getSamplesData() {
        return samplesData;
    }

    public void setSamplesData(List<SamplesDatum> samplesData) {
        this.samplesData = samplesData;
    }
}
