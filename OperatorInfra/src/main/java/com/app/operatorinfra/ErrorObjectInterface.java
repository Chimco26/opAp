package com.app.operatorinfra;

public interface ErrorObjectInterface
{

    enum ErrorCode
    {
        Unknown,
        Retrofit,
        SessionInvalid,
        Credentials_mismatch,
        Url_not_correct,
        Get_jobs_list_failed,
        Start_job_for_machine_failed
    }

    ErrorCode getError();

    String getDetailedDescription();
}
