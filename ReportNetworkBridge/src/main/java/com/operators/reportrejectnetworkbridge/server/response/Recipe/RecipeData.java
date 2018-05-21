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
public class RecipeData implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.ChannelNumber);
        dest.writeList(this.channelSplits);
        dest.writeString(this.Name);
    }

    protected RecipeData(Parcel in) {
        this.ChannelNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.channelSplits = new ArrayList<ChannelSplits>();
        in.readList(this.channelSplits, ChannelSplits.class.getClassLoader());
        this.Name = in.readString();
    }

    public static final Parcelable.Creator<RecipeData> CREATOR = new Parcelable.Creator<RecipeData>() {
        @Override
        public RecipeData createFromParcel(Parcel source) {
            return new RecipeData(source);
        }

        @Override
        public RecipeData[] newArray(int size) {
            return new RecipeData[size];
        }
    };
}