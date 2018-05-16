package com.operators.reportrejectnetworkbridge.server.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class ProductData{
  @SerializedName("FileUrl")
  @Expose
  private List<String> FileUrl;
  @SerializedName("ID")
  @Expose
  private Integer ID;
  @SerializedName("Name")
  @Expose
  private String Name;
  public ProductData(){
  }
  public ProductData(List<String> fileUrl, Integer ID,String Name){
   this.FileUrl=fileUrl;
   this.ID=ID;
   this.Name=Name;
  }
  public Object getFileUrl(){
   return FileUrl;
  }
  public void setID(Integer ID){
   this.ID=ID;
  }
  public Integer getID(){
   return ID;
  }
  public void setName(String Name){
   this.Name=Name;
  }
  public String getName(){
   return Name;
  }
}