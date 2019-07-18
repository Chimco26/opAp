package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.PostUpdateNotesForJobRequest;
import com.example.common.StandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alex on 01/08/2018.
 */

public interface EmeraldPostUpdateNotesForJob {

    @POST("/LeaderMESApi/UpdateJobNotes")
    Call<StandardResponse> postUpdateNotesForJobRequest(@Body PostUpdateNotesForJobRequest postUpdateNotesForJobRequest);
}
