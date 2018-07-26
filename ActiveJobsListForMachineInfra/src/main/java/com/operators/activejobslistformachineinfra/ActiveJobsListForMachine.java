package com.operators.activejobslistformachineinfra;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 14/08/2016.
 */
public class ActiveJobsListForMachine implements Parcelable {
    @SerializedName("Joshs")
    private List<ActiveJob> jobs = new ArrayList<ActiveJob>();

    public ActiveJobsListForMachine(List<ActiveJob> jobs) {
        this.jobs = jobs;
    }

    public List<ActiveJob> getActiveJobs() {
        return jobs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.jobs);
    }

    protected ActiveJobsListForMachine(Parcel in) {
        this.jobs = new ArrayList<ActiveJob>();
        in.readList(this.jobs, ActiveJob.class.getClassLoader());
    }

    public static final Parcelable.Creator<ActiveJobsListForMachine> CREATOR = new Parcelable.Creator<ActiveJobsListForMachine>() {
        @Override
        public ActiveJobsListForMachine createFromParcel(Parcel source) {
            return new ActiveJobsListForMachine(source);
        }

        @Override
        public ActiveJobsListForMachine[] newArray(int size) {
            return new ActiveJobsListForMachine[size];
        }
    };
}
