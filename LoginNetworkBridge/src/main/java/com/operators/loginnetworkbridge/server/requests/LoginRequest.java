package com.operators.loginnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

import java.util.TimeZone;

public class LoginRequest {
    public static final String PLATFORM = "mobile";

    @SerializedName("Username")
    private String mUserName;

    @SerializedName("EncryptedPassword")
    private String mPassword;

    @SerializedName("Lang")
    private String mLanguage;

    @SerializedName("Platform")
    public String mPlatform = PLATFORM;

    @SerializedName("TimeZone")
    private String mTimeZone = TimeZone.getDefault().getID();

    public LoginRequest(String userName, String password, String language) {
        mUserName = userName;
        mPassword = password;
        mLanguage = language;
    }
}
