package com.example.common.callback;

public interface ErrorObjectInterface {

    enum ErrorCode {
        Unknown,
        Retrofit,
        Server,
        No_data,
        SessionInvalid,
        Credentials_mismatch,
        Url_not_correct,
        Missing_reports
    }

//    ErrorResponse getError();
//
//    String getDetailedDescription();
}
