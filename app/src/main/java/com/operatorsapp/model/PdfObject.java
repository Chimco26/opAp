package com.operatorsapp.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class PdfObject implements Parcelable {

    private Uri uri;

    private String url;

    public PdfObject(Uri uri, String url) {
        this.uri = uri;
        this.url = url;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.uri, flags);
        dest.writeString(this.url);
    }

    private PdfObject(Parcel in) {
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.url = in.readString();
    }

    public static final Parcelable.Creator<PdfObject> CREATOR = new Parcelable.Creator<PdfObject>() {
        @Override
        public PdfObject createFromParcel(Parcel source) {
            return new PdfObject(source);
        }

        @Override
        public PdfObject[] newArray(int size) {
            return new PdfObject[size];
        }
    };
}
