package com.operators.reportrejectnetworkbridge.server.response.Recipe;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Awesome Pojo Generator
 */
public class BaseSplits implements Parcelable {
    @SerializedName("DisplayOrder")
    @Expose
    private Integer displayOrder;
    @SerializedName("FValue")
    @Expose
    private String fValue;
    @SerializedName("PropertyName")
    @Expose
    private String propertyName;
    @SerializedName("Range")
    @Expose
    private String range;
    @SerializedName("AllowNull")
    @Expose
    private Boolean allowNull;
    @SerializedName("CalcUpdateOption")
    @Expose
    private Integer calcUpdateOption;
    @SerializedName("CatalogID")
    @Expose
    private String catalogID;
    @SerializedName("ChannelNum")
    @Expose
    private Integer channelNum;
    @SerializedName("ComboDisplayEField")
    @Expose
    private String comboDisplayEField;
    @SerializedName("ComboDisplayHField")
    @Expose
    private String comboDisplayHField;
    @SerializedName("ComboValueField")
    @Expose
    private String comboValueField;
    @SerializedName("ComboValues")
    @Expose
    private List<Object> comboValues = null;
    @SerializedName("DisplayType")
    @Expose
    private String displayType;
    @SerializedName("FValueCalcFunction")
    @Expose
    private String fValueCalcFunction;
    @SerializedName("GroupID")
    @Expose
    private Integer groupID;
    @SerializedName("HValue")
    @Expose
    private Float hValue;
    @SerializedName("IsEditable")
    @Expose
    private Boolean isEditable;
    @SerializedName("IsEnabled")
    @Expose
    private Boolean isEnabled;
    @SerializedName("IsValid")
    @Expose
    private Boolean isValid;
    @SerializedName("LValue")
    @Expose
    private Float lValue;
    @SerializedName("MandatoryField")
    @Expose
    private Boolean mandatoryField;
    @SerializedName("MaterialEName")
    @Expose
    private String materialEName;
    @SerializedName("MaterialLName")
    @Expose
    private String materialLName;
    @SerializedName("MaterialTypeEname")
    @Expose
    private String materialTypeEname;
    @SerializedName("MaterialTypeLname")
    @Expose
    private String materialTypeLname;
    @SerializedName("MaxValidValue")
    @Expose
    private String maxValidValue;
    @SerializedName("MinValidValue")
    @Expose
    private String minValidValue;
    @SerializedName("ProductID")
    @Expose
    private Integer productID;
    @SerializedName("ProductRecipeID")
    @Expose
    private Integer productRecipeID;
    @SerializedName("PropertyEName")
    @Expose
    private String propertyEName;
    @SerializedName("PropertyHName")
    @Expose
    private String propertyHName;
    @SerializedName("PropertyID")
    @Expose
    private Integer propertyID;
    @SerializedName("RecipeFValue")
    @Expose
    private Boolean recipeFValue;
    @SerializedName("RecipeHValue")
    @Expose
    private Boolean recipeHValue;
    @SerializedName("RecipeLValue")
    @Expose
    private Boolean recipeLValue;
    @SerializedName("RoundNum")
    @Expose
    private Integer roundNum;
    @SerializedName("SearchLink")
    @Expose
    private Boolean searchLink;
    @SerializedName("SearchLinkReportID")
    @Expose
    private String searchLinkReportID;
    @SerializedName("SortOrder")
    @Expose
    private Integer sortOrder;
    @SerializedName("SplitNum")
    @Expose
    private Integer splitNum;
    @SerializedName("ToleranceUpdateOption")
    @Expose
    private Integer toleranceUpdateOption;
    @SerializedName("ToolTip")
    @Expose
    private String toolTip;
    @SerializedName("ValidateValue")
    @Expose
    private Boolean validateValue;
    @SerializedName("matrixModeID")
    @Expose
    private Integer matrixModeID;
    @SerializedName("matrixPositionID")
    @Expose
    private Integer matrixPositionID;
    private boolean isEditMode;
    private String editValue;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getFValue() {
        return fValue;
    }

    public void setFValue(String fValue) {
        this.fValue = fValue;
    }

    public String getfValue() {
        return fValue;
    }

    public void setfValue(String fValue) {
        this.fValue = fValue;
    }

    public String getfValueCalcFunction() {
        return fValueCalcFunction;
    }

    public void setfValueCalcFunction(String fValueCalcFunction) {
        this.fValueCalcFunction = fValueCalcFunction;
    }

    public Float gethValue() {
        return hValue;
    }

    public void sethValue(Float hValue) {
        this.hValue = hValue;
    }

    public Boolean getEditable() {
        return isEditable;
    }

