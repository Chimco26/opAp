package com.operators.getmachinesnetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;

public class ErrorBaseResponse {
    @SerializedName("error")
    ErrorResponse mErrorResponse;

    public ErrorResponse getErrorResponse() {
        return mErrorResponse;
    }
}
