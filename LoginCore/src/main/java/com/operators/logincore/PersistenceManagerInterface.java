package com.operators.logincore;

import java.util.ArrayList;

public interface PersistenceManagerInterface {

    String getSiteUrl();

    String getUserName();

    String getPassword();

    String getSessionId();

    ArrayList getMachines();

    void setSiteUrl(String siteUrl);

    void setUsername(String username);

    void setPassword(String password);

    void setSessionId(String sessionId);

    void saveMachines(ArrayList machines);
}
