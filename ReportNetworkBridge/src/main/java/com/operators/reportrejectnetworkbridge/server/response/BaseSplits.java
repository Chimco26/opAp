package com.operators.reportrejectnetworkbridge.server.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class BaseSplits{
  @SerializedName("FileUrl")
  @Expose
  private Object FileUrl;
  @SerializedName("MaterialCatalogID")
  @Expose
  private Object MaterialCatalogID;
  @SerializedName("PropertyName")
  @Expose
  private String PropertyName;
  @SerializedName("DisplayOrder")
  @Expose
  private Integer DisplayOrder;
  @SerializedName("FValue")
  @Expose
  private Double FValue;
  @SerializedName("MaterialPCRange")
  @Expose
  private String MaterialPCRange;
  @SerializedName("MaterialName")
  @Expose
  private Object MaterialName;
  public BaseSplits(){
  }
  public BaseSplits(Object FileUrl,Object MaterialCatalogID,String PropertyName,Integer DisplayOrder,Double FValue,String MaterialPCRange,Object MaterialName){
   this.FileUrl=FileUrl;
   this.MaterialCatalogID=MaterialCatalogID;
   this.PropertyName=PropertyName;
   this.DisplayOrder=DisplayOrder;
   this.FValue=FValue;
   this.MaterialPCRange=MaterialPCRange;
   this.MaterialName=MaterialName;
  }
  public void setFileUrl(Object FileUrl){
   this.FileUrl=FileUrl;
  }
  public Object getFileUrl(){
   return FileUrl;
  }
  public void setMaterialCatalogID(Object MaterialCatalogID){
   this.MaterialCatalogID=MaterialCatalogID;
  }
  public Object getMaterialCatalogID(){
   return MaterialCatalogID;
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
  public void setFValue(Double FValue){
   this.FValue=FValue;
  }
  public Double getFValue(){
   return FValue;
  }
  public void setMaterialPCRange(String MaterialPCRange){
   this.MaterialPCRange=MaterialPCRange;
  }
  public String getMaterialPCRange(){
   return MaterialPCRange;
  }
  public void setMaterialName(Object MaterialName){
   this.MaterialName=MaterialName;
  }
  public Object getMaterialName(){
   return MaterialName;
  }
}