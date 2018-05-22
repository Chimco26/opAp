package com.operators.loginnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

import java.util.TimeZone;

public class LoginRequest {
    @SerializedName("Username")
    private String mUserName;

    @SerializedName("EncryptedPassword")
    private String mPassword;

    @SerializedName("Lang")
    private String mLanguage;

    @SerializedName("Platform")
    private String mPlatform = "mobile";

    @SerializedName("TimeZone")
    private String mTimeZone = TimeZone.getDefault().getID();

    public LoginRequest(String userName, String password, String language) {
        mUserName = userName;
        mPassword = password;
        mLanguage = language;
    }
}
