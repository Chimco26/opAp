package com.operators.reportrejectnetworkbridge.server.response.Recipe;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class BaseSplits implements Parcelable {
  @SerializedName("PropertyName")
  @Expose
  private String PropertyName;
  @SerializedName("DisplayOrder")
  @Expose
  private Integer DisplayOrder;
  @SerializedName("FValue")
  @Expose
  private Float FValue;
  @SerializedName("Range")
  @Expose
  private String Range;
  public BaseSplits(){
  }
  public BaseSplits(String PropertyName,Integer DisplayOrder,Float FValue,String Range){
   this.PropertyName=PropertyName;
   this.DisplayOrder=DisplayOrder;
   this.FValue=FValue;
   this.Range=Range;
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
  public void setFValue(Float FValue){
   this.FValue=FValue;
  }
  public Float getFValue(){
   return FValue;
  }
  public void setRange(String Range){
   this.Range=Range;
  }
  public String getRange(){
   return Range;
  }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PropertyName);
        dest.writeValue(this.DisplayOrder);
        dest.writeValue(this.FValue);
        dest.writeString(this.Range);
    }

    protected BaseSplits(Parcel in) {
        this.PropertyName = in.readString();
        this.DisplayOrder = (Integer) in.readValue(Integer.class.getClassLoader());
        this.FValue = (Float) in.readValue(Float.class.getClassLoader());
        this.Range = in.readString();
    }

    public static final Parcelable.Creator<BaseSplits> CREATOR = new Parcelable.Creator<BaseSplits>() {
        @Override
        public BaseSplits createFromParcel(Parcel source) {
            return new BaseSplits(source);
        }

        @Override
        public BaseSplits[] newArray(int size) {
            return new BaseSplits[size];
        }
    };
}