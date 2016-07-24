package com.app.operatorinfra;

public interface ErrorObjectInterface {

    enum ErrorCode {
        Unknown,
        Retrofit,
        SessionInvalid,
        Credentials_mismatch,
        Url_not_correct,
        Get_operator_failed,
        Set_operator_for_machine_failed
    }

    ErrorCode getError();

    String getDetailedDescription();
}
