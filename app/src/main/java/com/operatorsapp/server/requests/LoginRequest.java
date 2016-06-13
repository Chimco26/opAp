package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by slava-android on 1/7/2016.
 */
public class LoginRequest
{
    @SerializedName("Username")
    private String mUserName;

    @SerializedName("EncryptedPassword")
    private String mPassword;

    @SerializedName("Lang")
    private String mLang = "heb";

    @SerializedName("Platform")
    private String mPlatform = "mobile";

    public LoginRequest(String userName, String password)
    {
        mUserName = userName;
        mPassword = password;
    }
}
