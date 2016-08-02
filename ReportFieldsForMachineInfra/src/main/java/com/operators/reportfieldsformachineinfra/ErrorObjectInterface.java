package com.operators.reportfieldsformachineinfra;

public interface ErrorObjectInterface
{

    enum ErrorCode
    {
        Unknown,
        Retrofit,
        SessionInvalid,
        Credentials_mismatch,
        Url_not_correct,
        Get_report_fields_failed

    }

    ErrorCode getError();

    String getDetailedDescription();
}
