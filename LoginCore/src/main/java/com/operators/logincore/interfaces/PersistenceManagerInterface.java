package com.operators.logincore.interfaces;

import java.util.ArrayList;

public interface PersistenceManagerInterface {

    String getSiteUrl();

    String getUserName();

    String getPassword();

    String getSessionId();


    void setSiteUrl(String siteUrl);

    void setUsername(String username);

    void setPassword(String password);

    void setSessionId(String sessionId);

}
