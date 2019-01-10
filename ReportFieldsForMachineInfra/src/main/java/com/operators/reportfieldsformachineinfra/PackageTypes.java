package com.operators.reportfieldsformachineinfra;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 14/08/2016.
 */
public class PackageTypes implements Parcelable {
    @SerializedName("ID")
    private int id;
    @SerializedName("EName")
    private String EName;
    @SerializedName("LName")
    private String LName;

    public int getId() {
        return id;
    }

    public String getEName() {
        return EName;
    }

    public String getLName()
    {
        return LName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.EName);
        dest.writeString(this.LName);
    }

    public PackageTypes() {
    }

    protected PackageTypes(Parcel in) {
        this.id = in.readInt();
        this.EName = in.readString();
        this.LName = in.readString();
    }

    public static final Parcelable.Creator<PackageTypes> CREATOR = new Parcelable.Creator<PackageTypes>() {
        @Override
        public PackageTypes createFromParcel(Parcel source) {
            return new PackageTypes(source);
        }

        @Override
        public PackageTypes[] newArray(int size) {
            return new PackageTypes[size];
        }
    };
}
