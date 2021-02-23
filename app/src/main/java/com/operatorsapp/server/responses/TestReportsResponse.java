package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TestReportsResponse {

    @SerializedName("Columns")
    ArrayList<TestReportColumn> mColumns;

    @SerializedName("Rows")
    ArrayList<TestReportRow> mRows;

    public ArrayList<TestReportColumn> getmolumns() {
        return mColumns;
    }

    public ArrayList<TestReportRow> getRows() {
        return mRows;
    }
}
