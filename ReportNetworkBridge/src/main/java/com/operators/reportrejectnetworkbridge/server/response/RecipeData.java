package com.operators.reportrejectnetworkbridge.server.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.operators.reportrejectnetworkbridge.server.response.ChannelSplits;

import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class RecipeData{
  @SerializedName("ChannelNumber")
  @Expose
  private Integer ChannelNumber;
  @SerializedName("channelSplits")
  @Expose
  private List<ChannelSplits> channelSplits;
  @SerializedName("Name")
  @Expose
  private String Name;
  public RecipeData(){
  }
  public RecipeData(Integer ChannelNumber,List<ChannelSplits> channelSplits,String Name){
   this.ChannelNumber=ChannelNumber;
   this.channelSplits=channelSplits;
   this.Name=Name;
  }
  public void setChannelNumber(Integer ChannelNumber){
   this.ChannelNumber=ChannelNumber;
  }
  public Integer getChannelNumber(){
   return ChannelNumber;
  }
  public void setChannelSplits(List<ChannelSplits> channelSplits){
   this.channelSplits=channelSplits;
  }
  public List<ChannelSplits> getChannelSplits(){
   return channelSplits;
  }
  public void setName(String Name){
   this.Name=Name;
  }
  public String getName(){
   return Name;
  }
}