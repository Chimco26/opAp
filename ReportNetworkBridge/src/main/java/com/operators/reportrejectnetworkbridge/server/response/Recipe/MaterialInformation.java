package com.operators.reportrejectnetworkbridge.server.response.Recipe;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class MaterialInformation implements Parcelable {
  @SerializedName("FileUrl")
  @Expose
  private List<String> FileUrl;
  @SerializedName("CatalogID")
  @Expose
  private String CatalogID;
  @SerializedName("Name")
  @Expose
  private String Name;
  public void setFileUrl(List<String> FileUrl){
   this.FileUrl=FileUrl;
  }
  public List<String> getFileUrl(){
   return FileUrl;
  }
  public void setCatalogID(String CatalogID){
   this.CatalogID=CatalogID;
  }
  public String getCatalogID(){
   return CatalogID;
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
        dest.writeString(this.CatalogID);
        dest.writeString(this.Name);
    }

    public MaterialInformation() {
    }

    protected MaterialInformation(Parcel in) {
        this.FileUrl = in.createStringArrayList();
        this.CatalogID = in.readString();
        this.Name = in.readString();
    }

    public static final Parcelable.Creator<MaterialInformation> CREATOR = new Parcelable.Creator<MaterialInformation>() {
        @Override
        public MaterialInformation createFromParcel(Parcel source) {
            return new MaterialInformation(source);
        }

        @Override
        public MaterialInformation[] newArray(int size) {
            return new MaterialInformation[size];
        }
    };
}