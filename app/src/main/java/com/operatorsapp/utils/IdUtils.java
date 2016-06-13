package com.operatorsapp.utils;

import java.util.UUID;

/**
 * Created by Omri Bager on 2/25/2016.
 * Falcore LTD
 */
public class IdUtils
{
    public static String generateId()
    {
        UUID id = UUID.randomUUID();
        return id.toString();
    }
}
