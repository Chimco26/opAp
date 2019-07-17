package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.operators.reportrejectnetworkbridge.server.response.StandardResponse;

import java.util.ArrayList;
import java.util.List;

public class JobDetailsStandardResponse extends StandardResponse implements Parcelable{

    public static final String TAG = JobDetailsStandardResponse.class.getSimpleName();
    @SerializedName("Jobs")
    @Expose
    private List<Job> jobs = null;

    public JobDetailsStandardResponse(List<Job> jobs) {
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

    protected JobDetailsStandardResponse(Parcel in) {
        super(in);
        this.jobs = new ArrayList<Job>();
        in.readList(this.jobs, Job.class.getClassLoader());
    }

    public static final Creator<JobDetailsStandardResponse> CREATOR = new Creator<JobDetailsStandardResponse>() {
        @Override
        public JobDetailsStandardResponse createFromParcel(Parcel source) {
            return new JobDetailsStandardResponse(source);
        }

        @Override
        public JobDetailsStandardResponse[] newArray(int size) {
            return new JobDetailsStandardResponse[size];
        }
    };
}
