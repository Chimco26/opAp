package com.operators.reportrejectnetworkbridge.server.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class RecipeResponse{
  @SerializedName("ProductData")
  @Expose
  private ProductData ProductData;
  @SerializedName("LeaderRecordID")
  @Expose
  private Integer LeaderRecordID;
  @SerializedName("error")
  @Expose
  private Object error;
  @SerializedName("FunctionSucceed")
  @Expose
  private Boolean FunctionSucceed;
  @SerializedName("RecipeData")
  @Expose
  private List<RecipeData> RecipeData;
  public RecipeResponse(){
  }
  public RecipeResponse(ProductData ProductData,Integer LeaderRecordID,Object error,Boolean FunctionSucceed,List<RecipeData> RecipeData){
   this.ProductData=ProductData;
   this.LeaderRecordID=LeaderRecordID;
   this.error=error;
   this.FunctionSucceed=FunctionSucceed;
   this.RecipeData=RecipeData;
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
  public void setError(Object error){
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
}