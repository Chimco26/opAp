package com.operators.reportrejectnetworkbridge.server.response.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class ChannelSplits{
  @SerializedName("SplitNumber")
  @Expose
  private Integer SplitNumber;
  @SerializedName("BaseSplits")
  @Expose
  private List<BaseSplits> BaseSplits;
  @SerializedName("MaterialInformation")
  @Expose
  private Object MaterialInformation;
  @SerializedName("Name")
  @Expose
  private String Name;
  public ChannelSplits(){
  }
  public ChannelSplits(Integer SplitNumber,List<BaseSplits> BaseSplits,Object MaterialInformation,String Name){
   this.SplitNumber=SplitNumber;
   this.BaseSplits=BaseSplits;
   this.MaterialInformation=MaterialInformation;
   this.Name=Name;
  }
  public void setSplitNumber(Integer SplitNumber){
   this.SplitNumber=SplitNumber;
  }
  public Integer getSplitNumber(){
   return SplitNumber;
  }
  public void setBaseSplits(List<BaseSplits> BaseSplits){
   this.BaseSplits=BaseSplits;
  }
  public List<BaseSplits> getBaseSplits(){
   return BaseSplits;
  }
  public void setMaterialInformation(Object MaterialInformation){
   this.MaterialInformation=MaterialInformation;
  }
  public Object getMaterialInformation(){
   return MaterialInformation;
  }
  public void setName(String Name){
   this.Name=Name;
  }
  public String getName(){
   return Name;
  }
}