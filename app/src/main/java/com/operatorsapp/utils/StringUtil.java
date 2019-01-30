package com.operatorsapp.utils;

import java.util.Locale;

public class StringUtil {

    public static String getResizedString(String s, int length) {

        if (s == null || s.length() <= length) {
            return s;

        } else {

            return s.substring(0, length) + "...";
        }
    }

    public static String add0ToNumber(int number) {
        if (number > 9){
            return String.valueOf(number);
        }else {
            return String.format(Locale.getDefault(), "0%d", number);
        }
    }
}
