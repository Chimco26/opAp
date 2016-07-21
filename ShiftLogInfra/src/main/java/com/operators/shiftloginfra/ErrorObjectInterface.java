package com.operators.shiftloginfra;

public interface ErrorObjectInterface {

    enum ErrorCode {
        Unknown,
        Retrofit,
        SessionInvalid,
        Credentials_mismatch,
        Get_machines_failed,
        Url_not_correct
    }

    ErrorCode getError();

    String getDetailedDescription();
}
