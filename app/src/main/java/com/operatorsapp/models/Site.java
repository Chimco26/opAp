package com.operatorsapp.models;

/**
 * Created by slava-android on 1/13/2016.
 */
public class Site
{
    private String mId;
    private String mSiteName;
    private String mSiteUrl;
    private String mSessionId;
    private String mUserName;
    private String mPassword;

    public Site(String id, String siteName, String siteUrl, String sessionId, String userName, String password)
    {
        mId = id;
        mSiteName = siteName;
        mSiteUrl = siteUrl;
        mSessionId = sessionId;
        mUserName = userName;
        mPassword = password;
    }

    public String getSiteName()
    {
        return mSiteName;
    }

    public String getSiteUrl()
    {
        return mSiteUrl;
    }

    public String getSessionId()
    {
        return mSessionId;
    }

    public String getUserName()
    {
        return mUserName;
    }

    public String getPassword()
    {
        return mPassword;
    }

    public String getSiteId()
    {
        return mId;
    }

    public void setSiteUrl(String siteUrl)
    {
        mSiteUrl = siteUrl;
    }

    public void setSessionId(String sessionId)
    {
        mSessionId = sessionId;
    }

    public void setUserName(String userName)
    {
        mUserName = userName;
    }

    public void setPassword(String password)
    {
        mPassword = password;
    }

    public void setSiteName(String siteName)
    {
        mSiteName = siteName;
    }
}
