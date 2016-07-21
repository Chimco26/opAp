package com.operators.shiftlognetworkbridge.server.responses;


import com.google.gson.annotations.SerializedName;
import com.operators.shiftloginfra.ShiftLog;

import java.util.ArrayList;

public class ShiftLogResponse extends ErrorBaseResponse {
    @SerializedName("events")
    private ArrayList<ShiftLog> mShiftLogs;

    public ArrayList<ShiftLog> getShiftLogs() {
        return mShiftLogs;
    }
}