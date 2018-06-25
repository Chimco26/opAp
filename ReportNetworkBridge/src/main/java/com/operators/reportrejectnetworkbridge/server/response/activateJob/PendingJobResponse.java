package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;
import android.service.autofill.Dataset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PendingJobResponse extends Response implements Parcelable{

    @SerializedName("PandingJobs")
    @Expose
    private List<PandingJob> pandingJobs = null;
    @SerializedName("Headers")
    @Expose
    private List<Header> headers = null;

    public List<PandingJob> getPandingJobs() {
        return pandingJobs;
    }

    public void setPandingJobs(List<PandingJob> pandingJobs) {
        this.pandingJobs = pandingJobs;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.pandingJobs);
        dest.writeList(this.headers);
    }

    public PendingJobResponse() {

    }

    protected PendingJobResponse(Parcel in) {
        this.pandingJobs = new ArrayList<PandingJob>();
        in.readList(this.pandingJobs, PandingJob.class.getClassLoader());
        this.headers = new ArrayList<Header>();
        in.readList(this.headers, Header.class.getClassLoader());
    }

    public static final Creator<PendingJobResponse> CREATOR = new Creator<PendingJobResponse>() {
        @Override
        public PendingJobResponse createFromParcel(Parcel source) {
            return new PendingJobResponse(source);
        }

        @Override
        public PendingJobResponse[] newArray(int size) {
            return new PendingJobResponse[size];
        }
    };
}
