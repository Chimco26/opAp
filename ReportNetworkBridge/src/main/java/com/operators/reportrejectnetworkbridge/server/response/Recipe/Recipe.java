package com.operators.reportrejectnetworkbridge.server.response.Recipe;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.common.ErrorResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Awesome Pojo Generator
 */
public class Recipe implements Parcelable {
    @SerializedName("LastUpdate")
    @Expose
    private String LastUpdate;
    @SerializedName("UpdatedBy")
    @Expose
    private String UpdatedBy;
    @SerializedName("error")
    @Expose
    private ErrorResponse errorResponse;
    @SerializedName("channels")
    @Expose
    private ArrayList<Channel> channels;


    public Recipe() {
    }

    public Recipe(String lastUpdate, String updatedBy, ErrorResponse errorResponse, ArrayList<Channel> channels) {
        LastUpdate = lastUpdate;
        UpdatedBy = updatedBy;
        this.errorResponse = errorResponse;
        this.channels = channels;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        LastUpdate = lastUpdate;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.LastUpdate);
        dest.writeString(this.UpdatedBy);
        dest.writeParcelable(this.errorResponse, flags);
    }

    protected Recipe(Parcel in) {
        this.LastUpdate = in.readString();
        this.UpdatedBy = in.readString();
        this.errorResponse = in.readParcelable(ErrorResponse.class.getClassLoader());
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}