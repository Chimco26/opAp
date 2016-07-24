package com.operators.machinestatusinfra;

public interface ErrorObjectInterface
{

    enum ErrorCode
    {
        Unknown,
        Retrofit,
        SessionInvalid,
        Credentials_mismatch,
        Get_machines_failed,
        Url_not_correct,
        Get_operator_failed
    }

    ErrorCode getError();

    String getDetailedDescription();
}
