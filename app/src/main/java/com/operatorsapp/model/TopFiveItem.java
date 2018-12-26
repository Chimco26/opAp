package com.operatorsapp.model;

/**
 * Created by alex on 19/12/2018.
 */

public class TopFiveItem {

    String mAmount;
    String mText;
    String mColor;

    public TopFiveItem(String mAmount, String mText, String mColor) {
        this.mAmount = mAmount;
        this.mText = mText;
        this.mColor = mColor;
    }

    public String getmColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }
}
