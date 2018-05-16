package com.operators.reportrejectnetworkbridge.server.response.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class BaseSplits{
  @SerializedName("PropertyName")
  @Expose
  private String PropertyName;
  @SerializedName("DisplayOrder")
  @Expose
  private Integer DisplayOrder;
  @SerializedName("FValue")
  @Expose
  private Integer FValue;
  @SerializedName("Range")
  @Expose
  private String Range;
  public BaseSplits(){
  }
  public BaseSplits(String PropertyName,Integer DisplayOrder,Integer FValue,String Range){
   this.PropertyName=PropertyName;
   this.DisplayOrder=DisplayOrder;
   this.FValue=FValue;
   this.Range=Range;
  }
  public void setPropertyName(String PropertyName){
   this.PropertyName=PropertyName;
  }
  public String getPropertyName(){
   return PropertyName;
  }
  public void setDisplayOrder(Integer DisplayOrder){
   this.DisplayOrder=DisplayOrder;
  }
  public Integer getDisplayOrder(){
   return DisplayOrder;
  }
  public void setFValue(Integer FValue){
   this.FValue=FValue;
  }
  public Integer getFValue(){
   return FValue;
  }
  public void setRange(String Range){
   this.Range=Range;
  }
  public String getRange(){
   return Range;
  }
}