package com.operators.reportrejectnetworkbridge.server.response.Recipe;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class ProductData implements Parcelable {
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
  public ProductData(List<String> FileUrl, Integer ID,String Name){
   this.FileUrl=FileUrl;
   this.ID=ID;
   this.Name=Name;
  }
  public void setFileUrl(List<String> FileUrl){
   this.FileUrl=FileUrl;
  }
  public List<String> getFileUrl(){
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.FileUrl);
        dest.writeValue(this.ID);
        dest.writeString(this.Name);
    }

    protected ProductData(Parcel in) {
        this.FileUrl = in.createStringArrayList();
        this.ID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Name = in.readString();
    }

    public static final Parcelable.Creator<ProductData> CREATOR = new Parcelable.Creator<ProductData>() {
        @Override
        public ProductData createFromParcel(Parcel source) {
            return new ProductData(source);
        }

        @Override
        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };
}