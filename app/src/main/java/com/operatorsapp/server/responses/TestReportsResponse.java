package com.operatorsapp.server.responses;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TestReportsResponse extends StandardResponse {

    @SerializedName("ResponseDictionary")
    TestReports mTestReports;

    public TestReportsResponse() {
        super(true, 0, null);
        mTestReports = new TestReports();
    }

    public ArrayList<TestReportColumn> getColumns() {
        return mTestReports.mColumns == null ? new ArrayList<TestReportColumn>() : mTestReports.mColumns;
    }

    public ArrayList<TestReportRow> getRows() {
        return mTestReports.mRows == null ? new ArrayList<TestReportRow>() : mTestReports.mRows;
    }

    public void setColumns(ArrayList<TestReportColumn> mColumns) {
        mTestReports.mColumns = mColumns;
    }

    public void setRows(ArrayList<TestReportRow> mRows) {
        mTestReports.mRows = mRows;
    }

    public void addRow(TestReportRow row){
        if (mTestReports.mRows == null)
            mTestReports.mRows = new ArrayList<>();
        mTestReports.mRows.add(row);
    }

    private class TestReports {
        @SerializedName("Columns")
        ArrayList<TestReportColumn> mColumns;

        @SerializedName("Rows")
        ArrayList<TestReportRow> mRows;
    }
}
