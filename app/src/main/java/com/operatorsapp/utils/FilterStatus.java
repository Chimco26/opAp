package com.operatorsapp.utils;


public class FilterStatus {
    private String mName;
    private String mColor;

    public FilterStatus(String mName, String mColor) {
        this.mName = mName;
        this.mColor = mColor;
    }

    public String getName() {
        return mName;
    }

    public String getColor() {
        return mColor;
    }
}
