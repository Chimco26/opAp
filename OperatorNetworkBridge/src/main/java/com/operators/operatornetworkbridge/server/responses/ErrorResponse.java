package com.operators.operatornetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("ErrorDescription")
    private String mErrorDesc;

    @SerializedName("ErrorMessage")
    private String mErrorMessage;

    @SerializedName("ErrorCode")
    private int mErrorCode;

    public String getErrorDesc() {
        return mErrorDesc;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

}
