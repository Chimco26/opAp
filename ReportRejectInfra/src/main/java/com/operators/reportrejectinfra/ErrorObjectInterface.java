package com.operators.reportrejectinfra;

public interface ErrorObjectInterface {

    enum ErrorCode {
        Unknown,
        Retrofit,
        SessionInvalid,
        Credentials_mismatch,
        Url_not_correct,
        Send_Report_Failed
    }

    ErrorCode getError();

    String getDetailedDescription();
}
