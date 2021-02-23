package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

public class TestReportColumn {

    @SerializedName("FieldName")
    String mFieldName;

    @SerializedName("DisplayEName")
    String mDisplayEName;

    @SerializedName("DisplayHName")
    String mDisplayHName;

    public String getFieldName() {
        return mFieldName;
    }

    public String getDisplayEName() {
        return mDisplayEName;
    }

    public String getDisplayHName() {
        return mDisplayHName;
    }
}
