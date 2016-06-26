package com.operatorsapp.models;

public class Site {
    private String mSiteUrl;
    private String mSessionId;
    private String mUserName;
    private String mPassword;

    public Site(String siteUrl, String sessionId, String userName, String password) {
        mSiteUrl = siteUrl;
        mSessionId = sessionId;
        mUserName = userName;
        mPassword = password;
    }


    public String getSiteUrl() {
        return mSiteUrl;
    }

    public String getSessionId() {
        return mSessionId;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }


    public void setSiteUrl(String siteUrl) {
        mSiteUrl = siteUrl;
    }

    public void setSessionId(String sessionId) {
        mSessionId = sessionId;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

}
