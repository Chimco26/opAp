package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Action implements Parcelable {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("IsSelected")
    @Expose
    private Boolean isSelected;
    @SerializedName("Notes")
    @Expose
    private String notes;
    @SerializedName("Order")
    @Expose
    private Integer order;
    @SerializedName("Text")
    @Expose
    private String text;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Object getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.iD);
        dest.writeValue(this.isSelected);
        dest.writeString(this.notes);
        dest.writeValue(this.order);
        dest.writeString(this.text);
    }

    public Action() {
    }

    protected Action(Parcel in) {
        this.iD = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isSelected = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.notes = in.readString();
        this.order = (Integer) in.readValue(Integer.class.getClassLoader());
        this.text = in.readString();
    }

    public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel source) {
            return new Action(source);
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };
}
