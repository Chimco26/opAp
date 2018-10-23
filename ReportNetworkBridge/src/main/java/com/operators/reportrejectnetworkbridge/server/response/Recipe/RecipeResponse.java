package com.operators.reportrejectnetworkbridge.server.response.Recipe;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponse;

import java.util.ArrayList;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
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
    @SerializedName("FunctionSucceed")
    @Expose
    private Boolean FunctionSucceed;
    @SerializedName("RecipeData")
    @Expose
    private List<RecipeData> RecipeData;
    @SerializedName("JobNotes")
    @Expose
    private String note = "";

    public RecipeResponse(){
    }
    public RecipeResponse(ProductData ProductData,Integer LeaderRecordID,ErrorResponse error,Boolean FunctionSucceed,List<RecipeData> RecipeData, String note){
     this.ProductData=ProductData;
     this.LeaderRecordID=LeaderRecordID;
     this.error=error;
     this.FunctionSucceed=FunctionSucceed;
     this.RecipeData=RecipeData;
     this.note=note;
    }
    public void setProductData(ProductData ProductData){
     this.ProductData=ProductData;
    }
    public ProductData getProductData(){
     return ProductData;
    }
    public void setLeaderRecordID(Integer LeaderRecordID){
     this.LeaderRecordID=LeaderRecordID;
    }
    public Integer getLeaderRecordID(){
     return LeaderRecordID;
    }
    public void setError(ErrorResponse error){
     this.error=error;
    }
    public Object getError(){
     return error;
    }
    public void setFunctionSucceed(Boolean FunctionSucceed){
     this.FunctionSucceed=FunctionSucceed;
    }
    public Boolean getFunctionSucceed(){
     return FunctionSucceed;
    }
    public void setRecipeData(List<RecipeData> RecipeData){
     this.RecipeData=RecipeData;
    }
    public List<RecipeData> getRecipeData(){
     return RecipeData;
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
        dest.writeValue(this.FunctionSucceed);
        dest.writeList(this.RecipeData);
        dest.writeValue(this.note);
    }

    protected RecipeResponse(Parcel in) {
        this.ProductData = in.readParcelable(com.operators.reportrejectnetworkbridge.server.response.Recipe.ProductData.class.getClassLoader());
        this.LeaderRecordID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.error = in.readParcelable(Object.class.getClassLoader());
        this.FunctionSucceed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.note = (String) in.readValue(String.class.getClassLoader());
        this.RecipeData = new ArrayList<com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeData>();
        in.readList(this.RecipeData, com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeData.class.getClassLoader());
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
}