    public void setEditable(Boolean editable) {
        isEditable = editable;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Float getlValue() {
        return lValue;
    }

    public void setlValue(Float lValue) {
        this.lValue = lValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Boolean getAllowNull() {
        return allowNull;
    }

    public void setAllowNull(Boolean allowNull) {
        this.allowNull = allowNull;
    }

    public Integer getCalcUpdateOption() {
        return calcUpdateOption;
    }

    public void setCalcUpdateOption(Integer calcUpdateOption) {
        this.calcUpdateOption = calcUpdateOption;
    }

    public Object getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public Integer getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(Integer channelNum) {
        this.channelNum = channelNum;
    }

    public String getComboDisplayEField() {
        return comboDisplayEField;
    }

    public void setComboDisplayEField(String comboDisplayEField) {
        this.comboDisplayEField = comboDisplayEField;
    }

    public String getComboDisplayHField() {
        return comboDisplayHField;
    }

    public void setComboDisplayHField(String comboDisplayHField) {
        this.comboDisplayHField = comboDisplayHField;
    }

    public String getComboValueField() {
        return comboValueField;
    }

    public void setComboValueField(String comboValueField) {
        this.comboValueField = comboValueField;
    }

    public List<Object> getComboValues() {
        return comboValues;
    }

    public void setComboValues(List<Object> comboValues) {
        this.comboValues = comboValues;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getFValueCalcFunction() {
        return fValueCalcFunction;
    }

    public void setFValueCalcFunction(String fValueCalcFunction) {
        this.fValueCalcFunction = fValueCalcFunction;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Float getHValue() {
        return hValue;
    }

    public void setHValue(Float hValue) {
        this.hValue = hValue;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public Float getLValue() {
        return lValue;
    }

    public void setLValue(Float lValue) {
        this.lValue = lValue;
    }

    public Boolean getMandatoryField() {
        return mandatoryField;
    }

    public void setMandatoryField(Boolean mandatoryField) {
        this.mandatoryField = mandatoryField;
    }

    public Object getMaterialEName() {
        return materialEName;
    }

    public void setMaterialEName(String materialEName) {
        this.materialEName = materialEName;
    }

    public String getMaterialLName() {
        return materialLName;
    }

    public void setMaterialLName(String materialLName) {
        this.materialLName = materialLName;
    }

    public Object getMaterialTypeEname() {
        return materialTypeEname;
    }

    public void setMaterialTypeEname(String materialTypeEname) {
        this.materialTypeEname = materialTypeEname;
    }

    public Object getMaterialTypeLname() {
        return materialTypeLname;
    }

    public void setMaterialTypeLname(String materialTypeLname) {
        this.materialTypeLname = materialTypeLname;
    }

    public Object getMaxValidValue() {
        return maxValidValue;
    }

    public void setMaxValidValue(String maxValidValue) {
        this.maxValidValue = maxValidValue;
    }

    public Object getMinValidValue() {
        return minValidValue;
    }

    public void setMinValidValue(String minValidValue) {
        this.minValidValue = minValidValue;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getProductRecipeID() {
        return productRecipeID;
    }

    public void setProductRecipeID(Integer productRecipeID) {
        this.productRecipeID = productRecipeID;
    }

    public String getPropertyEName() {
        return propertyEName;
    }

    public void setPropertyEName(String propertyEName) {
        this.propertyEName = propertyEName;
    }

    public String getPropertyHName() {
        return propertyHName;
    }

    public void setPropertyHName(String propertyHName) {
        this.propertyHName = propertyHName;
    }

    public Integer getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(Integer propertyID) {
        this.propertyID = propertyID;
    }

    public Boolean getRecipeFValue() {
        return recipeFValue;
    }

    public void setRecipeFValue(Boolean recipeFValue) {
        this.recipeFValue = recipeFValue;
    }

    public Boolean getRecipeHValue() {
        return recipeHValue;
    }

    public void setRecipeHValue(Boolean recipeHValue) {
        this.recipeHValue = recipeHValue;
    }

    public Boolean getRecipeLValue() {
        return recipeLValue;
    }

    public void setRecipeLValue(Boolean recipeLValue) {
        this.recipeLValue = recipeLValue;
    }

    public Integer getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(Integer roundNum) {
        this.roundNum = roundNum;
    }

    public Boolean getSearchLink() {
        return searchLink;
    }

    public void setSearchLink(Boolean searchLink) {
        this.searchLink = searchLink;
    }

    public Object getSearchLinkReportID() {
        return searchLinkReportID;
    }

    public void setSearchLinkReportID(String searchLinkReportID) {
        this.searchLinkReportID = searchLinkReportID;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getSplitNum() {
        return splitNum;
    }

    public void setSplitNum(Integer splitNum) {
        this.splitNum = splitNum;
    }

    public Integer getToleranceUpdateOption() {
        return toleranceUpdateOption;
    }

    public void setToleranceUpdateOption(Integer toleranceUpdateOption) {
        this.toleranceUpdateOption = toleranceUpdateOption;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public Boolean getValidateValue() {
        return validateValue;
    }

    public void setValidateValue(Boolean validateValue) {
        this.validateValue = validateValue;
    }

    public Integer getMatrixModeID() {
        return matrixModeID;
    }

    public void setMatrixModeID(Integer matrixModeID) {
        this.matrixModeID = matrixModeID;
    }

    public Integer getMatrixPositionID() {
        return matrixPositionID;
    }

    public void setMatrixPositionID(Integer matrixPositionID) {
        this.matrixPositionID = matrixPositionID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.displayOrder);
        dest.writeString(this.fValue);
        dest.writeString(this.propertyName);
        dest.writeString(this.range);
        dest.writeValue(this.allowNull);
        dest.writeValue(this.calcUpdateOption);
        dest.writeString(this.catalogID);
        dest.writeValue(this.channelNum);
        dest.writeString(this.comboDisplayEField);
        dest.writeString(this.comboDisplayHField);
        dest.writeString(this.comboValueField);
        dest.writeList(this.comboValues);
        dest.writeString(this.displayType);
        dest.writeString(this.fValueCalcFunction);
        dest.writeValue(this.groupID);
        dest.writeValue(this.hValue);
        dest.writeValue(this.isEditable);
        dest.writeValue(this.isEnabled);
        dest.writeValue(this.isValid);
        dest.writeValue(this.lValue);
        dest.writeValue(this.mandatoryField);
        dest.writeString(this.materialEName);
        dest.writeString(this.materialLName);
        dest.writeString(this.materialTypeEname);
        dest.writeString(this.materialTypeLname);
        dest.writeString(this.maxValidValue);
        dest.writeString(this.minValidValue);
        dest.writeValue(this.productID);
        dest.writeValue(this.productRecipeID);
        dest.writeString(this.propertyEName);
        dest.writeString(this.propertyHName);
        dest.writeValue(this.propertyID);
        dest.writeValue(this.recipeFValue);
        dest.writeValue(this.recipeHValue);
        dest.writeValue(this.recipeLValue);
        dest.writeValue(this.roundNum);
        dest.writeValue(this.searchLink);
        dest.writeString(this.searchLinkReportID);
        dest.writeValue(this.sortOrder);
        dest.writeValue(this.splitNum);
        dest.writeValue(this.toleranceUpdateOption);
        dest.writeString(this.toolTip);
        dest.writeValue(this.validateValue);
        dest.writeValue(this.matrixModeID);
        dest.writeValue(this.matrixPositionID);
    }

    public BaseSplits() {
    }

    protected BaseSplits(Parcel in) {
        this.displayOrder = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fValue = in.readString();
        this.propertyName = in.readString();
        this.range = in.readString();
        this.allowNull = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.calcUpdateOption = (Integer) in.readValue(Integer.class.getClassLoader());
        this.catalogID = in.readString();
        this.channelNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.comboDisplayEField = in.readString();
        this.comboDisplayHField = in.readString();
        this.comboValueField = in.readString();
        this.comboValues = new ArrayList<Object>();
        in.readList(this.comboValues, Object.class.getClassLoader());
        this.displayType = in.readString();
        this.fValueCalcFunction = in.readString();
        this.groupID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.hValue = (Float) in.readValue(Float.class.getClassLoader());
        this.isEditable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isEnabled = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isValid = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.lValue = (Float) in.readValue(Float.class.getClassLoader());
        this.mandatoryField = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.materialEName = in.readString();
        this.materialLName = in.readString();
        this.materialTypeEname = in.readString();
        this.materialTypeLname = in.readString();
        this.maxValidValue = in.readString();
        this.minValidValue = in.readString();
        this.productID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productRecipeID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.propertyEName = in.readString();
        this.propertyHName = in.readString();
        this.propertyID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.recipeFValue = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.recipeHValue = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.recipeLValue = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.roundNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.searchLink = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.searchLinkReportID = in.readString();
        this.sortOrder = (Integer) in.readValue(Integer.class.getClassLoader());
        this.splitNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toleranceUpdateOption = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toolTip = in.readString();
        this.validateValue = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.matrixModeID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.matrixPositionID = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<BaseSplits> CREATOR = new Parcelable.Creator<BaseSplits>() {
        @Override
        public BaseSplits createFromParcel(Parcel source) {
            return new BaseSplits(source);
        }

        @Override
        public BaseSplits[] newArray(int size) {
            return new BaseSplits[size];
        }
    };

    public void setEditValue(String string) {
        this.editValue = string;
    }

    public String getEditValue() {
        return editValue;
    }
}