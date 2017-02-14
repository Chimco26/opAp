package com.operators.jobsinfra;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 04/08/2016.
 */

public class Header {

    @SerializedName("FieldName")
    private String mFieldName;
    @SerializedName("DisplayEName")
    private String mDisplayEName;
    @SerializedName("DisplayHName")
    private String mDisplayHName;
    @SerializedName("FormID")
    private Object mFormID;
    @SerializedName("linkitem")
    private String mLinkItem;
    @SerializedName("DisplayType")
    private String mDisplayType;
    @SerializedName("CustomLinkItem")
    private boolean mCustomLinkItem;

    public Header(String fieldName, String displayEName, Object formID, String linkitem, String displayType) {
        mFieldName = fieldName;
        mDisplayEName = displayEName;
        mFormID = formID;
        mLinkItem = linkitem;
        mDisplayType = displayType;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public String getDisplayHName()
    {
        return mDisplayHName;
    }

    public String getDisplayEName() {
        return mDisplayEName;
    }

    public Object getFormID() {
        return mFormID;
    }

    public String getLinkItem() {
        return mLinkItem;
    }

    public String getDisplayType() {
        return mDisplayType;
    }

    public boolean isCustomLinkItem()
    {
        return mCustomLinkItem;
    }
}

