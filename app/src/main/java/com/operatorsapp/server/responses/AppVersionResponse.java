package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppVersionResponse extends PojoResponse{

    @SerializedName("ApplicationVersion")
    ArrayList<ApplicationVersion> mAppVersion;

    public ArrayList<ApplicationVersion> getmAppVersion() {
        return mAppVersion;
    }

    public void setmAppVersion(ArrayList<ApplicationVersion> mAppVersion) {
        this.mAppVersion = mAppVersion;
    }

    public class ApplicationVersion {

        @SerializedName("Application")
        String mAppName;

        @SerializedName("Version")
        float mVersionNumber;

        @SerializedName("Url")
        String mUrl;

        public String getmAppName() {
            return mAppName;
        }

        public String getmUrl() {
            return mUrl;
        }

        public void setmUrl(String mUrl) {
            this.mUrl = mUrl;
        }

        public void setmAppName(String mAppName) {
            this.mAppName = mAppName;
        }

        public float getmAppVersion() {
            return mVersionNumber;
        }

        public void setmAppVersion(int mAppVersion) {
            this.mVersionNumber = mAppVersion;
        }
    }
}
