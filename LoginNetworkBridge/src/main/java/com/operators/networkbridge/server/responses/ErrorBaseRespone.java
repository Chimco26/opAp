package com.operators.networkbridge.server.responses;

import com.google.gson.annotations.SerializedName;

public class ErrorBaseRespone {
    @SerializedName("error")
    ErrorResponse mErrorResponse;

    public ErrorResponse getErrorResponse() {
        return mErrorResponse;
    }
}
