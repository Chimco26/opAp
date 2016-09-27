package com.operators.errorobject;

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

    ErrorCode getError();

    String getDetailedDescription();
}
