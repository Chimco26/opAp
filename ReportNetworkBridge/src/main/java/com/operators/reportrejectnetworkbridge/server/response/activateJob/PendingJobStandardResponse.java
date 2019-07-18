package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.example.common.StandardResponse;

import java.util.ArrayList;
import java.util.List;

public class PendingJobStandardResponse extends StandardResponse implements Parcelable{

    public static final String TAG = PendingJobStandardResponse.class.getSimpleName();
    @SerializedName("PandingJobs")
    @Expose
    private List<PendingJob> pendingJobs = null;
    @SerializedName("Headers")
    @Expose
    private List<Header> headers = null;

    public List<PendingJob> getPendingJobs() {
        return pendingJobs;
    }

    public void setPendingJobs(List<PendingJob> pendingJobs) {
        this.pendingJobs = pendingJobs;
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
        dest.writeList(this.pendingJobs);
        dest.writeList(this.headers);
    }

    public PendingJobStandardResponse() {

    }

    protected PendingJobStandardResponse(Parcel in) {
        this.pendingJobs = new ArrayList<PendingJob>();
        in.readList(this.pendingJobs, PendingJob.class.getClassLoader());
        this.headers = new ArrayList<Header>();
        in.readList(this.headers, Header.class.getClassLoader());
    }

    public static final Creator<PendingJobStandardResponse> CREATOR = new Creator<PendingJobStandardResponse>() {
        @Override
        public PendingJobStandardResponse createFromParcel(Parcel source) {
            return new PendingJobStandardResponse(source);
        }

        @Override
        public PendingJobStandardResponse[] newArray(int size) {
            return new PendingJobStandardResponse[size];
        }
    };
}
