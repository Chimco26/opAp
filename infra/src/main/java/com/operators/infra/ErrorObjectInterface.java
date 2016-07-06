package com.operators.infra;

public interface ErrorObjectInterface {

    enum ErrorCode {
        Unknown,
        Retrofit,
        SessionInvalid,
        Credentials_mismatch,
        Get_machines_failed
    }

    ErrorCode getError();

    String getDetailedDescription();
}
