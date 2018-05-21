package com.operators.reportrejectnetworkbridge.server.response.Recipe;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class ChannelSplits implements Parcelable {
  @SerializedName("SplitNumber")
  @Expose
  private Integer SplitNumber;
  @SerializedName("BaseSplits")
  @Expose
  private List<BaseSplits> BaseSplits;
  @SerializedName("materialInformation")
  @Expose
  private MaterialInformation materialInformation;
  @SerializedName("Name")
  @Expose
  private String Name;
  public ChannelSplits(){
  }
  public ChannelSplits(Integer SplitNumber,List<BaseSplits> BaseSplits,MaterialInformation materialInformation,String Name){
   this.SplitNumber=SplitNumber;
   this.BaseSplits=BaseSplits;
   this.materialInformation =materialInformation;
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
  public void setMaterialInformation(MaterialInformation materialInformation){
   this.materialInformation =materialInformation;
  }
  public MaterialInformation getMaterialInformation(){
   return materialInformation;
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
        dest.writeValue(this.SplitNumber);
        dest.writeList(this.BaseSplits);
        dest.writeParcelable(this.materialInformation, flags);
        dest.writeString(this.Name);
    }

    protected ChannelSplits(Parcel in) {
        this.SplitNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.BaseSplits = new ArrayList<com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits>();
        in.readList(this.BaseSplits, com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits.class.getClassLoader());
        this.materialInformation = in.readParcelable(Object.class.getClassLoader());
        this.Name = in.readString();
    }

    public static final Parcelable.Creator<ChannelSplits> CREATOR = new Parcelable.Creator<ChannelSplits>() {
        @Override
        public ChannelSplits createFromParcel(Parcel source) {
            return new ChannelSplits(source);
        }

        @Override
        public ChannelSplits[] newArray(int size) {
            return new ChannelSplits[size];
        }
    };
}