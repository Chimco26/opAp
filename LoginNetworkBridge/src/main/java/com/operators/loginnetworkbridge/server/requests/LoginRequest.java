package com.operators.loginnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("Username")
    private String mUserName;

    @SerializedName("EncryptedPassword")
    private String mPassword;

    @SerializedName("Lang")
    private String mLanguage;

    @SerializedName("Platform")
    private String mPlatform = "mobile";

    public LoginRequest(String userName, String password, String language) {
        mUserName = userName;
        mPassword = password;
        mLanguage = language;
    }
}
