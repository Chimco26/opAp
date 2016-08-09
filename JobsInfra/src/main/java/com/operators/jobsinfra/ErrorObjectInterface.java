package com.operators.jobsinfra;

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
        Jobs_list_Is_Empty,
        Start_job_for_machine_failed
    }

    ErrorCode getError();

    String getDetailedDescription();
}
