package com.operators.infra;

public interface ErrorObjectInterface {

    enum ErrorCode {
        Unknown,
        Retrofit,
        SessionInvalid,
        Credentials_mismatch
    }

    ErrorCode getError();

    String getDetailedDescription();
}
