package com.example.common.utils;

import com.google.gson.Gson;

public class GsonHelper {

    public static String toJson(Object o){
        Gson gson = new Gson();
        return gson.toJson(o);
    }
}
