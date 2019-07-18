package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActionsUpdateRequest;
import com.example.common.StandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldPostUpdateActions {

    @POST("/LeaderMESApi/SaveOperatorActions")
    Call<StandardResponse> postUpdtaeActionsRequest(@Body ActionsUpdateRequest actionsUpdateRequest);
}
