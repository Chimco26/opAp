package com.operatorsapp.utils;

import java.util.UUID;

public class IdUtils
{
    public static String generateId()
    {
        UUID id = UUID.randomUUID();
        return id.toString();
    }
}
