package com.operatorsapp.server.responses;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.operatorsapp.utils.Consts;

/**
 * Created by alex on 06/02/2019.
 */

public class NotificationText {
    @SerializedName("Name")
    private String mText;

    @SerializedName("MachineName")
    private String mMachineName;

    @SerializedName("Value")
    private String mValue;


    public NotificationText(String mText, String mMachineName, String mValue) {
        this.mText = mText;
        this.mMachineName = mMachineName;
        this.mValue = mValue;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmMachineName() {
        return mMachineName;
    }

    public void setmMachineName(String mMachineName) {
        this.mMachineName = mMachineName;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }

    public String getFullText(Context context){

        try {
            int keyText = context.getResources().getIdentifier(getmText(), "string", context.getPackageName());
            String body = String.format(context.getString(keyText),getmMachineName(),getmValue());
            return body;
        }catch (Exception e){
            return getmText();
        }
    }
}
