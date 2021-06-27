package com.operators.reportrejectnetworkbridge.server.response.Recipe;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.common.ErrorResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Awesome Pojo Generator
 */
public class RecipeResponse implements Parcelable {
    @SerializedName("ProductData")
    @Expose
    private ProductData ProductData;
    @SerializedName("LeaderRecordID")
    @Expose
    private Integer LeaderRecordID;
    @SerializedName("error")
    @Expose
    private ErrorResponse error;
    @SerializedName("functionSucceed")
    @Expose
    private Boolean functionSucceed;
    @SerializedName("Recipe")
    @Expose
    private com.operators.reportrejectnetworkbridge.server.response.Recipe.Recipe Recipe;
    @SerializedName("JobNotes")
    @Expose
    private String note = "";
    @SerializedName("RecipeRefStandardID")
    @Expose
    private Integer recipeRefStandardID;
    @SerializedName("RecipeRefType")
    @Expose
    private Integer recipeRefType;
    @SerializedName("CanEditRecipe")
    @Expose
    private boolean canEditRecipe;
    @SerializedName("ProductFiles")
    @Expose
    private List<String> productFiles;
    @SerializedName("ChannelSplitName")
    @Expose
    private List<ChannelSplitName> channelSplitName = null;

    public RecipeResponse() {
    }

    public RecipeResponse(ProductData ProductData, Integer LeaderRecordID, ErrorResponse error, Boolean FunctionSucceed, com.operators.reportrejectnetworkbridge.server.response.Recipe.Recipe Recipe, String note) {
        this.ProductData = ProductData;
        this.LeaderRecordID = LeaderRecordID;
        this.error = error;
        this.functionSucceed = FunctionSucceed;
        this.Recipe = Recipe;
        this.note = note;
    }

    public List<ChannelSplitName> getChannelSplitName() {
        return channelSplitName;
    }

    public boolean getCanEditRecipe() {
        return canEditRecipe;
    }

    public void setCanEditRecipe(boolean canEditRecipe) {
        this.canEditRecipe = canEditRecipe;
    }

    public Integer getRecipeRefStandardID() {
        return recipeRefStandardID;
    }

    public void setRecipeRefStandardID(Integer recipeRefStandardID) {
        this.recipeRefStandardID = recipeRefStandardID;
    }

    public Integer getRecipeRefType() {
        return recipeRefType;
    }

    public List<String> getProductFiles() {
        return productFiles;
    }

    public void setRecipeRefType(Integer recipeRefType) {
        this.recipeRefType = recipeRefType;
    }
    public void setProductData(ProductData ProductData) {
        this.ProductData = ProductData;
    }

    public ProductData getProductData() {
        return ProductData;
    }

    public void setLeaderRecordID(Integer LeaderRecordID) {
        this.LeaderRecordID = LeaderRecordID;
    }

    public Integer getLeaderRecordID() {
        return LeaderRecordID;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setFunctionSucceed(Boolean FunctionSucceed) {
        this.functionSucceed = FunctionSucceed;
    }

    public Boolean getFunctionSucceed() {
        return functionSucceed;
    }

    public void setRecipe(com.operators.reportrejectnetworkbridge.server.response.Recipe.Recipe Recipe) {
        this.Recipe = Recipe;
    }

    public com.operators.reportrejectnetworkbridge.server.response.Recipe.Recipe getRecipe() {
        return Recipe;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.ProductData, flags);
        dest.writeValue(this.LeaderRecordID);
        dest.writeParcelable(this.error, flags);
        dest.writeValue(this.functionSucceed);
        dest.writeParcelable(this.Recipe, flags);
        dest.writeString(this.note);
        dest.writeValue(this.recipeRefStandardID);
        dest.writeValue(this.recipeRefType);
        dest.writeByte(this.canEditRecipe ? (byte) 1 : (byte) 0);
    }

    protected RecipeResponse(Parcel in) {
        this.ProductData = in.readParcelable(com.operators.reportrejectnetworkbridge.server.response.Recipe.ProductData.class.getClassLoader());
        this.LeaderRecordID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.error = in.readParcelable(ErrorResponse.class.getClassLoader());
        this.functionSucceed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.Recipe = in.readParcelable(com.operators.reportrejectnetworkbridge.server.response.Recipe.Recipe.class.getClassLoader());
        this.note = in.readString();
        this.recipeRefStandardID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.recipeRefType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.canEditRecipe = in.readByte() != 0;
    }

    public static final Parcelable.Creator<RecipeResponse> CREATOR = new Parcelable.Creator<RecipeResponse>() {
        @Override
        public RecipeResponse createFromParcel(Parcel source) {
            return new RecipeResponse(source);
        }

        @Override
        public RecipeResponse[] newArray(int size) {
            return new RecipeResponse[size];
        }
    };

    public String getChannelLName(int channelNumber) {
        for (ChannelSplitName channelSplitName : channelSplitName) {
            if (channelSplitName.getKey().equals("Channel")) {
                for (Value value : channelSplitName.getValue()) {
                    if (value.getKey() == channelNumber) {
                        return value.getValue();
                    }
                }
            }
        }
        return null;
    }
}