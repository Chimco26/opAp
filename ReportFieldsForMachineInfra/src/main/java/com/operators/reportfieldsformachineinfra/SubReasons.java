package com.operators.reportfieldsformachineinfra;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 02/08/2016.
 */
public class SubReasons implements Parcelable {

    @SerializedName("ID")
    private int id;
    @SerializedName("EName")
    private String EName;
    @SerializedName("LName")
    private String LName;
    @SerializedName("EventGroupIconID")
    private String EventIconID;
    @SerializedName("EventOpAppDisplayOrder")
    private int EventOpAppDisplayOrder;
    @SerializedName("SubReason")
    private SubReasons[] subReasons;

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

    public SubReasons[] getSubReasons()
    {
        return subReasons;
    }

    public String getEventIconID() {
        if (EventIconID == null){
            return "";
        }
        return EventIconID.replace("-","_");
    }

    public int getEventOpAppDisplayOrder() {
        return EventOpAppDisplayOrder;
    }

    public int getEventIcon(Context context){
        int icon = context.getResources().getIdentifier(getEventIconID(), "drawable", context.getPackageName());
//        if (icon == 0){
//            icon = context.getResources().getIdentifier("rejects_copy_5", "drawable", context.getPackageName());
//        }
        return icon;
    }


    public SubReasons() {
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
        dest.writeString(this.EventIconID);
        dest.writeInt(this.EventOpAppDisplayOrder);
        dest.writeTypedArray(this.subReasons, flags);
    }

    protected SubReasons(Parcel in) {
        this.id = in.readInt();
        this.EName = in.readString();
        this.LName = in.readString();
        this.EventIconID = in.readString();
        this.EventOpAppDisplayOrder = in.readInt();
        this.subReasons = in.createTypedArray(SubReasons.CREATOR);
    }

    public static final Creator<SubReasons> CREATOR = new Creator<SubReasons>() {
        @Override
        public SubReasons createFromParcel(Parcel source) {
            return new SubReasons(source);
        }

        @Override
        public SubReasons[] newArray(int size) {
            return new SubReasons[size];
        }
    };
}
