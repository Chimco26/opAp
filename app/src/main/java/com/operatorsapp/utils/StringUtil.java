package com.operatorsapp.utils;

public class StringUtil {

    public static String getResizedString(String s, int length) {

        if (s == null || s.length() <= length) {
            return s;

        } else {

            return s.substring(0, length) + "...";
        }
    }
}
