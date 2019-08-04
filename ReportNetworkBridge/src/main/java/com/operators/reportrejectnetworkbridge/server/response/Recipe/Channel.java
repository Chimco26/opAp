package com.operators.reportrejectnetworkbridge.server.response.Recipe;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Channel implements Parcelable {

    @SerializedName("ChannelNumber")
    @Expose
    private Integer ChannelNumber;
    @SerializedName("channelSplit")
    @Expose
    private List<ChannelSplits> channelSplits;
    @SerializedName("ChannelEname")
    @Expose
    private String channelEname;
    @SerializedName("ChannelLname")
    @Expose
    private String channelLname;
    @SerializedName("inRecipeMatrix")
    @Expose
    private Boolean inRecipeMatrix;

    public Channel(){}
    public Channel(Integer channelNumber, List<ChannelSplits> channelSplits, String channelEname, String channelLname, Boolean inRecipeMatrix) {
        ChannelNumber = channelNumber;
        this.channelSplits = channelSplits;
        this.channelEname = channelEname;
        this.channelLname = channelLname;
        this.inRecipeMatrix = inRecipeMatrix;
    }

    public Integer getChannelNumber() {
        return ChannelNumber;
    }

    public void setChannelNumber(Integer channelNumber) {
        ChannelNumber = channelNumber;
    }

    public List<ChannelSplits> getChannelSplits() {
        return channelSplits;
    }

    public void setChannelSplits(List<ChannelSplits> channelSplits) {
        this.channelSplits = channelSplits;
    }

    public String getChannelEname() {
        return channelEname;
    }

    public void setChannelEname(String channelEname) {
        this.channelEname = channelEname;
    }

    public String getChannelLname() {
        return channelLname;
    }

    public void setChannelLname(String channelLname) {
        this.channelLname = channelLname;
    }

    public Boolean getInRecipeMatrix() {
        return inRecipeMatrix;
    }

    public void setInRecipeMatrix(Boolean inRecipeMatrix) {
        this.inRecipeMatrix = inRecipeMatrix;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.ChannelNumber);
        dest.writeTypedList(this.channelSplits);
        dest.writeString(this.channelEname);
        dest.writeString(this.channelLname);
        dest.writeValue(this.inRecipeMatrix);
    }

    protected Channel(Parcel in) {
        this.ChannelNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.channelSplits = in.createTypedArrayList(ChannelSplits.CREATOR);
        this.channelEname = in.readString();
        this.channelLname = in.readString();
        this.inRecipeMatrix = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Channel> CREATOR = new Parcelable.Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel source) {
            return new Channel(source);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };
}
