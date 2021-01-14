package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

public class TranslateForTest {


    @SerializedName("ColumnName")
    String columnName;

    @SerializedName("Value")
    String translation;

    public String getColumnName() {
        return columnName;
    }

    public String getTranslation() {
        return translation;
    }
}
