package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Header implements Parcelable {

    public static String TAG = Header.class.getSimpleName();

    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Order")
    @Expose
    private Integer order;
    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("ShowOnHeader")
    @Expose
    private Boolean showOnHeader;

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean isShowOnHeader() {
        return showOnHeader;
    }

    public void setShowOnHeader(Boolean showOnHeader) {
        this.showOnHeader = showOnHeader;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.displayName);
        dest.writeString(this.name);
        dest.writeValue(this.order);
        dest.writeString(this.color);
        dest.writeValue(this.showOnHeader);
    }

    public Header() {
    }

    public Header(String displayName, int order) {
        this.displayName = displayName;
        this.order = order;
    }

    protected Header(Parcel in) {
        this.displayName = in.readString();
        this.name = in.readString();
        this.order = (Integer) in.readValue(Integer.class.getClassLoader());
        this.color = in.readString();
        this.showOnHeader = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<Header> CREATOR = new Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel source) {
            return new Header(source);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };
}
