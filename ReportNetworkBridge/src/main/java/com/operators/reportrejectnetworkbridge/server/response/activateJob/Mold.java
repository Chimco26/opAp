package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mold implements Parcelable {

    @SerializedName("Catalog")
    @Expose
    private String catalog;
    @SerializedName("CavitiesActual")
    @Expose
    private Double cavitiesActual;
    @SerializedName("CavitiesStandard")
    @Expose
    private Double cavitiesStandard;
    @SerializedName("Files")
    @Expose
    private List<String> files = null;
    @SerializedName("Name")
    @Expose
    private String name;

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public Double getCavitiesActual() {
        return cavitiesActual;
    }

    public void setCavitiesActual(Double cavitiesActual) {
        this.cavitiesActual = cavitiesActual;
    }

    public Double getCavitiesStandard() {
        return cavitiesStandard;
    }

    public void setCavitiesStandard(Double cavitiesStandard) {
        this.cavitiesStandard = cavitiesStandard;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
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
        dest.writeString(this.catalog);
        dest.writeValue(this.cavitiesActual);
        dest.writeValue(this.cavitiesStandard);
        dest.writeStringList(this.files);
        dest.writeString(this.name);
    }

    public Mold() {
    }

    protected Mold(Parcel in) {
        this.catalog = in.readString();
        this.cavitiesActual = (Double) in.readValue(Integer.class.getClassLoader());
        this.cavitiesStandard = (Double) in.readValue(Integer.class.getClassLoader());
        this.files = in.createStringArrayList();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Mold> CREATOR = new Parcelable.Creator<Mold>() {
        @Override
        public Mold createFromParcel(Parcel source) {
            return new Mold(source);
        }

        @Override
        public Mold[] newArray(int size) {
            return new Mold[size];
        }
    };
}
