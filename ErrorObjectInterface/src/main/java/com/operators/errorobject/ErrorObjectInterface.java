package com.operators.errorobject;

public interface ErrorObjectInterface {

    enum ErrorCode {
        Unknown,
        Retrofit,
        No_data,
        SessionInvalid,
        Credentials_mismatch,
        Get_machines_failed,
        Url_not_correct,
        Get_machine_status_failed,
        Jobs_list_Is_Empty,
        Get_jobs_list_failed,
        Get_machine_parameters_failed,
        Get_operator_failed,
        Set_operator_for_machine_failed,
        Get_report_fields_failed,
        Send_Report_Failed,
        Get_active_jobs_for_machine_failed,
        Error_rest
    }

    ErrorCode getError();

    String getDetailedDescription();
}
