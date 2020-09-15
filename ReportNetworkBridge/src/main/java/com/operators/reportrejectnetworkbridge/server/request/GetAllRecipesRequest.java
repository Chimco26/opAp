package com.operators.reportrejectnetworkbridge.server.request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class GetAllRecipesRequest{
  @SerializedName("SessionID")
  @Expose
  private String SessionID;
  @SerializedName("JobID")
  @Expose
  private Integer JobID;
  public GetAllRecipesRequest(){
  }
  public GetAllRecipesRequest(String SessionID,Integer JobID){
   this.SessionID=SessionID;
   this.JobID=JobID;
  }
//  public void setSessionID(String SessionID){
//   this.SessionID=SessionID;
//  }
//  public String getSessionID(){
//   return SessionID;
//  }
  public void setJobID(Integer JobID){
   this.JobID=JobID;
  }
  public Integer getJobID(){
   return JobID;
  }
}