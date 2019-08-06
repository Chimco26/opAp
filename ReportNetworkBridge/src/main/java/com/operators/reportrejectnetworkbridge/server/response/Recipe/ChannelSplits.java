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
  @SerializedName("splits")
  @Expose
  private List<BaseSplits> BaseSplits;
  @SerializedName("EName")
  @Expose
  private String eName;
  @SerializedName("LName")
  @Expose
  private String lName;
  public ChannelSplits(){
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
    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void seteName(String Name){
   this.eName =Name;
  }
  public String geteName(){
   return eName;
  }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.SplitNumber);
        dest.writeList(this.BaseSplits);
//        dest.writeParcelable(this.materialInformation, flags);
        dest.writeString(this.eName);
        dest.writeString(this.lName);
    }

    protected ChannelSplits(Parcel in) {
        this.SplitNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.BaseSplits = new ArrayList<com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits>();
        in.readList(this.BaseSplits, com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits.class.getClassLoader());
//        this.materialInformation = in.readParcelable(MaterialInformation.class.getClassLoader());
        this.eName = in.readString();
        this.lName = in.readString();
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