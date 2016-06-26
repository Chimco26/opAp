package com.operators.infra;

/**
 * Created by Admin on 26-Jun-16.
 */
public interface ErrorObjectInterface {

    enum ErrorCode {
        Unknown,
        Retrofit,
        /*SessionInvalid,*/
        Credentials_mismatch
    }

    ErrorCode getError();

    String getDetailedDescription();
}
