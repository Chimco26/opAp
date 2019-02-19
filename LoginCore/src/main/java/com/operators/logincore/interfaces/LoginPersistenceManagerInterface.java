package com.operators.logincore.interfaces;

import java.util.ArrayList;

public interface LoginPersistenceManagerInterface {

    String getSiteUrl();

    String getUserName();

    String getPassword();

    String getSessionId();

    int getTotalRetries();

    int getRequestTimeout();

    String getCurrentLang();

    void setSiteUrl(String siteUrl);

    void setUsername(String username);

    void setPassword(String password);

    void setSessionId(String sessionId);

    void setTotalRetries(int totalRetries);

    void setRequestTimeOut(int requestTimeOut);

    void setCurrentLang(String lang);

    void setUserId(int userId);
}
