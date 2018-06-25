package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Material implements Parcelable {


    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("AmountUnits")
    @Expose
    private Integer amountUnits;
    @SerializedName("Catalog")
    @Expose
    private String catalog;
    @SerializedName("Name")
    @Expose
    private String name;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getAmountUnits() {
        return amountUnits;
    }

    public void setAmountUnits(Integer amountUnits) {
        this.amountUnits = amountUnits;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.amount);
        dest.writeValue(this.amountUnits);
        dest.writeString(this.catalog);
        dest.writeString(this.name);
    }

    public Material() {
    }

    protected Material(Parcel in) {
        this.amount = in.readString();
        this.amountUnits = (Integer) in.readValue(Integer.class.getClassLoader());
        this.catalog = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Material> CREATOR = new Parcelable.Creator<Material>() {
        @Override
        public Material createFromParcel(Parcel source) {
            return new Material(source);
        }

        @Override
        public Material[] newArray(int size) {
            return new Material[size];
        }
    };
}
