package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class JobDetailsResponse extends Response implements Parcelable{

    @SerializedName("Jobs")
    @Expose
    private List<Job> jobs = null;

    public JobDetailsResponse(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(this.jobs);
    }

    protected JobDetailsResponse(Parcel in) {
        super(in);
        this.jobs = new ArrayList<Job>();
        in.readList(this.jobs, Job.class.getClassLoader());
    }

    public static final Creator<JobDetailsResponse> CREATOR = new Creator<JobDetailsResponse>() {
        @Override
        public JobDetailsResponse createFromParcel(Parcel source) {
            return new JobDetailsResponse(source);
        }

        @Override
        public JobDetailsResponse[] newArray(int size) {
            return new JobDetailsResponse[size];
        }
    };
}
